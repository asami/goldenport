package org.goldenport.entities.html

import scala.xml.{Node, Elem}
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.FileDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceNode
import org.goldenport.value.GTreeBase

/*
 * Oct.  6, 2008
 * Oct. 18, 2008
 */
class HtmlRealmEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeContainerEntityBase(aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource
  override type TreeNode_TYPE = GTreeContainerEntityNode
  override def is_Text_Output = true

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
    set_root(new TreeWorkspaceNode(null, this))
  }
}

class HtmlRealmEntityClass extends GEntityClass {
  type Instance_TYPE = HtmlRealmEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "html.d"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new HtmlRealmEntity(aDataSource, aContext))
}
