package org.goldenport.activity

/*
 * Aug. 29, 2008
 * Aug. 29, 2008
 */
trait GActivityElement {
  private var activity_context: GActivityContext = _

  final def context: GActivityContext = activity_context

  final def setContext(aContext: GActivityContext) = {
    activity_context = aContext
  }
}
