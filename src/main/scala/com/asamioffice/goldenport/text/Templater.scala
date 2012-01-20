package com.asamioffice.goldenport.text

/*
 * @since   Apr. 12, 2009
 * @version Apr. 12, 2009
 * @author  ASAMI, Tomoharu
 */
class Templater(aTemplate: CharSequence, theReplaces: Map[String, String]) {
  private var _text = aTemplate
  private var _buffer: StringTextBuilder = null

  for ((regex, target) <- theReplaces.elements) {
    replace(regex, target)
  }

  def replace(aRegex: String, aTarget: String) {
    _text = aRegex.r.replaceAllIn(_text, aTarget)
  }

  def replace(aRegex: String)(aProc: TextBuilder => Unit) {
    _buffer = new StringTextBuilder
    aProc(_buffer)
    replace(aRegex, _buffer.toString)
    _buffer = null
  }

  def buffer: TextBuilder = {
    require (_buffer != null)
    _buffer
  }

  override def toString() = _text.toString
}
