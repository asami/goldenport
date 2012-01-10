package org.goldenport.container

import org.goldenport.monitor.DefaultMonitor
import org.goldenport.parameter.NullParameterRepository
import org.goldenport.recorder.MonitorRecorder

/*
 * Nov.  3, 2008
 * Apr.  6, 2009
 */
class DefaultContainerContext extends GContainerContext(new DefaultMonitor, NullParameterRepository) {
  setup_FowardingRecorder(new MonitorRecorder(monitor))
}
