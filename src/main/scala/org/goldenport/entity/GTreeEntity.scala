package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.value.GTree

/*
 * Jul. 25, 2008
 * Sep.  2, 2008
 */
abstract class GTreeEntity[E](aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) with GTree[E] {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
}
