package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.{GTable, PlainTable}
import org.goldenport.sdoc._

/*
 * Jan. 16, 2009
 * Jan. 16, 2009
 */
case class SBFigure extends SBlock {
  var src: String = ""
}

object SBFigureXml {
  def unapply(xnode: Node): Option[SBFigure] = {
    if (xnode.label == "figure") {
      val figure = SBFigure()
      figure.putXmlAttributes(xnode.attributes)
      Some(figure)
    } else None
  }
}

object SBFigureString {
  def unapply(string: String): Option[(SBFigure, Option[String], Option[String])] = None
}
