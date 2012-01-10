package org.goldenport.sdoc.inline

import scala.collection.mutable.ArrayBuffer
import org.goldenport.sdoc._
import org.goldenport.sdoc.attribute._

/*
 * derived from SInline.java since Sep. 17, 2006
 *
 * @since   Sep.  4, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class SInline(theChildren: SDoc*) extends SDoc(theChildren: _*) {
/*
  final def title: SATitle = {
    val titles: Seq[SATitle] = children.filter(_.isInstanceOf[SATitle]).map(_.asInstanceOf[SATitle])
    if (titles.isEmpty) SATitle()
    else titles(0) // XXX
  }
*/
}
