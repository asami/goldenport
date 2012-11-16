package org.goldenport.service

import scala.collection.mutable.ArrayBuffer
import org.goldenport.Goldenport
import org.goldenport.GObject
import org.goldenport.unitofwork._
import org.goldenport.activity._
import org.goldenport.importer.GImporterClass
import org.goldenport.exporter.GExporterClass

/*
 * Service instance which binds with ServiceCall and UnitofWork.
 *
 * @since   Aug. 28, 2008
 *  version Nov.  4, 2008
 * @version Nov. 17, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GService(val call: GServiceCall, val serviceClass: GServiceClass) extends GObject {
  private val _importers = new ArrayBuffer[GImporterClass]
  private val _exporters = new ArrayBuffer[GExporterClass]
  private val _blueprints = new ArrayBuffer[GEpisode]
  private val _promoters = new ArrayBuffer[GEpisode]

  final def importers: Seq[GImporterClass] = _importers
  final def exporters: Seq[GExporterClass] = _exporters
  final def blueprints: Seq[GEpisode] = _blueprints
  final def promoters: Seq[GEpisode] = _promoters

  final def serviceContext = call.serviceContext
  final def entitySpace = call.entitySpace
  final def entityContext = call.entityContext

  call.setup(this)
  name = serviceClass.name
  setup_Recordable(serviceContext)

  def isSystemService: Boolean = {
    getClass.getPackage().getName().startsWith("org.goldenport.service")
  }

  final def execute() {
    execute_prologue()
    try {
      execute_service()
    } finally {
      execute_epilogue()
    }
  }

  private def execute_prologue() {
  }

  private def execute_epilogue() {
  }

  private def execute_service() {
    execute_Service(call.request, call.response)
  }

  protected def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse)

  protected final def parameter[R <: Any](aKey: String): R = {
    serviceContext.parameter[R](aKey)
  }

  //
  protected final def application_copyright_years: String = {
    parameter[String](Goldenport.Application_Copyright_Years)
  }

  protected final def application_copyright_owner: String = {
    parameter[String](Goldenport.Application_Copyright_Owner)
  }

  protected final def application_name: String = {
    parameter[String](Goldenport.Application_Name)
  }

  protected final def application_version: String = {
    parameter[String](Goldenport.Application_Version)
  }

  protected final def application_version_build: String = {
    parameter[String](Goldenport.Application_Version_Build)
  }

  protected final def application_commnad_name: String = {
    parameter[String](Goldenport.Application_Command_Name)
  }
}
