package org.goldenport.exporter

import java.io.File
import org.goldenport.GoldenportConstants
import org.goldenport.service._
import org.goldenport.recorder.GRecordable
import org.goldenport.entity._
import org.goldenport.entities.fs.FileStoreEntity

/**
 * @since   Aug. 29, 2008
 *  version Oct. 16, 2009
 * @version Jan. 23, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class GExporter(val serviceCall: GServiceCall) extends GRecordable with GoldenportConstants {
  private var _output_realm: GTreeContainerEntity = _
  val response = serviceCall.response
  val serviceContext = serviceCall.serviceContext
  val entityContext = serviceCall.entityContext
  setup_Recordable(serviceCall.serviceContext)

  def parameter[R <: Any](aKey: String): R = {
    serviceContext.parameter(aKey)
  }

  def getParameter[R <: Any](aKey: String): Option[R] = {
    serviceContext.getParameter(aKey)
  }

  final protected def output_realm: GTreeContainerEntity = {
    if (_output_realm == null) {
      val filename = parameter(Container_Output_Base).toString
      require (filename != null && filename != "", filename)
      val file = new File(filename)
      record_trace("Exporter file = " + file.getAbsolutePath)
      _output_realm = new FileStoreEntity(file, entityContext)
      _output_realm.open()
    }
    _output_realm
  }

  final def open() {
  }

  final def close() {
    if (_output_realm != null) {
      _output_realm.commit()
      _output_realm.close()
    }
  }

  final def execute(): Unit = {
    open()
    execute_Export()
    close()
  }

  protected def execute_Export(): Unit
}
