package org.goldenport.application

import scala.collection.mutable.ArrayBuffer
import org.goldenport.service.GServiceClass
import org.goldenport.entity.GEntityClass
import org.goldenport.importer.GImporterClass
import org.goldenport.exporter.GExporterClass

/*
 * Oct. 31, 2008
 * Feb.  7, 2009
 */
class GApplicationDescriptor {
  private var _classpath = new ArrayBuffer[String]
  private var _services = new ArrayBuffer[GServiceClass]
  private var _entities = new ArrayBuffer[GEntityClass]
  private var _importers = new ArrayBuffer[GImporterClass]
  private var _exporters = new ArrayBuffer[GExporterClass]

  var name: String = null
  var version: String = null
  var version_build: String = null
  var copyright_years: String = null
  var copyright_owner: String = null
  var command_name: String = null

  final def classpath(paths: String*) {
    _classpath ++= paths
  }

  final def services(services: GServiceClass*) {
    _services ++= services
  }

  final def entities(entities: GEntityClass*) {
    _entities ++= entities
  }

  final def importers(importers: GImporterClass*) {
    _importers ++= importers
  }

  final def exporters(exporters: GExporterClass*) {
    _exporters ++= exporters
  }

  final def classpath: Seq[String] = _classpath
  final def services: Seq[GServiceClass] = _services
  final def entities: Seq[GEntityClass] = _entities
  final def importers: Seq[GImporterClass] = _importers
  final def exporters: Seq[GExporterClass] = _exporters
}

object NullApplicationDescriptor extends GApplicationDescriptor {
  name = "Null"
  version = "1.0"
  version_build = "20081031"
  copyright_years = "2008"
  copyright_owner = "ASAMI, Tomoharu"
  command_name = "null"
}
