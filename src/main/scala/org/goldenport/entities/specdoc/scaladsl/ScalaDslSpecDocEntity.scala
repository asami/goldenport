package org.goldenport.entities.specdoc.scaladsl

import scala.collection.mutable
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.value.GTreeNodeBase
import org.goldenport.entities.specdoc._
import org.goldenport.entities.specdoc.plain._
import org.goldenport.dsl.specdoc.Entity

/*
 * Sep.  5, 2008
 * Sep. 16, 2008
 */
class ScalaDslSpecDocEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends SpecDocEntity(aIn, aOut, aContext) {

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    require (aDataSource.isInstanceOf[EntityDataSource])
    val entityDs = aDataSource.asInstanceOf[EntityDataSource]
    set_root(new PlainRoot)
    entityDs.entities.foreach(build_entity)
  }

  private def build_entity(entity: Entity) {
    val clazz = entity.getClass
    val pkgName = clazz.getPackage.getName
    val pathName = pkgName.replace('.', '/')
    addEntity(pathName, entity)
    println("Entity = " + entity + "," + pathName)
  }
}

class ScalaDslSpecDocEntityClass extends GEntityClass {
  type Instance_TYPE = ScalaDslSpecDocEntity

  override def accept_DataSource(aDataSource: GDataSource): Boolean = {
    aDataSource.isInstanceOf[EntityDataSource]
  }

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new ScalaDslSpecDocEntity(aDataSource, aContext))
}
