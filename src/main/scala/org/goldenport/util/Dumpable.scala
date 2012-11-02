package org.goldenport.util

import org.goldenport.recorder.GRecordable

/*
 * @since   Nov.  1, 2012
 * @version Nov.  2, 2012
 * @author  ASAMI, Tomoharu
 */
trait Dumpable {
  def dumpString(): String
  def dump(): Unit = {
    println(dumpString)
  }
}

trait DumpLoggerable {
  self: Dumpable with GRecordable =>

  def dumpDebug(): Unit = {
    record_debug(dumpString)
  }

  def dumpTrace(): Unit = {
    record_trace(dumpString)
  }
}
