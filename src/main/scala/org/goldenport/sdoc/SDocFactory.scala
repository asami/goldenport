package org.goldenport.sdoc

import scala.xml._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.structure._
import org.goldenport.sdoc.attribute._

/*
 * derived from SNodeFactory.java since Sep. 17, 2006
 *
 * Sep.  1, 2008
 * Jan. 26, 2009
 */
class SDocFactory(val rigid: Boolean) {
  def this() = this(false)

  final def create(text: String): SDoc = {
    try {
      if (text == null || text.length == 0) return SFragment()
      val snode = create(XML.loadString(
	if (text(0) != '<') "<fragment>" + text + "</fragment>"
	else text))
      if (snode.isInstanceOf[SFragment] && snode.length == 1) snode.getChild(0)
      else snode
    } catch {
      case e: SSyntaxErrorException => {
 	return SISpan(SText("Invalid smartdoc text (" + e + "): " + text)) style_is "color: red; background-color: yellow"
      }
      case e => {
	if (rigid) {
	  println("Invalid smartdoc text (" + e + "): " + text) // XXX
	  throw e
	} else {
	  return SISpan(SText("Invalid smartdoc text (" + e + "): " + text)) style_is "color: red; background-color: yellow"
	}
      }
    }
  }

  final def create(xnode: Node): SDoc = {
    if (xnode.isInstanceOf[Atom[String]])
      create_text(xnode.asInstanceOf[Atom[String]])
    else create_node(xnode)
  }

  private def create_text(anAtom: Atom[String]): SDoc = {
    create_text(anAtom.text)
  }

  private def create_node(xnode: Node): SDoc = {
    val snode = xnode match {
      case STextXml(node) => node
      case SIItalicXml(node) => node
      case SIBoldXml(node) => node
      case SIAnchorXml(node) => node
      case SBOrderedListXml(node) => node
      case SBUnorderedListXml(node) => node
      case SBListItemXml(node) => node
      case SBDefinitionListXml(node) => node
      case SBDefinitionTermXml(node) => node
      case SBDefinitionDescriptionXml(node) => node
      case SBTableXml(node) => node
      case SBTableHeadXml(node) => node
      case SBTableBodyXml(node) => node
      case SBTableFootXml(node) => node
      case SBTableRecordXml(node) => node
      case SBTableDataXml(node) => node
      case SBTableHeaderXml(node) => node
      case SBParagraphXml(node) => node
      case SBDivisionXml(node) => node
      case SSDocumentXml(node) => node
      case SSHeadXml(node) => node
      case SSBodyXml(node) => node
      case SSPartXml(node) => node
      case SSChapterXml(node) => node
      case SSSectionXml(node) => node
      case SSSubSectionXml(node) => node
      case SSSubSubSectionXml(node) => node
      case SATitleXml(node) => node
      case SFragmentXml(node) => node
      case _ => error("Unknown sdoc = " + xnode)
    }
    for (child <- xnode.child) {
      snode.addChildOrChildren(create(child))
    }
    snode
  }

  private def create_text(aText: String): SDoc = {
//    println("create_text = " + aText) ; 2008-10-13
    var text = aText
    val nodes = new SFragment
    while (text != null) {
      val (snode: SDoc, body: Option[String], tail: Option[String]) = text match {
        case SSSectionString(snode, body, tail) => (snode, body, tail)
        case SSSubSectionString(snode, body, tail) => (snode, body, tail)
        case SSSubSubSectionString(snode, body, tail) => (snode, body, tail)
        case SBUnorderedListString(snode, body, tail) => (snode, body, tail)
        case SBListItemString(snode, body, tail) => (snode, body, tail)
        case SBTableString(snode, body, tail) => (snode, body, tail)
        case SIItalicString(snode, body, tail) => (snode, body, tail)
        case SIBoldString(snode, body, tail) => (snode, body, tail)
        case SIAnchorString(snode, body, tail) => (snode, body, tail)
	case STextString(snode, body, tail) => (snode, body, tail)
	case _ => throw new SSyntaxErrorException("Unknown sdoc = " + text)
      }
      assert (snode != null && body != null && tail != null)
      if (body.isDefined) {
	snode.addChildOrChildren(create_text(body.get))
      }
      nodes.addChild(snode)
      text = if (tail.isDefined) tail.get else null
    }
//    print("create_text : " ); nodes.children.foreach(child => print(child.toString())); println  ; 2008-10-13
    nodes
  }
}

object SDocFactory extends SDocFactory {
}
