package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource

/*
 * Aug.  5, 2008
 * Oct.  7, 2008
 */
abstract class GContainerEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) {
  final def mountAll() = null
}
