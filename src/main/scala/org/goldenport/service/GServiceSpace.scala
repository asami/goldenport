package org.goldenport.service

import scala.collection.mutable.ArrayBuffer
import org.goldenport.recorder._
import org.goldenport.container.GContainerContext
import org.goldenport.parameter._
import org.goldenport.unitofwork._
import org.goldenport.activity._
import org.goldenport.importer._
import org.goldenport.exporter._

/*
 * derived from RServiceSpace.java since Mar. 20, 2006
 *
 * Aug. 28, 2008
 * Nov.  4, 2008
 */
class GServiceSpace(val containerContext: GContainerContext, val parameters: GParameterRepository) {
  val context = new ServiceSpaceServiceContext(containerContext, parameters)
  private val _service_classes = new ArrayBuffer[GServiceClass]
  private val _importer_classes = new ArrayBuffer[GImporterClass]
  private val _exporter_classes = new ArrayBuffer[GExporterClass]
  val serviceCallEngine = new GServiceCallEngine(this)

  def serviceClasses: Seq[GServiceClass] = _service_classes
  def importerClasses: Seq[GImporterClass] = _importer_classes
  def exporterClasses: Seq[GExporterClass] = _exporter_classes

  final def addServiceClass(aClass: GServiceClass) {
    _service_classes += aClass
  }

  final def addImporterClass(aClass: GImporterClass) {
    _importer_classes += aClass
  }

  final def addExporterClass(aClass: GExporterClass) {
    _exporter_classes += aClass
  }

  final def createServiceContext(aUnitofwork: GUnitofwork): GServiceContext = {
    new UnitofworkServiceContext(aUnitofwork, context, parameters)
  }

  final def getBlueprints(aCall: GServiceCall): Seq[GEpisode] = {
    aCall.service.blueprints ++ service_blueprints(aCall)
  }

  private def service_blueprints(aCall: GServiceCall): Seq[GEpisode] = {
    Nil
  }

  final def getPromoters(aCall: GServiceCall): Seq[GEpisode] = {
    aCall.service.promoters ++ service_promoters(aCall)
  }

  private def service_promoters(aCall: GServiceCall): Seq[GEpisode] = {
    Nil
  }

  final def getImporters(aCall: GServiceCall): Seq[GImporter] = {
    val classes = _importer_classes.filter(_.accept(aCall))
    if (classes.isEmpty) new DefaultImporter(aCall) :: Nil
    else classes.map(_.newImporter(aCall))
  }

  final def getExporters(aCall: GServiceCall): Seq[GExporter] = {
    val classes = _exporter_classes.filter(_.accept(aCall))
    if (classes.isEmpty) new DefaultExporter(aCall) :: Nil
    else classes.map(_.newExporter(aCall))
  }

  final def getService(aCall: GServiceCall): Option[GService] = {
    val mayServiceClass = aCall.getSpecificServiceClass()
    if (mayServiceClass.isDefined)
      return mayServiceClass.get.newServiceOption(aCall)
    serviceClasses.find(_.accept(aCall)) match {
      case Some(clazz) => clazz.newServiceOption(aCall)
      case None => None
    }
  }

  class ServiceSpaceServiceContext(aContainerContext: GContainerContext, theParams: GParameterRepository) extends GServiceContext(this, aContainerContext, theParams) {
  }
}
