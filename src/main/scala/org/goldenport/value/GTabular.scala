package org.goldenport.value

/*
 * @since   Jul. 26, 2008
 * @version Jul. 16, 2010
 * @version Dec.  1, 2011
 * @author  ASAMI, Tomoharu
 */
trait GTabular[E] {
  def width: Int
  def height: Int
  def get(x: Int, y: Int): E
  def getOrElse(x: Int, y: Int, value: E): E
  def getOption(x: Int, y: Int): Option[E]
  def put(x: Int, y: Int, content: E): Option[E]
  def putOption(x: Int, y: Int, value: Option[E]): Option[E]
  def +=(seq: Seq[E])
/*
  def +=(c: E): Unit
  def +=(c1: E, c2: E): Unit
  def +=(c1: E, c2: E, c3: E): Unit
  def +=(c1: E, c2: E, c3: E, c4: E): Unit
  def +=(c1: E, c2: E, c3: E, c4: E, c5: E): Unit
  def +=(tuple: Product1[E]): Unit
  def +=(tuple: Product2[E, E]): Unit
  def +=(tuple: Product3[E, E, E]): Unit
  def +=(tuple: Product4[E, E, E, E]): Unit
  def +=(tuple: Product5[E, E, E, E, E]): Unit
//  def +=(tuple: Product): Unit
  def append(tuple: Product): Unit
*/
  def append(seq: Seq[E]): Unit
  def putAll(aTabular: GTabular[E]): Unit
  def rows: Seq[Seq[E]]
  def terseRows: Seq[Seq[E]]
  def rowCursor: Cursor

  abstract class Cursor {
    def +=(content: E): Unit
  }
}
