package org.goldenport.service

import java.util.concurrent.BlockingQueue
import java.util.concurrent.PriorityBlockingQueue
import java.util.concurrent.TimeUnit

/*
 * Aug. 28, 2008
 * Aug. 28, 2008
 */
class GServiceCallQueue {
  private val service_q = new PriorityBlockingQueue[GServiceCall]()
  private var timeout_service_call: GServiceCall = _
  private var interrupted_exception_service_call: GServiceCall = _

  def enqueue(aCall: GServiceCall) {
    service_q.offer(aCall)
  }

  def setTimeoutServiceCall(aCall: GServiceCall) {
    timeout_service_call = aCall
  }

  def setInterruptedExceptionServiceCall(aCall: GServiceCall) {
    interrupted_exception_service_call = aCall
  }

  def dequeue(): GServiceCall =  {
    try {
      val aCall: GServiceCall = service_q.poll(timeout, timeout_unit)
      if (aCall == null) {
        return timeout_service_call
      }
      return aCall
    } catch {
      case e: InterruptedException => return interrupted_exception_service_call
    }
  }

  private val timeout: Long = 1000
  private val timeout_unit: TimeUnit = TimeUnit.MILLISECONDS
}
