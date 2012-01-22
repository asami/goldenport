package org.goldenport.entities.smartdoc.builder

import scala.collection.mutable.Stack
import org.goldenport.entity.GEntityContext
import org.goldenport.sdoc._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.structure._
import org.goldenport.sdoc.attribute._
import org.goldenport.value._
import org.goldenport.entities.smartdoc.SmartDocEntity

/*
 * derived from SmartDocMaker.java since Sep. 29, 2005
 *
 * @since   Sep.  6, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class SmartDocMaker(val context: GEntityContext) extends GTreeVisitor[SBNode] {
  val doc: SmartDocEntity = new SmartDocEntity(context)
  doc.open()
  private val stack = new Stack[SStructure]
  stack.push(doc.body)
  protected final def current = stack.top

//  final def title_=(aTitle: SDoc*) = {
//    doc.head.title = aTitle: _*
//  }

  final def setName(aName: String) = {
    doc.name = aName
  }

  final def setTitle(aTitle: SDoc*) = {
    doc.head.title = aTitle: _*
  }

  override def enter(aNode: GTreeNode[SBNode]) {
    aNode.content match {
      case topic: TopicNode => enter_topic(topic)
      case page: PageNode => enter_page(page)
      case div: DivisionNode => enter_division(div)
      case desc: DescriptionNode => enter_description(desc)
      case table: TableNode => enter_table(table)
      case node => sys.error("not implemented yet:" + node)
    }
  }

  override def leave(aNode: GTreeNode[SBNode]) {
    aNode.content match {
      case topic: TopicNode => leave_topic(topic)
      case page: PageNode => leave_page(page)
      case div: DivisionNode => leave_division(div)
      case desc: DescriptionNode => leave_description(desc)
      case table: TableNode => leave_table(table)
      case node => sys.error("not implemented yet:" + node)
    }
  }

  private def enter_topic(div: TopicNode) {
    sys.error("topic")
  }

  private def leave_topic(div: TopicNode) {
    sys.error("topic")
  }

  private def enter_page(div: PageNode) {
    sys.error("page")
  }

  private def leave_page(div: PageNode) {
    sys.error("page")
  }

  private def enter_division(div: DivisionNode) {
    val child = current match {
      case _: SSBody => if (is_Section) SSSection() else SSChapter()
      case _: SSChapter => SSSection()
      case _: SSSection => SSSubSection()
      case _: SSSubSection => SSSubSubSection()
      case _: SSSubSubSection => sys.error("too deep : " + current)
      case _ => sys.error("illegal structure node : " + current)
    }
    child.title = div.title.deepCopy
    current.addChild(child)
    stack.push(child)
  }

  protected var is_Section: Boolean

  private def leave_division(div: DivisionNode) {
    stack.pop
  }

  private def enter_description(aDesc: DescriptionNode) {
    current.addChildOrChildren(import_sdoc(aDesc.description))
  }

  private def leave_description(desc: DescriptionNode) {
    // do nothing
  }

  private def enter_table(aTable: TableNode) {
    val table = SBTable()
    table.title = aTable.caption
    import_table(aTable.table, table.table)
    current.addChild(table)
  }

  private def import_table(aSrc: GTable[SDoc], aDest: GTable[SDoc]) {
    if (aSrc.head.isDefined) {
      val head = new PlainTable[SDoc]
      import_tabular(aSrc.head.get, head)
      aDest.setHead(head)
    }
    if (aSrc.side.isDefined) {
      sys.error("not implemented yet.")
    }
    import_tabular(aSrc, aDest)
  }

  private def import_tabular(aSrc: GTabular[SDoc], aDest: GTabular[SDoc]) {
    var width = aSrc.width
    var height = aSrc.height
    for (y <- 0 until height; x <- 0 until width) {
      aDest.put(x, y, import_sdoc(aSrc.get(x, y)))
    }
  }

  private def import_sdoc(aSdoc: SDoc): SDoc = aSdoc

/*
  private def import_sdoc(aCell: SDoc): SDoc = {
    val cell = aCell.deepCopy
    adjust_anchor(cell)
    cell
  }

  private def adjust_anchor(aCell: SDoc) {
    aCell match {
      case anchor: SIAnchor => {
	if (anchor.href == null) {
	  if (anchor.unresolvedRef != null) {
	    anchor.href = resolve_ref(anchor.unresolvedRef)
	  }
	}
      }
      case _ => // do nothing
    }
    aCell.children.foreach(adjust_anchor)
  }

  private def resolve_ref(aRef: SUnresolvedRef): String = {
    aRef match {
      case obj: SObjectRef => obj.name
      case help: SHelpRef => "help:" + help.name + help.params.mkString // XXX
    }
  }
*/

  private def leave_table(table: TableNode) {
    // do nothing
  }
}
