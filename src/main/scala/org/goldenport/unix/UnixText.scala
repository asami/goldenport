package org.goldenport.unix

/*
 * @since   Dec. 12, 2010
 * @version Dec. 12, 2010
 * @author  ASAMI, Tomoharu
 */
class UnixText(val lines: List[String]) extends IndexedSeq[String] {
  def length = lines.length
  def apply(idx: Int) = lines.apply(idx)
  def tabular = Tabular(lines)
}

object UnixText {
  def apply(text: String) = {
    val t = text.replace("\r\n", "\n").replace('\r', '\n')
    new UnixText(text.split("[\n]").toList)
  }

  def apply(text: Array[String]) = {
    new UnixText(text.toList)
  }
}
