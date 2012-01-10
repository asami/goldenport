package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.value.GTable

/*
 * Aug.  6, 2008
 * Aug. 16, 2008
 */
abstract class GTableEntity[T](aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) with GTable[T] {
}
