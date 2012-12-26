package org.goldenport.service

import scala.collection.mutable.ArrayBuffer
import java.util.Date
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
 *  version Nov. 17, 2012
 * @version Dec. 26, 2012
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
  final def parameters = call.request.parameters
  final def request = call.request
  final def response = call.response

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

  protected final def string_value(key: String, other: String = ""): String = {
    string_option(key) getOrElse other
  }

  protected final def string_option(key: String): Option[String] = {
    parameters.getString(key)
  }

  protected final def string_list(key: String): List[String] = {
    parameters.getStrings(key)
  }

  protected final def boolean_value(key: String, other: Boolean = false): Boolean = {
    boolean_option(key) getOrElse other
  }

  protected final def boolean_option(key: String): Option[Boolean] = {
    parameters.getBooleanOption(key)
  }

  protected final def boolean_list(key: String): List[Boolean] = {
    parameters.getBooleans(key)
  }

  protected final def byte_value(key: String, other: Byte = -1): Byte = {
    byte_option(key) getOrElse other
  }

  protected final def byte_option(key: String): Option[Byte] = {
    parameters.getByte(key)
  }

  protected final def byte_list(key: String): List[Byte] = {
    parameters.getBytes(key)
  }

  protected final def short_value(key: String, other: Short = -1): Short = {
    short_option(key) getOrElse other
  }

  protected final def short_option(key: String): Option[Short] = {
    parameters.getShort(key)
  }

  protected final def short_list(key: String): List[Short] = {
    parameters.getShorts(key)
  }

  protected final def int_value(key: String, other: Int = -1): Int = {
    int_option(key) getOrElse other
  }

  protected final def int_option(key: String): Option[Int] = {
    parameters.getInt(key)
  }

  protected final def int_list(key: String): List[Int] = {
    parameters.getInts(key)
  }

  protected final def long_value(key: String, other: Long = -1): Long = {
    long_option(key) getOrElse other
  }

  protected final def long_option(key: String): Option[Long] = {
    parameters.getLong(key)
  }

  protected final def long_list(key: String): List[Long] = {
    parameters.getLongs(key)
  }

  protected final def float_value(key: String, other: Float = Float.NaN): Float = {
    float_option(key) getOrElse other
  }

  protected final def float_option(key: String): Option[Float] = {
    parameters.getFloat(key)
  }

  protected final def float_list(key: String): List[Float] = {
    parameters.getFloats(key)
  }

  protected final def double_value(key: String, other: Double = Double.NaN): Double = {
    double_option(key) getOrElse other
  }

  protected final def double_option(key: String): Option[Double] = {
    parameters.getDouble(key)
  }

  protected final def double_list(key: String): List[Double] = {
    parameters.getDoubles(key)
  }

  protected final def integer_value(key: String, other: BigInt = -1): BigInt = {
    integer_option(key) getOrElse other
  }

  protected final def integer_option(key: String): Option[BigInt] = {
    parameters.getInteger(key)
  }

  protected final def integer_list(key: String): List[BigInt] = {
    parameters.getIntegers(key)
  }

  protected final def decimal_value(key: String, other: BigDecimal = -1): BigDecimal = {
    decimal_option(key) getOrElse other
  }

  protected final def decimal_option(key: String): Option[BigDecimal] = {
    parameters.getDecimal(key)
  }

  protected final def decimal_list(key: String): List[BigDecimal] = {
    parameters.getDecimals(key)
  }

  protected final def date_value(key: String, other: Date = new Date()): Date = {
    date_option(key) getOrElse other
  }

  protected final def date_option(key: String): Option[Date] = {
    parameters.getDate(key)
  }

  protected final def date_list(key: String): List[Date] = {
    parameters.getDates(key)
  }

  protected final def time_value(key: String, other: Date = new Date()): Date = {
    time_option(key) getOrElse other
  }

  protected final def time_option(key: String): Option[Date] = {
    parameters.getTime(key)
  }

  protected final def time_list(key: String): List[Date] = {
    parameters.getTimes(key)
  }

  protected final def datetime_value(key: String, other: Date = new Date()): Date = {
    datetime_option(key) getOrElse other
  }

  protected final def datetime_option(key: String): Option[Date] = {
    parameters.getDateTime(key)
  }

  protected final def datetime_list(key: String): List[Date] = {
    parameters.getDateTimes(key)
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
