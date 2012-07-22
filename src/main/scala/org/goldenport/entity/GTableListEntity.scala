package org.goldenport.entity

import org.goldenport.value.GTable
import org.goldenport.entity.datasource.GDataSource

/**
 * @since   Jun. 12, 2012
 *  version Jun. 19, 2012
 * @version Jul. 21, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GTableListEntity[E <: GTable[_]](aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GListContainerEntity(aIn, aOut, aContext) {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)

  def head: E
}
