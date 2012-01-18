package org.goldenport.value

import com.asamioffice.goldenport.text.UString

/*
 * Oct. 13, 2008
 * Oct. 13, 2008
 */
class GKey(val name: String) {
  private var _label: String = null

  final def label: String = {
    if (_label != null) _label
    else UString.capitalize(name)
  }

  protected final def set_label(label: String) {
    _label = label
  }
}
