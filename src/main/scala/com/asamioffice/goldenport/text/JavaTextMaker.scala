package com.asamioffice.goldenport.text

import scala.xml.Elem
import scala.collection.mutable.ArrayBuffer

/*
 * @since   May.  5, 2009
 *  version Aug. 14, 2011
 * @version Nov.  3, 2012
 * @author  ASAMI, Tomoharu
 */
class JavaTextMaker(aTemplate: CharSequence, theReplaces: Map[String, String]) extends TextMaker {
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

  def replace(aRegex: String)(aProc: JavaTextMaker => Unit) {
    val buffer = new JavaTextMaker
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
  def makeIf(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeIfNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeIfNotNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeIfReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeIfReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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

  def makeIfNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeIfNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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

  def makeIfNotNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeIfNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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
  def makeIfElse(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    this
  }

  def makeIfElseNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    this
  }

  def makeIfElseNotNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    this
  }

  def makeIfElseReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    this
  }

  def makeIfElseReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print(format_params(expr, eparams))
    this
  }

  def makeIfElseNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    this
  }

  def makeIfElseNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    this
  }

  def makeIfElseNotNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    this
  }

  def makeIfElseNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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
  def makeElseIf(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
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

  def makeElseIfNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeElseIfNotNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeElseIfReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeElseIfReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print(format_params(expr, eparams))
    indentDown
    println("}")
    this
  }

  def makeElseIfNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeElseIfNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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

  def makeElseIfNotNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    indentDown
    println("}")
    this
  }

  def makeElseIfNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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
  def makeElseIfElse(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    indentDown
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    this
  }

  def makeElseIfElseNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    body
    this
  }

  def makeElseIfElseNotNull(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    body
    this
  }

  def makeElseIfElseReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    println("return;")
    this
  }

  def makeElseIfElseReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    print(format_params(expr, eparams))
    this
  }

  def makeElseIfElseNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    println("return;")
    this
  }

  def makeElseIfElseNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" == null) {")
    indentUp
    print("return ")
    print(format_params(expr, eparams))
    println(";")
    this
  }

  def makeElseIfElseNotNullReturn(condition: String, params: AnyRef*): JavaTextMaker = {
    print("} else if (")
    print(format_params(condition, params))
    println(" != null) {")
    indentUp
    println("return;")
    this
  }

  def makeElseIfElseNotNullReturnExpr(condition: String, params: AnyRef*)(expr: String, eparams: AnyRef*): JavaTextMaker = {
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
  def makeElse(body: => Unit): JavaTextMaker = {
    indentDown
    println("} else {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  // for
  def makeFor(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
    print("for (")
    print(format_params(condition, params))
    println(") {")
    indentUp
    body
    indentDown
    println("}")
    this
  }

  def makeFor(typeName: String, varName: String, generator: String)(body: => Unit): JavaTextMaker = {
    makeFor(typeName + " " + varName + ": " + generator)(body) 
    this
  }

  def makeWhile(condition: String, params: AnyRef*)(body: => Unit): JavaTextMaker = {
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

  def makeReturn(expr: String, params: AnyRef*): JavaTextMaker = {
    print("return ")
    print(format_params(expr, params))
    println(";")
    this
  }

  def makeReturn(): JavaTextMaker = {
    println("return;")
    this
  }

  def makeReturnNull(): JavaTextMaker = {
    println("return null;")
    this
  }

  def makeReturnTrue(): JavaTextMaker = {
    println("return true;")
    this
  }

  def makeReturnFalse(): JavaTextMaker = {
    println("return false;")
    this
  }

  def makeReturnThis(): JavaTextMaker = {
    println("return this;")
    this
  }

  def makeReturnVarOrNull(varname: String) {
    println("return %s != null ? %s : null;".format(varname, varname))
  }

  def makeReturnExprOrNull(varname: String, expr: String) {
    println("return %s != null ? %s : null;".format(varname, expr.format(varname)))
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
}
