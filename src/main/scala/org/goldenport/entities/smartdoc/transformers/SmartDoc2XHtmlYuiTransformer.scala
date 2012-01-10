package org.goldenport.entities.smartdoc.transformers

import org.goldenport.value.GTreeNode
import org.goldenport.value.GTreeVisitor
import org.goldenport.sdoc._
import org.goldenport.sdoc.attribute._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.structure._
import org.goldenport.entities.smartdoc._

/*
 * @since   Sep. 26, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class SmartDoc2XHtmlYuiTransformer(aSdoc: SDoc) extends SmartDoc2HtmlStringTransformer(aSdoc) {
  def this(aSmartDoc: SmartDocEntity) = this(aSmartDoc.root)
}
