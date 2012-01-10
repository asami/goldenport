package org.goldenport

import java.util.UUID
import org.goldenport.recorder.GRecordable

/*
 * Jul. 25, 2008
 * Feb.  4, 2009
 */
abstract class GObject extends GRecordable {
  val uuid: UUID = UUID.randomUUID()
  private var _name: String = ""

  final def name: String = _name
  final def name_=(aName: String) {
    require (aName != null)
    _name = aName
  }
}
