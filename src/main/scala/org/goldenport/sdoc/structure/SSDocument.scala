package org.goldenport.sdoc.structure

import scala.xml._
import org.goldenport.value.GTreeNode
import org.goldenport.sdoc._

/*
 * derived from SSDocument.java since Jan. 16, 2007
 *
 * @since   Sep.  2, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
case class SSDocument extends SStructure {
  private var doc_head: SSHead = _
  private var doc_body: SSBody = _

  final def head: SSHead = {
    require (doc_head != null)
    doc_head
  }

  final def head_=(aHead: SSHead): SSHead = {
    set_head(aHead)
    set_child(aHead)
    aHead
  }

  private def set_head(aHead: SSHead) {
    require (doc_head == null)
    doc_head = aHead
  }    

  final def body: SSBody = {
    require (doc_body != null)
    doc_body
  }

  final def body_=(aBody: SSBody): SSBody = {
    set_body(aBody)
    set_child(aBody)
    aBody
  }

  private def set_body(aBody: SSBody) {
    require (doc_body == null)
    doc_body = aBody
  }    

  override def set_Child(child: GTreeNode[SDoc]) {
    child match {
      case head: SSHead => set_head(head)
      case body: SSBody => set_body(body)
      case space: SText => 
      case _ => throw new SSyntaxErrorException("syntex error") // XXX
    }
  }

  override def xml_Element_Label = "doc"
}

object SSDocumentXml {
  def unapply(xnode: Node): Option[SSDocument] = {
    if (xnode.label == "doc") Some(SSDocument())
    else None
  }
}

object SSDocumentString {
  def unapply(string: String): Option[(SSDocument, Option[String], Option[String])] = None
}
