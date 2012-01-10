package org.goldenport.entities.specdoc

import scala.collection.mutable
import org.goldenport.value.{GTreeNodeBase, GKey}
import org.goldenport.sdoc.{SDoc, SEmpty}
import com.asamioffice.text.UString

/*
 * derived from SDFeature.java since Feb. 21, 2007
 *
 * Sep.  4, 2008
 * Oct. 18, 2008
 */
class SDFeature(val key: GKey, val value: SDoc) {
  var description: SDoc = SEmpty
  var label: SDoc = UString.capitalize(key.label)

  final def description_is(aDesc: SDoc): SDFeature = {
    description = aDesc
    this
  }

  final def label_is(aLabel: SDoc): SDFeature = {
    label = aLabel
    this
  }
}
