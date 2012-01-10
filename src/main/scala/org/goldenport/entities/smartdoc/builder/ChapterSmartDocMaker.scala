package org.goldenport.entities.smartdoc.builder

import org.goldenport.entity.GEntityContext
import org.goldenport.sdoc._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.attribute._
import org.goldenport.sdoc.structure._
import org.goldenport.value._

/*
 * derived from ChapterSmartDocMaker.java since Mar. 31, 2007
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
class ChapterSmartDocMaker(aContext: GEntityContext) extends SmartDocMaker(aContext) {
/* 2008-10-11
  protected var sdoc_chapter: SSChapter = _
*/

  override var is_Section = false
}
