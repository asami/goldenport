package org.goldenport.dsl.specdoc

import org.goldenport.entities.specdoc._
import org.goldenport.sdoc.SDoc

/*
 * Sep.  5, 2008
 * Sep. 21, 2008
 */
abstract class Entity extends SDEntity {
  set_name(getClass.getName)

  final def feature(aKey: Any, aValue: SDoc, aDesc: SDoc): SDFeature = {
    null // XXX
  }
}
