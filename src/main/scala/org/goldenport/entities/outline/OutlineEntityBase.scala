package org.goldenport.entities.outline

import scala.xml.{Node, Elem, Group}
import scalaz._
import Scalaz._
import java.io.OutputStream
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase
import org.goldenport.value.GTreeNode
import org.goldenport.value.GTreeNodeBase
import org.smartdox._

/*
 * @since   Nov. 30, 2011
 *  version Nov. 30, 2011
 * @version Oct. 19, 2012
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

  def firstThema: OutlineNode = {
    root.getChild(0) match {
      case s: SheetNode => s
      case t => root
    }
  }
}

abstract class OutlineNode(aTitle: Dox) extends GTreeNodeBase[OutlineNode] {
  type TreeNode_TYPE = OutlineNode
  // Title is not name.
  // name is unique id like representation in tree.
  // set_name(aName)
  if (aTitle != null) {
    title = aTitle.toText // XXX: GTreeNode title should be Dox.
  }
  content = this
  val doc: Dox

  override protected def new_Node(aTitle: String): TreeNode_TYPE = {
    TopicNode(aTitle)
  }
}

class RootNode extends OutlineNode(EmptyDox) {
  val doc = EmptyDox 
}

class SheetNode(aTitle: Dox) extends OutlineNode(aTitle) {
  val doc = EmptyDox 
}

class TopicNode(aTitle: Dox, val doc: Dox) extends OutlineNode(aTitle) {
}

object TopicNode {
  def apply(title: String) = {
    new TopicNode(Text(title), EmptyDox)
  }

  def apply(title: Dox, subtopics: Seq[TopicNode], doc: Dox) = {
    new TopicNode(title, doc) {
      subtopics.foreach(addChild)
    }
  }
}

object OutlineNodeShow extends Show[GTreeNode[OutlineNode]] {
  def show(a: GTreeNode[OutlineNode]) = {
    (a.title ?? "").toList
  }
}
