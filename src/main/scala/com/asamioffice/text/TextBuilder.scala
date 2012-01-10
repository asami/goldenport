package com.asamioffice.text

import java.io.Flushable

/*
 * @since   Jan. 15, 2009
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class TextBuilder {
  private var _out: Appendable = _
  private var _flushable: Flushable = _
  private var _indent = 0
  private var _indent_width = 2
  private var _afterNl = true
  private var _eol = "\n"

  protected final def set_appendable(anOut: Appendable) {
    _out = anOut
    _flushable = _out match {
      case flushable: Flushable => flushable
      case _ => null
    }
  }

  private def print_indent() {
    if (_afterNl) {
      for (i <- 0 until _indent; w <- 0 until _indent_width) {
	_out.append(' ')
      }
    }
  }

  final def indentUp() {
    _indent += 1
  }

  final def indentDown() {
    _indent -= 1
  }

  final def print(n: Int) {
    print(n.toString)
  }

  final def print(aText: CharSequence) {
    print_indent()
    _out.append(aText)
    _afterNl = false
  }

  final def print(c: Char) {
    print_indent()
    _out.append(c)
    _afterNl = false
  }    

  final def println(aText: CharSequence) {
    print_indent()
    _out.append(aText)
    println()
  }

  final def println(c: Char) {
    print_indent()
    _out.append(c)
    println()
  }

  final def println() {
    _out.append(_eol)
    _afterNl = true
  }

  final def append(aText: CharSequence) {
    _out.append(aText)
  }

  final def append(aText: CharSequence, theReplaces: Map[String, String]) {
    var result = aText
    for ((regex, target) <- theReplaces.elements) {
      result = regex.r.replaceAllIn(result, target)
    }
    _out.append(result)
  }

  final def flush() = {
    if (_flushable != null) _flushable.flush
  }
}
