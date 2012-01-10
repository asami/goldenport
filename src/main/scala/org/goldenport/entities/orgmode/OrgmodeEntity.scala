package org.goldenport.entities.orgmode

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
import org.goldenport.entities.outline._

/**
 * @since   Sep. 15, 2010 (in g3)
 * @since   Nov. 29, 2011
 * @version Nov. 29, 2011
 * @author  ASAMI, Tomoharu
 */
class OrgmodeEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends OutlineEntityBase(aIn, aOut, aContext) {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  protected override def load_Datasource(aDataSource: GDataSource) {
  }    

  override protected def write_Content(anOut: OutputStream): Unit = {
  }
}

class OrgmodeEntityClass extends GEntityClass {
  type Instance_TYPE = OrgmodeEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "org"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new OrgmodeEntity(aDataSource, aContext))
}

object OrgmodeEntity extends OrgmodeEntityClass
