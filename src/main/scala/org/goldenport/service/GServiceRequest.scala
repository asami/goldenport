package org.goldenport.service

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import java.io.File
import org.goldenport.entity.GEntity
import org.goldenport.parameter.GParameterRepository
import com.asamioffice.goldenport.io.UURL

/*
 * derived from ServiceRequestModel.java since Aug. 8, 2006
 *
 * @since   Aug. 29, 2008
 * Jan. 30, 2009
 * Jun. 25, 2011
 * @version Nov.  7, 2011
 * @author  ASAMI, Tomoharu
 */
class GServiceRequest(val call: GServiceCall) {
  private var _serviceName: String = null
  private val _arguments = new ArrayBuffer[AnyRef]
  private var _parameters: GParameterRepository = null
  //
  private var _entity: GEntity = null
  private val _entities = new ArrayBuffer[GEntity]

  final def serviceName: String = {
    require (_serviceName != null)
    _serviceName
  }

  final def setServiceName(aServiceName: String) {
    require (aServiceName != null)
    require (_serviceName == null)
    _serviceName = aServiceName
  }

  final def parameter(aKey: String): Option[Any] = {
    parameters.get(aKey)
  }

  final def parameterAsStrings(aKey: String): List[String] = {
    parameters.get(aKey) match {
      case Some(name) => name.toString.split(":").map(_.trim).toList
      case None       => Nil
    }
  }

  final def parameterBoolean(aKey: String): Option[Boolean] = {
    parameters.get(aKey) match {
      case Some(value) => {
	value.toString.toLowerCase match {
	  case "true" => Some(true)
	  case "false" => Some(false)
	  case _ => sys.error("syntax error")
	}
      }
      case None => None
    }
  }

  final def parameters: GParameterRepository = {
    require (_parameters != null)
    _parameters
  }

  final def setParameters(theParams: GParameterRepository) {
    require (_parameters == null)
    _parameters = theParams
  }

  final def addArguments(theArgs: Seq[AnyRef]) {
    _arguments ++= theArgs
  }

  final def addArgument(anArg: AnyRef) {
    _arguments += anArg
  }

  final def argument(aIndex: Int): AnyRef = {
    _arguments(aIndex)
  }

  final def arguments: Seq[AnyRef] = {
    _arguments
  }

  final def string: String = {
    string(0)
  }

  final def string(nth: Int): String = {
    _arguments(0).toString
  }

  final def strings: Seq[String] = {
    _arguments.map(_.toString)
  }

  final def file: File = {
    file(0)
  }

  final def file(nth: Int): File = {
    try {
      UURL.getFileFromFileNameOrURLName(_arguments(0).toString)
    } catch {
      case e => sys.error("jump usage error")
    }
  }

/*
  final def parameter[R <: Any](key: String): Option[R] = {
    _parameters.get(key).asInstanceOf[Option[R]]
  }
*/

  final def isEntity: Boolean = _entity != null

  final def entity: GEntity = {
    if (_entity == null) {
      sys.error("jump usage error") // XXX
    }
    _entity
  }

  def entityAs[T <: GEntity]: T = {
    entity.asInstanceOf[T]
  }

  def getEntity(): Option[GEntity] = {
    Option(_entity)
  }

  def getEntityAs[T <: GEntity](): Option[T] = {
    Option(_entity.asInstanceOf[T])
  } 

  final def entities: Seq[GEntity] = _entities

  final def setEntity(anEntity: GEntity) {
    _entity = anEntity
  }

  final def setEntities(theEntities: Seq[GEntity]) {
    _entities ++= theEntities
  }
}
