package org.goldenport.entities.smartdoc.builder

import org.goldenport.entity.GEntityContext
import org.goldenport.sdoc._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.structure._
import org.goldenport.sdoc.attribute._
import org.goldenport.value._
import org.goldenport.entities.smartdoc._

/*
 * @since   Oct.  7, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
class SmartDocRealmMaker(val context: GEntityContext) extends GTreeVisitor[SBNode] {
  val realm: SmartDocRealmEntity = new SmartDocRealmEntity(context)
  realm.open()
  private var current = realm.root

  override def enter(aNode: GTreeNode[SBNode]) {
    aNode.content match {
      case topic: TopicNode => enter_topic(topic, aNode)
      case page: PageNode => enter_page(page, aNode)
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

  private def enter_topic(aTopic: TopicNode, aNode: GTreeNode[SBNode]) {
    val child = current.setChild(aTopic.name)
    val index = make_index(aTopic, aNode)
    child.setNode("index").content = index.toContent
    current = child
  }

  // XXX if table is not exists, use default layout
  private def make_index(aTopic: TopicNode, aNode: GTreeNode[SBNode]): SmartDocEntity = {
    val indexBuilder = new SmartDocBuilder(context)
    val cursor = indexBuilder.getCursor

    def enter_index_topic(aTopic: TopicNode, aNode: GTreeNode[SBNode]) {
      // do nothing
    }

    def leave_index_topic(topic: TopicNode) {
      // do nothing
    }

    def enter_index_page(aPage: PageNode, aNode: GTreeNode[SBNode]) {
      // do nothing
    }

    def leave_index_page(page: PageNode) {
      // do nothing
    }

    def enter_index_division(div: DivisionNode) {
      // do nothing
    }

    def leave_index_division(div: DivisionNode) {
      // do nothing
    }

    def enter_index_description(aDesc: DescriptionNode) {
      cursor.addDescription(aDesc.description)
    }

    def leave_index_description(desc: DescriptionNode) {
      // do nothing
    }

    def enter_index_table(aTable: TableNode) {
      cursor.addTable(aTable.table) caption_is aTable.caption
    }

    def leave_index_table(table: TableNode) {
      // do nothing
    }

    indexBuilder.name = "index"
    indexBuilder.title = aTopic.title
    for (child <- aNode.children) {
      child.content match {
	case topic: TopicNode => enter_index_topic(topic, aNode)
	case page: PageNode => enter_index_page(page, aNode)
	case div: DivisionNode => enter_index_division(div)
	case desc: DescriptionNode => enter_index_description(desc)
	case table: TableNode => enter_index_table(table)
	case _ => sys.error("not implemented yet:" + child + "/" + child.content)
      }
    }
    indexBuilder.make
  }

  private def leave_topic(topic: TopicNode) {
    current = current.parent
  }

  private def enter_page(aPage: PageNode, aNode: GTreeNode[SBNode]) {
    val child = current.setChild(aPage.name)
    val pageMaker = new SectionSmartDocMaker(context)
    pageMaker.setName(aPage.name)
    pageMaker.setTitle(aPage.title)
    aNode.traverse(pageMaker)
    val page = pageMaker.doc
    child.setEntity(page)
    done_traverse(aNode)
  }

  private def leave_page(page: PageNode) {
    // do nothing
  }

  private def enter_division(div: DivisionNode) {
    // do nothing
  }

  private def leave_division(div: DivisionNode) {
    // do nothing
  }

  private def enter_description(aDesc: DescriptionNode) {
    // do nothing
  }

  private def leave_description(desc: DescriptionNode) {
    // do nothing
  }

  private def enter_table(aTable: TableNode) {
    // do nothing
  }

  private def leave_table(table: TableNode) {
    // do nothing
  }
}
