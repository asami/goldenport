package org.goldenport.unix

/*
 * @since   Dec. 12, 2010
 * @version Dec. 12, 2010
 * @author  ASAMI, Tomoharu
 */
class Tabular(val lines: List[List[String]]) extends IndexedSeq[List[String]] {
  def length = lines.length
  def apply(idx: Int) = lines.apply(idx)
}

object Tabular {
  def apply(lines: List[String], s: String = "\t") = {
    new Tabular(lines.map(_.split(s).toList))
  }
}
