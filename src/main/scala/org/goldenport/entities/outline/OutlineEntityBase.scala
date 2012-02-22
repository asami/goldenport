package org.goldenport.entities.outline

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
import org.goldenport.value.GTreeNodeBase
import java.util.UUID

/*
 * @since   Nov. 30, 2011
 *  version Nov. 30, 2011
 * @version Feb. 22, 2012
 * @Author  ASAMI, Tomoharu
 */
abstract class OutlineEntityBase(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeEntityBase[OutlineNode](aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource
  override type TreeNode_TYPE = OutlineNode

  val outlineContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
    set_root(new RootNode)
    for (ds <- template_DataSource) {
      load_Datasource(ds)
    }
  }

  protected def template_DataSource: Option[GDataSource] = None
  
  override protected def open_Entity_Create(aDataSource: GDataSource) {
    set_root(new RootNode)
    for (ds <- template_DataSource) {
      load_Datasource(ds)
    }
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    set_root(new RootNode)
    load_Datasource(aDataSource)
  }

  protected def load_Datasource(aDataSource: GDataSource)

  final def firstThema: TopicNode = {
    root.getChild(0) match {
      case s: SheetNode => s.getChild(0).asInstanceOf[TopicNode]
      case t => t.asInstanceOf[TopicNode]
    }
  }
}

abstract class OutlineNode(aName: String) extends GTreeNodeBase[OutlineNode] {
  type TreeNode_TYPE = OutlineNode
  set_name(aName)
  content = this

  override protected def new_Node(aName: String): TreeNode_TYPE = {
    new TopicNode(aName)
  }
}

class RootNode extends OutlineNode(null) {
}

class SheetNode(aName: String) extends OutlineNode(aName) {
}

class TopicNode(aName: String) extends OutlineNode(aName) {
}
