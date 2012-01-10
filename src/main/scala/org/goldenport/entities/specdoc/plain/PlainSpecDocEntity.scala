package org.goldenport.entities.specdoc.plain

import scala.collection.mutable
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.value.GTreeNodeBase
import org.goldenport.entities.specdoc._

/*
 * derived from PlainSpecDocModel.java since Mar. 6, 2007
 *
 * Sep.  4, 2008
 * Sep.  4, 2008
 */
class PlainSpecDocEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends SpecDocEntity(aIn, aOut, aContext) {

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)
}
