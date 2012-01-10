package org.goldenport.sdoc.inline

import scala.xml._
import java.util.UUID
import org.goldenport.sdoc._

/*
 * derived from SLAnchor.java since Sep. 20, 2006
 *
 * @since   Oct. 14, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SIAnchor(theChildren: SDoc*) extends SInline(theChildren: _*) {
  var href: String = ""
  var unresolvedRef: SUnresolvedRef = SNullRef
  var summary: SDoc = SEmpty

  override def copy_Node(): SIAnchor = {
    val anchor = SIAnchor()
    anchor.href = this.href
    anchor.unresolvedRef = this.unresolvedRef
    anchor
  }

  final def unresolvedRef_is(anRef: SUnresolvedRef): SIAnchor = {
    unresolvedRef = anRef
    this
  }

  final def summary_is(aSummary: SDoc): SIAnchor = {
    summary = aSummary
    this
  }
}

sealed abstract class SUnresolvedRef
case class SNullRef extends SUnresolvedRef

// XXX resolve policy - SmartDoc2HtmlStringTransformer
case class SObjectRef(val name: String) extends SUnresolvedRef

case class SHelpRef(val name: String, val params: Any*) extends SUnresolvedRef

case class SElementRef(val packageName: String, val objectName: String, val elementName: String) extends SUnresolvedRef {
  require (packageName != null && objectName != null && elementName != null)
  def this(packageName: String, objectName: String) = this(packageName, objectName, "")
  def this(packageName: String) = this(packageName, "", "")
}

object SNullRef extends SNullRef

object SIAnchorXml {
  val XmlTagName = "a"

  def unapply(xnode: Node): Option[SIAnchor] = {
    if (xnode.label == XmlTagName) Some(SIAnchor())
    else None
  }
}

object SIAnchorString {
  val WikiSymbolStart = '['
  val WikiSymbolEnd = ']'

  def unapply(string: String): Option[(SIAnchor, Option[String], Option[String])] = {
    if (string(0) != WikiSymbolStart) return None
    val (body: Option[String], tail: Option[String]) = USmartDoc.makeBody(string, WikiSymbolStart, WikiSymbolEnd)
    val anchor = new SIAnchor()
    Some((anchor, body, tail))
  }
}
