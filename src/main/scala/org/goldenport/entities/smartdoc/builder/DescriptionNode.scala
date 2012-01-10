package org.goldenport.entities.smartdoc.builder

import org.goldenport.sdoc._

/*
 * derived from ParagraphNode.java since Sep. 28, 2005
 *
 * Sep.  6, 2008
 * Oct. 18, 2008
 */
case class DescriptionNode(val description: SDoc) extends SBNode {
  require(description != null)
}
