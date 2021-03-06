package org.goldenport.entity

import org.goldenport.entity.datasource.{GDataSource, NullDataSource}

/**
 * @since   Jun. 12, 2012
 * @version Jun. 19, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GListContainerEntity[E](aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
}
