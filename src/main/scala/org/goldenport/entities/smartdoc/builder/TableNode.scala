package org.goldenport.entities.smartdoc.builder

import org.goldenport.value.GTable
import org.goldenport.sdoc._

/*
 * derived from TableNode.java since Sep. 28, 2005
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class TableNode(val table: GTable[SDoc]) extends SBNode {
  var caption: SDoc = SEmpty

  final def caption_is(aCaption: SDoc): TableNode = {
    caption = aCaption
    this
  }
}
