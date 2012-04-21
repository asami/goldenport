package com.asamioffice.goldenport.text

import scala.xml.Elem
import scala.collection.mutable.ArrayBuffer

/**
 * @since   Apr.  7, 2012
 * @version Apr.  7, 2012
 * @author  ASAMI, Tomoharu
 */
// current implementation is copy from JavaTextMaker
abstract class ProgramTextMaker(aTemplate: CharSequence, theReplaces: Map[String, String]) extends TextMaker {
  var methodAutoIndent = true
  val annotations = new ArrayBuffer[String]

  if (aTemplate != null && theReplaces != null) {
    append(aTemplate, theReplaces)
    popup("}")
  } else if (aTemplate != null) {
    append(aTemplate)
    popup("}")
  }

  private var append_after_ln: Boolean = false
  private var append_indent = 0
  private var append_indent_width = 4
  private var append_eol = "\\n"

  def this() = this(null, null)
  def this(aTemplate: CharSequence) = this(aTemplate, null)

  def replace(aRegex: String)(aProc: TextMaker => Unit) {
    val buffer = new TextMaker() {}
    aProc(buffer)
    replace(aRegex, buffer.toString)
  }

  def makeAnnotation(annotation: String, params: AnyRef*) {
    annotations += format_params(annotation, params)
  }

  private def _consume_annotations() {
    annotations.foreach(println(_))
    annotations.clear()
  }

  def method(signature: String, params: AnyRef*)(content: => Unit) {
    println()
    _consume_annotations()
    if (methodAutoIndent) {
      indentUp
    }
    if (params.isEmpty) {
      print(signature)
    } else {
      print(signature.format(params: _*))
    }
    println(" {")
    indentUp
    content
    indentDown
    println("}")
    if (methodAutoIndent) {
      indentDown
    }
  }

  def constructor(signature: String, params: AnyRef*)(content: => Unit) {
    method(signature, params: _*)(content)
  }

  def makeVar(className: String, varName: String) {
    print(className)
    print(" ")
    print(varName)
    print(" = new ")
    print(className)
    println("();")
  }

  def makeVarNull(className: String, varName: String) {
    print(className)
    print(" ")
    print(varName)
    println(" = null;")
  }

  def makeVar(typeName: String, varName: String, expr: String, params: AnyRef*) {
    print(typeName)
    print(" ")
    print(varName)
    print(" = ")
    print(format_params(expr, params))
    println(";")
  }

  def makeVarString(varName: String, expr: String, params: AnyRef*) {
    makeVar("String", varName, expr, params: _*)
  }

  // XXX assign
  def makeSetVarNull(varName: String) {
    print(varName)
    println(" = null;")
  }

  // if
  def makeIf(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeIfNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeIfNotNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeIfReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeIfReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    indentDown
    println("}")
    this
  }

  def makeIfNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeIfNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    indentDown
    println("}")
    this
  }

  def makeIfNotNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeIfNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    indentDown
    println("}")
    this
  }

  // if else
  def makeIfElse(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    this
  }

  def makeIfElseNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    this
  }

  def makeIfElseNotNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    this
  }

  def makeIfElseReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    this
  }

  def makeIfElseReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print(format_params(expr, eparams))
    this
  }

  def makeIfElseNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    this
  }

  def makeIfElseNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    this
  }

  def makeIfElseNotNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    this
  }

  def makeIfElseNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    this
  }

  // else if
  def makeElseIf(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    indentDown
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeElseIfNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeElseIfNotNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeElseIfReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeElseIfReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print(format_params(expr, eparams))
    indentDown
    println("}")
    this
  }

  def makeElseIfNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeElseIfNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    indentDown
    println("}")
    this
  }

  def makeElseIfNotNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeElseIfNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    indentDown
    println("}")
    this
  }

  // else if else
  def makeElseIfElse(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    indentDown
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    this
  }

  def makeElseIfElseNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    this
  }

  def makeElseIfElseNotNull(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    this
  }

  def makeElseIfElseReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    this
  }

  def makeElseIfElseReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print(format_params(expr, eparams))
    this
  }

  def makeElseIfElseNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    this
  }

  def makeElseIfElseNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    this
  }

  def makeElseIfElseNotNullReturn(condition: String, params: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    this
  }

  def makeElseIfElseNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): ProgramTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    this
  }

  // else
  def makeElse(body: => Unit): ProgramTextMaker = {
    indentDown
    println("} else {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  // for
  def makeFor(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("for (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeFor(typeName: String, varName: String, generator: String)(body: => Unit): ProgramTextMaker = {
    makeFor(typeName + " " + varName + ": " + generator)(body) 
    this
  }

  def makeWhile(condition: String, params: AnyRef*)(body: => Unit): ProgramTextMaker = {
    print("while (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeSwitch(cond: String, params: AnyRef*)(body: => Unit) {
    print("switch(")
    print(format_params(cond, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
  }

  def makeCase(pattern: String)(body: => Unit) {
    indentDown
    println("case %s: {", pattern)
    indentUp
    body
    indentDown
    println("}")
    indentUp
  }

  def makeCaseReturn(pattern: String, form: String, params: AnyRef*) {
    indentDown
    println("case %s:", pattern)
    indentUp
    println("return %s;", format_params(form, params))
  }

  def makeCaseDefault(body: => Unit) {
    indentDown
    println("default:")
    indentUp
    body
  }

  def makeTry(body: => Unit) {
    println("try {")
    indentUp
    body
  }

  def makeCatch(condition: String, params: AnyRef*)(body: => Unit) {
    indentDown
    print("} catch(")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
  }

  def makeCatchEnd(condition: String, params: AnyRef*)(body: => Unit) {
    indentDown
    print("} catch(")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
  }

  def makeCatchNop(condition: String, params: AnyRef*) {
    indentDown
    print("} catch(")
    print(format_params(condition, params))
    println(") {}")
  }

  def makeFinally(body: => Unit) {
    indentDown
    println("} finally {")
    indentUp
    body
    indentDown
    println("}")
  }

  def makeReturn(expr: String, params: AnyRef*): ProgramTextMaker = {
    print("return ")
    print(format_params(expr, params))
    println(";")
    this
  }

  def makeReturn(): ProgramTextMaker = {
    println("return;")
    this
  }

  def makeReturnNull(): ProgramTextMaker = {
    println("return null;")
    this
  }

  def makeReturnTrue(): ProgramTextMaker = {
    println("return true;")
    this
  }

  def makeReturnFalse(): ProgramTextMaker = {
    println("return false;")
    this
  }

  def makeReturnThis(): ProgramTextMaker = {
    println("return this;")
    this
  }

  def makeString(string: String, params: AnyRef*) {
    print("\"")
    print(UJavaString.escapeJavaText(format_params(string, params)))
    print("\"")
  }

  def makeStringBuilderVar() {
    makeVar("StringBuilder", "buf")
  }

  def makeStringBuilderReturn() {
    println("return buf.toString();")
  }

  // Append
  def makeAppendExpr(expr: String, params: AnyRef*) {
    makeAppendVarExpr("buf", format_params(expr, params))
  }

  def makeAppendVarExpr(varName: String, expr: String, params: AnyRef*) {
    print(varName)
    print(".append(")
    print(format_params(expr, params))
    println(");")
    append_after_ln = false
  }

  def makeAppendString(string: String, params: AnyRef*) {
    makeAppendVarString("buf", string, params: _*)
  }

  def makeAppendStringln(string: String, params: AnyRef*) {
    makeAppendVarStringln("buf", string, params: _*)
  }

  def makeAppendVarString(varName: String, string: String, params: AnyRef*) {
    make_append_indent(varName)
    print(varName)
    print(".append(\"")
    print(UJavaString.escapeJavaText(format_params(string, params)))
    println("\");")
    append_after_ln = false
  }

  def makeAppendVarStringln(varName: String, string: String, params: AnyRef*) {
    make_append_indent(varName)
    print(varName)
    print(".append(\"")
    print(UJavaString.escapeJavaText(format_params(string, params)))
    println(append_eol + "\");")
    append_after_ln = true
  }

  def makeAppendln() {
    makeAppendln("buf")
  }

  def makeAppendln(varName: String) {
    print(varName)
    println(".append(\"" + append_eol + "\");")
    append_after_ln = true
  }

  def makeAppendIndentUp() {
    append_indent += 1
  }

  def makeAppendIndentDown() {
    append_indent -= 1
  }

  private def make_append_indent(varName: String) {
    if (append_after_ln) {
      val buf = new StringBuilder
      for (i <- 0 until append_indent; w <- 0 until append_indent_width) {
        buf += ' '
      }
      print(varName)
      println(".append(\"" + buf + "\");")
    }
  }

  //
  def makeBuildTextXml(varName: String, xml: Elem) {
    def unescape_text(text: String) = {
      "&quot;".r.replaceAllIn(text, "\"")
    }

    val text = xml.toString
    for (line <- text.split("[\\n\\r]")) {
      var done = false
      var base = 0
      while (!done) {
        var prefix = "%{"
        var index = line.indexOf(prefix, base)
        if (index == -1) {
          prefix = "%*{"
          index = line.indexOf(prefix, base)
        }
        if (index == -1) {
          print(varName)
          print(".append(\"")
          print(UJavaString.escapeJavaText(line.substring(base)))
          println("\\n\");")
          done = true
        } else {
          if (base != index) {
            print(varName)
            print(".append(\"")
            print(UJavaString.escapeJavaText(line.substring(base, index)))
            println("\");")
          }
          val end = line.indexOf("}%", index)
          if (end == -1) {
            print(varName)
            println(".append(\"No }%\");")
            done = true
          } else {
            if (prefix == "%{") {
              print(varName)
              print(".append(")
              print(unescape_text(line.substring(index + prefix.length, end)))
              println(");")
              base = end + 2
            } else if (prefix == "%*{") {
              print(unescape_text(line.substring(index + prefix.length, end)))
              println(";")
              base = end + 2
            } else {
              print(varName)
              println(".append(\"No }%\");")
              done = true
            }
          }
        }
      }
    }
  }

  def makeTextXml(varName: String, xml: Elem) {
    val bufVarName = "buf" // XXX
    makeVar(bufVarName, "StringBuilder")
    makeBuildTextXml(bufVarName, xml)
    makeVar(varName, "String", bufVarName + ".toString()")
  }

  def makeReturnTextXml(xml: Elem) {
    val bufVarName = "buf" // XXX
    makeStringBuilderVar
    makeBuildTextXml(bufVarName, xml)
    makeStringBuilderReturn
  }

  // Extension
  // Derived from JavaMaker (not JavaTextMaker).
  private var _use_package = false
  private var _imports = new ArrayBuffer[String]
  private var _static_imports = new ArrayBuffer[String]
  def p(s: String, params: AnyRef*) {
    this.print(format_params(s, params));
  }

  def pln(s: String, params: AnyRef*) {
    this.println(format_params(s, params));
  }

  def pln() {
    this.println();
  }

  def declarePackage(name: String) {
      p("package ")
      p(name)
      pln(";")
      _use_package = true
  }

  def endPackageSection() {
    if (_use_package) {
      pln
    }
  }

  def declareImport(name: String) {
    _imports += name
  }

  def declareStaticImport(name: String) {
    _static_imports += name
  }
  
  def endImportSection() {
    val imports = _unify_imports
    for (name <- imports) {
      p("import ")
      p(name)
      pln(";")
    }
    for (name <- _static_imports) {
      p("import static ")
      p(name)
      pln(";")
    }
    if (imports.nonEmpty || _static_imports.nonEmpty) {
      pln
    }
  }

  private def _unify_imports: List[String] = {
    val (astas, plains) = _imports.toList.partition(_.endsWith(".*"))
    val astaspkgs = astas.map(s => s.dropRight(".*".length))
    val independents = plains.filter(s => {
      val index = s.lastIndexOf(".")
      if (index  == -1) true
      else {
        val pkg = s.substring(0, index)
        !astaspkgs.exists(_ == pkg)
      }
    })
    (astas ::: independents).distinct.sorted
  }

  def publicFinalInstanceVariableSingle(typename: String, varname: String) {
    pln("public final %s %s;".format(typename, varname))    
  }

  def publicFinalInstanceVariableList(typename: String, varname: String) {
    pln("public final List<%s> %s;".format(typename, varname))
  }

  def publicInstanceVariableSingle(typename: String, varname: String) {
    pln("public %s %s;".format(typename, varname))    
  }

  def publicInstanceVariableList(typename: String, varname: String) {
    pln("public List<%s> %s;".format(typename, varname))
  }

  def privateInstanceVariableSingle(typename: String, varname: String) {
    pln("private %s %s;".format(typename, varname))    
  }

  def privateInstanceVariableList(typename: String, varname: String) {
    pln("private List<%s> %s = new ArrayList<%s>();".format(typename, varname, typename))
  }

  def privateTransientInstanceVariableSingle(typename: String, varname: String) {
    pln("private transient %s %s;".format(typename, varname))    
  }

  def privateTransientInstanceVariableList(typename: String, varname: String) {
    pln("private transient List<%s> %s = new ArrayList<%s>();".format(typename, varname, typename))
  }

  // unify 
  def publicMethod(signature: String, params: AnyRef*)(body: => Unit) {
    method("public " + format_params(signature, params)) {
      body
    }
  }

/*
  private def _format(signature: String, params: Seq[AnyRef]) = {
    if (params.isEmpty) signature
    else {
      require (!params.exists(_.isInstanceOf[Seq[_]]), "JavaMaker#_format: invalid sequence parameter")
      signature.format(params: _*)
    }
  }
*/

  def publicVoidMethod(signature: String, params: AnyRef*)(body: => Unit) {
    method("public void " + format_params(signature, params)) {
      body
    }
  }

  def publicGetMethod(typeName: String, attrName: String, expr: AnyRef*) {
    method("public %s get%s()", typeName, attrName.capitalize) {
      makeReturn(attr_expr(attrName, expr))
    }
  }
/*
  def publicSetMethod(typeName: String, attrName: String, varName: String = null, paramName: String = null) {
    val vname = if (varName != null) varName else attrName
    val pname = if (paramName != null) paramName else attrName
    method("public void set%s(%s %s)", attrName.capitalize, typeName, pname) {
      assignThis(vname, pname)
    }
  }

  def publicWithMethod(className: String, typeName: String, attrName: String, varName: String = null, paramName: String = null) {
    val vname = if (varName != null) varName else attrName
    val pname = if (paramName != null) paramName else attrName
    method("public %s with%s(%s %s)", className, attrName.capitalize, typeName, pname) {
      assignThis(vname, pname)
      makeReturnThis
    }
  }
*/
  /**
   * publicGetOrNullMethod("int", "price", "goodPrice", "intValue()")
   */
  def publicGetOrNullMethod(typeName: String, attrName: String,
      varName: String, expr: String, params: AnyRef*) {
    val vname = _var_name(attrName, varName);
    method("public %s get%s()", typeName, attrName.capitalize) {
      makeIf("%s == null", vname) {
        makeReturn("null");
      }
      makeReturn("%s.%s;", vname, format_params(expr, params))
    }
  }

  private def _var_name(attrname: String, varname: String) = {
    if (varname != null) varname
    else attrname
  }

  protected final def attr_expr(attrName: String, expr: Seq[AnyRef]) = {
    if (expr.isEmpty) attrName
    else if (expr.length == 1) expr(0).toString
    else format_expr_params_array(expr)
  }

  protected final def format_expr_params_array(expr: Seq[AnyRef]) = {
    format_params(expr(0).toString, expr.tail)
  }

  def publicIsMethod(attrName: String, expr: AnyRef*) {
    method("public boolean is%s()", attrName.capitalize, attr_expr(attrName, expr)) {
    }
  }

  def publicSetMethod(attrName: String, typeName: String,
       paramName: String = null, varName: String = null, expr: Seq[AnyRef] = Nil) {
    val pname = if (paramName == null) attrName else paramName
    val vname = if (varName == null) attrName else varName
    method("public void set%s(%s %s)", attrName.capitalize, typeName, pname) {
      if (!expr.isEmpty) {
        assignThis(vname, expr)
      } else {
        assignThis(vname, pname)
      }
    } 
  }

  def publicSetOrNullMethod(attrName: String, typeName: String,
       paramName: String = null, varName: String = null, expr: Seq[AnyRef] = Nil) {
    val pname = if (paramName == null) attrName else paramName
    val vname = if (varName == null) attrName else varName
    method("public void set%s(%s %s)", attrName.capitalize, typeName, pname) {
      makeIf("%s == null", pname) {
        assignThisNull(vname);
      }
      if (!expr.isEmpty) {
        assignThis(vname, expr)
      } else {
        assignThis(vname, pname)
      }
    } 
  }

  def publicWithMethod(className: String, attrName: String, typeName: String,
       paramName: String = null, varName: String = null, expr: Seq[AnyRef] = Nil) {
    val pname = if (paramName == null) attrName else paramName
    val vname = if (varName == null) attrName else varName
    method("public %s with%s(%s %s)", className, attrName.capitalize, typeName, pname) {
      if (!expr.isEmpty) {
        assignThis(vname, expr)
      } else {
        assignThis(vname, pname)
      }
      makeReturnThis
    } 
  }

  def publicWithOrNullMethod(className: String, attrName: String, typeName: String,
       paramName: String, varName: String, expr: Seq[AnyRef]) {
    val pname = if (paramName == null) attrName else paramName
    val vname = if (varName == null) attrName else varName
    method("public %s with%s(%s %s)", className, attrName.capitalize, typeName, pname) {
      makeIf("%s == null", pname) {
        assignThisNull(vname);
      }
      if (!expr.isEmpty) {
        assignThis(vname, expr)
      } else {
        assignThis(vname, pname)
      }
      makeReturnThis
    } 
  }

  def protectedMethod(signature: String, params: AnyRef*)(body: => Unit) {
    method("protected " + format_params(signature, params)) {
      body
    }
  }

  def protectedFinalVoidMethod(signature: String, params: AnyRef*)(body: => Unit) {
    method("protected final void " + format_params(signature, params)) {
      body
    }
  }

  def privateMethod(signature: String, params: AnyRef*)(body: => Unit) {
    method("private " + format_params(signature, params)) {
      body
    }
  }

  def publicStaticMethod(signature: String, params: AnyRef*)(body: => Unit) {
    method("public static " + format_params(signature, params)) {
      body
    }
  }

  // constructor
  def publicConstructor(signature: String, params: AnyRef*)(body: => Unit) {
    constructor("public " + format_params(signature, params)) {
      body
    }
  }
  
  // static
  def staticBlock(body: => Unit) {
    pln("static {")
    indentUp
    body
    indentDown
    pln("}")
  }

  //
  def ifReturn(condition: String, return_value: String = null) {
    makeIf(condition) {
      if (return_value != null) {
        pln("return %s;".format(return_value))
      } else {
        pln("return;")
      }
    }
  }

  // var
  def varListNewArrayList(varname: String, classname: String) {
    pln("List<%s> %s = new ArrayList<%s>();".format(classname, varname, classname))
  }

  // assign
  def assign(varname: String, expr: String) {
      pln("%s = %s;".format(varname, expr))
  }

  def assign(varname: String, expr: String, args: AnyRef*) {
      pln("%s = %s;".format(varname, format_params(expr, args)))
  }

  def assignNull(varname: String) {
      pln("%s = null;".format(varname))
  }

  def assignTrue(varname: String) {
      pln("%s = true;".format(varname))
  }

  def assignFalse(varname: String) {
      pln("%s = false;".format(varname))
  }

  def assignNew(varname: String, newclass: String) {
    pln("%s = new %s();".format(varname, newclass))
  }

  def assignNew(varname: String, newclass: String, signature: String, params: AnyRef*) {
    val s = if (params.isEmpty) signature
            else format_params(signature, params)
    pln("%s = new %s(%s);".format(varname, newclass, s))
  }

  def assignNewContainer(varname: String, newclass: String, containee: String) {
    pln("%s = new %s<%s>();".format(varname, newclass, containee))
  }

  def assignNewContainer(varname: String, newclass: String, containee: String, signature: String, params: Seq[AnyRef] = Nil) {
    val s = if (params.isEmpty) signature
            else format_params(signature, params)
    pln("%s = new %s<%s>(%s);".format(varname, newclass, containee, s))
  }

  def assignNewArrayList(varname: String, classname: String) {
    assignNewContainer(varname, "ArrayList", classname)
  }

  def assignNewArrayList(varname: String, classname: String, signature: String, params: AnyRef*) {
    assignNewContainer(varname, "ArrayList", classname, signature, params)
  }

  // assignThis
  def assignThis(varname: String, expr: String) {
    pln("this.%s = %s;".format(varname, expr))
  }

  def assignThis(varname: String, expr: Seq[AnyRef]) {
    pln("this.%s = %s;".format(varname, format_expr(expr)))
  }

  def assignThis(varname: String, expr: String, args: AnyRef*) {
    pln("this.%s = %s;".format(varname, format_params(expr, args)))
  }

  def assignThisNull(varname: String) {
    pln("this.%s = null;".format(varname))
  }

  def assignThisTrue(varname: String) {
    pln("this.%s = true;".format(varname))
  }

  def assignThisNew(varname: String, newclass: String) {
    pln("this.%s = new %s();".format(varname, newclass))
  }

  def assignThisNew(varname: String, newclass: String, signature: String, params: Seq[AnyRef] = Nil) {
    val s = if (params.isEmpty) signature
            else format_params(signature, params)
    pln("this.%s = new %s(%s);".format(varname, newclass, s))
  }

  def assignThisNewContainer(varname: String, newclass: String, containee: String) {
    pln("this.%s = new %s<%s>();".format(varname, newclass, containee))
  }

  def assignThisNewContainer(varname: String, newclass: String, containee: String, signature: String, params: Seq[AnyRef] = Nil) {
    val s = if (params.isEmpty) signature
            else format_params(signature, params)
    pln("this.%s = new %s<%s>(%s);".format(varname, newclass, containee, s))
  }

  def assignThisNewArrayList(varname: String, classname: String) {
    assignThisNewContainer(varname, "ArrayList", classname)
  }

  def assignThisNewArrayList(varname: String, classname: String, signature: String, params: Seq[AnyRef] = Nil) {
    assignThisNewContainer(varname, "ArrayList", classname, signature, params)
  }
}
