package com.asamioffice.text

/*
 * Jan. 15, 2009
 * Jan. 27, 2009
 */
class StringTextBuilder extends TextBuilder {
  private val _buffer = new java.lang.StringBuilder
  set_appendable(_buffer)

  override def toString = {
    _buffer.toString
  }
}
