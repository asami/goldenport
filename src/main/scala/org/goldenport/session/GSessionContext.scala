package org.goldenport.session

import org.goldenport.container.GContainerContext
import org.goldenport.service._
import org.goldenport.entity._
import org.goldenport.recorder.ForwardingRecorder

/*
 * Aug. 28, 2008
 * Apr.  1, 2009
 */
abstract class GSessionContext(val containerContext: GContainerContext) extends ForwardingRecorder {
  def serviceSpace: GServiceSpace
  def entitySpace: GEntitySpace
  final def serviceCallEngine = serviceSpace.serviceCallEngine

  setup_FowardingRecorder(containerContext, null) // XXX null
}
