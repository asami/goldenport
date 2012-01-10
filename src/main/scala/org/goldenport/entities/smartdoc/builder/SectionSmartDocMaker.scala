package org.goldenport.entities.smartdoc.builder

import org.goldenport.entity.GEntityContext
import org.goldenport.sdoc._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.structure._
import org.goldenport.sdoc.attribute._
import org.goldenport.value._

/*
 * derived from SectionSmartDocMaker.java since Mar. 31, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
class SectionSmartDocMaker(aContext: GEntityContext) extends SmartDocMaker(aContext) {
  override var is_Section = true
}
