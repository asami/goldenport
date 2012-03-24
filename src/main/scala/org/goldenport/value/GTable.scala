package org.goldenport.value

import org.goldenport.sdoc._

/**
 * @since   Jul. 27, 2008
 *          Dec. 23, 2008
 * @version Mar. 17, 2012
 * @author  ASAMI, Tomoharu
 */
trait GTable[E] extends GAttributedTabular[E] {
  def head: Option[GAttributedTabular[SDoc]]
  def headAsStringList: Option[List[String]]
  def setHead(head: GAttributedTabular[SDoc])
  def setHead(head: GTree[SDoc])
  def setHead(head: Seq[SDoc])
  def setHeadString(head: Seq[String])
  def side: Option[GAttributedTabular[SDoc]]
  def setSide(side: GAttributedTabular[SDoc])
  def setSide(side: GTree[SDoc])
  def setSide(side: Seq[SDoc])
  def setSideString(side: Seq[String])
  def headTree: Option[GTree[SDoc]]
  def sideTree: Option[GTree[SDoc]]
  def columnNames: Seq[SDoc]
  def rowNames: Seq[SDoc]
  def copyIn(aSrc: GTable[E]): Unit
}
