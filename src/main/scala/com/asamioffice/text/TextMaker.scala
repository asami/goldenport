package com.asamioffice.text

import java.io.Flushable

import com.asamioffice.goldenport.text.UString;

/*
 * @since   May.  5, 2009
 * @version Aug. 21, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class TextMaker {
  type MAKER <: TextMaker
  private val _buffer = new StringMaker
  private var _indent = 0
  private var _afterNl = true
  var indentWidth = 4
  var eol = "\n"

  def append(aTemplate: CharSequence, theReplaces: Map[String, String]): MAKER = {
    var text = aTemplate
    for ((regex, target) <- theReplaces.elements) {
      text = regex.r.replaceAllIn(text, target)
    }
    append(text)
    this.asInstanceOf[MAKER]
  }

  def append(cs: CharSequence): MAKER = {
    _buffer.append(cs)
    this.asInstanceOf[MAKER]
  }

  def code(aTemplate: CharSequence, theReplaces: Map[String, String]): MAKER = {
    var text = aTemplate
    for ((regex, target) <- theReplaces.elements) {
      text = regex.r.replaceAllIn(text, target)
    }
    code(text)
    this.asInstanceOf[MAKER]
  }

  def code(cs: CharSequence): MAKER = {
    def parselines(lines: Seq[String]): Seq[(String, String)] = {
      for (l <- lines) yield {
        l.indexWhere(_ != ' ') match {
          case -1 => ("", l)
          case index => l.splitAt(index)
        }
      }
    }

    def calcindentwidth(lines: Seq[String]): Int = {
      lines.filter(!_.isEmpty) match {
        case Nil => 0
        case l => l.reduceLeft((l, r) => if (l.size < r.size) l else r).size
      }
    }

    def makeindents(lines: Seq[String], width: Int): Seq[String] = {
      if (width == 0) {
        lines
      } else {
        for (l <- lines) yield {
          " " * ((l.size / width) * indentWidth)
        }
      }
    }

    val lines = UString.getLineList(cs.toString).toList
    val pairs: Seq[(String, String)] = parselines(lines)
    val spaces = pairs.map(_._1)
    val widthofindent = calcindentwidth(spaces)
    val indents = makeindents(spaces, widthofindent)
    val indentedlines: Seq[(String, String)] = spaces.zip(pairs.map(_._2))
    for (l <- indentedlines) {
      print_indent
      _buffer.append(l._1)
      _buffer.append(l._2)
      println
    }
    this.asInstanceOf[MAKER]
  }

  def replace(aRegex: String, aTarget: String) {
    _buffer.replace(aRegex, aTarget)
  }

  private def print_indent() {
    if (_afterNl) {
      for (i <- 0 until _indent; w <- 0 until indentWidth) {
        _buffer.append(' ')
      }
    }
  }

  final def indentUp() {
    _indent += 1
  }

  final def indentDown() {
    _indent -= 1
  }

  final def print(any: Any) {
    print(any.toString)
  }

  def print(cs: CharSequence, params: AnyRef*): MAKER = {
    print_indent()
    _buffer.append(format_params(cs.toString, params))
    _afterNl = false
    this.asInstanceOf[MAKER]
  }

  final def print(c: Char) {
    print_indent()
    _buffer.append(c)
    _afterNl = false
  }    

  final def println(aText: CharSequence, params: AnyRef*) {
    print_indent()
    _buffer.append(format_params(aText.toString, params))
    println()
  }

  final def println(c: Char) {
    print_indent()
    _buffer.append(c)
    println()
  }

  final def println() {
    _buffer.append(eol)
    _afterNl = true
  }

  final def checkpoint: StringMakerCheckPoint = {
    _buffer.checkpoint
  }

  final def isModified(checkpoint: StringMakerCheckPoint) = {
    _buffer.isModified(checkpoint)
  }

  final def rollback(checkpoint: StringMakerCheckPoint) {
    _buffer.rollback(checkpoint)
  }

  final def popup(mark: String) {
    _buffer.popup(mark)
  }

  override def toString = {
    _buffer.pushback()
    _buffer.toString
  }

  protected final def format_params(format: String, params: Seq[Any]): String = {
    if (params.exists(_.isInstanceOf[Seq[_]])) {
      throw new IllegalArgumentException("TextMaker#format_params: " + format + ", " + params)
    }
    try {
      if (params.isEmpty) format
      else format.format(params: _*)
    } catch {
      case e: Throwable => throw new IllegalArgumentException(format + ":" + params) 
    }
  }

  protected final def format_expr(expr: Seq[Any]): String = {
    if (expr.exists(_.isInstanceOf[Seq[_]])) {
      throw new IllegalArgumentException("TextMaker#format_expr: " + expr)
    }
    try {
      if (expr.isEmpty) ""
      else expr.head.toString.format(expr.tail: _*)
    } catch {
      case e: Throwable => throw new IllegalArgumentException(expr.toString) 
    }
  }
}
