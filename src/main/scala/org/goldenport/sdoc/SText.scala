package org.goldenport.sdoc

import scala.xml._

/*
 * derived from SText.java since Sep. 17, 2006
 *
 * Sep.  1, 2008
 * Oct. 18, 2008
 */
case class SText(aString:String) extends SDoc {
  require (aString != null)
  val text_string:String = aString

  override def toString = text_string

  override def copy_Node(): SText = SText(text_string)

  override protected def build_Text(aBuffer: StringBuilder): StringBuilder = {
    aBuffer.append(text_string)
  }

  override def xml_Node() = Text(text_string)
}

object STextXml {
  def unapply(xnode: Node): Option[SText] = {
    if (xnode.isInstanceOf[Text]) Some(SText(xnode.toString()))
    else None
  }
}

object STextString {
  def unapply(string: String): Option[(SText, Option[String], Option[String])] = {
    if (string.length == 0) return None
    var body = string
    var tail: Option[String] = None
    var index = 0
    var done = false
    while (!done) {
      val c = string(index)
      if (USmartDoc.isSpecialChar(c)) {
	body = string.substring(0, index)
	tail = Some(string.substring(index))
	done = true
      }
      index += 1
      if (index == string.length) done = true
    }
    if (true) Some(SText(body), None, tail)
    else None
  }
}
