package org.goldenport.entities.specdoc

import scala.collection.mutable
import org.goldenport.value.GTreeNodeBase
import org.goldenport.sdoc.{SDoc, SEmpty}

/*
 * Oct. 13, 2008
 * Oct. 21, 2008
 */
abstract class SDCategory(val name: String) {
  def label = name // XXX
  def tableHead: Seq[SDoc]
  def tableRow(anEntity: SDEntity): Seq[SDoc]
}
