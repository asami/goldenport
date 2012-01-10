package org.goldenport.sdoc.block

import scala.collection.mutable.ArrayBuffer
import org.goldenport.sdoc.SDoc
import org.goldenport.sdoc.attribute._

/*
 * derived from SBlock.java since Sep. 17, 2006
 *
 * @since   Sep.  6, 2008
 * @version Oct. 18, 2008
 * @author  ASAMI, Tomoharu
 */
abstract class SBlock(theChildren: SDoc*) extends SDoc(theChildren: _*) {
}
