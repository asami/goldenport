package org.goldenport.entities.db

import java.io._
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.GContentDataSource
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTableBase
import com.asamioffice.goldenport.text.ReaderCsvTableMaker;

/**
 * RowSetEntity?
 * 
 * @since   Jun. 12, 2012
 * @version Jun. 19, 2012
 * @author  ASAMI, Tomoharu
 */
class JdbcEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTableEntity[AnyRef](aIn, aOut, aContext) with GTableBase[AnyRef] {
  type DataSource_TYPE = GDataSource

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override def is_Special_Commit = true
  override def table_Update() = set_dirty()

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  override protected def open_Entity_Create() {
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
  }
}

class JdbcEntityClass extends GEntityClass {
  type Instance_TYPE = JdbcEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "jdbc"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new JdbcEntity(aDataSource, aContext))
}

object JdbcEntity extends JdbcEntityClass
