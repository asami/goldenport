package org.goldenport.entities.yaml

import scala.xml.{Node, Elem, Group}
import java.io.OutputStream
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase

/**
 * @since   Nov. 29, 2011
 * @version Nov. 29, 2011
 * @author  ASAMI, Tomoharu
 */
class YamlEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeEntityBase[YamlNode](aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource
  override type TreeNode_TYPE = YamlNode

  val xmindContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
    set_root(new RootNode)
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
    set_root(new RootNode)
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    set_root(new RootNode)
    load_datasource(aDataSource)
  }

  private def load_datasource(aDataSource: GDataSource) {
    try {
    } finally {
    }
  }    

  private def add_sheet(aSheet: Node) {
//    println("shhet = " + aSheet)
    val id = (aSheet \ "@id").text
    val sheet = new SheetNode(id)
    sheet.title = (aSheet \ "title").text
    root.addChild(sheet)
    for (child <- aSheet \ "topic") {
      add_topic(child, sheet)
    }
  }

  private def add_topic(aTopic: Node, aParent: YamlNode) {
    val id = (aTopic \ "@id").text
    val topic = new TopicNode(id)
    topic.title = (aTopic \ "title").text
    aParent.addChild(topic)
    for (child <- aTopic \ "children" \ "topics" \ "topic") {
      add_topic(child, topic)
    }
  }

  override protected def write_Content(anOut: OutputStream): Unit = {
  }
}

class YamlEntityClass extends GEntityClass {
  type Instance_TYPE = YamlEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "yaml"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new YamlEntity(aDataSource, aContext))
}

object YamlEntity extends YamlEntityClass
