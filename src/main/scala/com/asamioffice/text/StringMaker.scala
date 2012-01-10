package com.asamioffice.text

/*
 * @since   May.  7, 2009
 * @version May. 12, 2009
 * @author  ASAMI, Tomoharu
 */
class StringMaker {
  private var _builder = new java.lang.StringBuilder
  private var _popup_content: String = null

  def append(cs: CharSequence): StringMaker = {
    _builder.append(cs)
    this
  }

  def append(c: Char): StringMaker = {
    _builder.append(c)
    this
  }

  def replace(aRegex: String, aTarget: String) {
    val text = aRegex.r.replaceAllIn(_builder, aTarget)
    _builder = new java.lang.StringBuilder
    _builder.append(text)
  }

  def checkpoint: StringMakerCheckPoint = {
    new StringMakerCheckPoint(_builder.length)
  }

  def isModified(checkpoint: StringMakerCheckPoint): Boolean = {
    _builder.length == checkpoint.index
  }

  def rollback(checkpoint: StringMakerCheckPoint): StringMaker = {
    _builder.delete(checkpoint.index, _builder.length)
    this
  }

  final def popup(mark: String) {
    val index = _builder.lastIndexOf(mark)
    if (index != -1) {
      _popup_content = _builder.substring(index)
      _builder.delete(index, _builder.length)
    }
  }

  final def pushback() {
    if (_popup_content != null) {
      _builder.append(_popup_content)
      _popup_content = null
    }
  }

  override def toString = {
    _builder.toString
  }
}

class StringMakerCheckPoint(val index : Int) {
}
