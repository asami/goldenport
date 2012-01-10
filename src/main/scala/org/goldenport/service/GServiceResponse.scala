package org.goldenport.service

import scala.collection.mutable.ArrayBuffer
import java.io.File
import org.goldenport.entity._
import org.goldenport.entity.datasource._
import org.goldenport.entity.content._
import org.goldenport.entities.workspace.TreeWorkspaceEntity

/*
 * derived from ServiceRequestModel.java since Aug. 8, 2006
 *
 * @since   Aug. 29, 2008
 * Nov.  2, 2008
 * @version Nov. 29, 2011
 * @author  ASAMI, Tomoharu
 */
class GServiceResponse(val call: GServiceCall) {
  private def entity_context: GEntityContext = call.entityContext
  private var _result: Any = _
  private val _entities = new ArrayBuffer[GServiceResponseEntityEntry]
  private val _contents = new ArrayBuffer[GServiceResponseContentEntry]
  private val _realms = new ArrayBuffer[GTreeContainerEntity]

  final def apply(aResult: Any) {
    setResult(aResult)
  }

  final def setResult(aResult: Any) {
    require (aResult != null)
    require (_result == null)
    _result = aResult
  }

  final def result[R >: Any]: R = {
    if (_result == null) None
    else _result.asInstanceOf[R]
  }

  final def resultAnyRef[R >: AnyRef]: R = {
    if (_result == null) None
    else _result.asInstanceOf[R]
  }

  def resultOption[R >: Any]: Option[R] = {
    Option(_result)
  }

  def resultOptionAnyRef[R >: AnyRef]: Option[R] = {
    Option(_result.asInstanceOf[R])
  }
  
  final def addEntity(aEntity: GEntity): GServiceResponseEntityEntry = {
    val entry = new GServiceResponseEntityEntry(aEntity)
    _entities += entry
    entry
  }

  final def addContent(aContent: GContent): GServiceResponseContentEntry = {
    val entry = new GServiceResponseContentEntry(aContent)
    _contents += entry
    entry
  }

  final def addRealm(aRealm: GTreeContainerEntity) {
    val realm = new TreeWorkspaceEntity(call.entityContext)
    realm.open() // XXX timing for close 
    realm.copyIn(aRealm)
    _realms += realm
  }

  final def entities: Seq[GServiceResponseEntityEntry] = _entities

  final def contents: Seq[GServiceResponseContentEntry] = _contents

  final def realms: Seq[GTreeContainerEntity] = _realms

/* 2008-10-27
  final def +=(aFile: File, aEntity: GEntity) {
    addEntity(aFile, aEntity)
  }

  final def addEntity(aFile: File, aEntity: GEntity) {
    addEntity(new FileDataSource(aFile, entity_context), aEntity)
  }

  final def +=(aDataSource: GDataSource, aEntity: GEntity) {
    addEntity(aDataSource, aEntity)
  }

  final def addEntity(aDataSource: GDataSource, aEntity: GEntity) {
    _entities += (aDataSource, aEntity)
  }

  final def entities: Seq[(GDataSource, GEntity)] = _entities

  final def +=(aFile: File, aContent: GContent) {
    addContent(aFile, aContent)
  }

  final def addContent(aFile: File, aContent: GContent) {
    addContent(new FileDataSource(aFile, entity_context), aContent)
  }

  final def +=(aDataSource: GDataSource, aContent: GContent) {
    addContent(aDataSource, aContent)
  }

  final def addContent(aDataSource: GDataSource, aContent: GContent) {
    _contents += (aDataSource, aContent)
  }

  final def contents: Seq[(GDataSource, GContent)] = _contents
*/
  abstract class GServiceResponseEntry {
    type GServiceResponseEntry_TYPE <: GServiceResponseEntry
    var datasource: GDataSource = NullDataSource
    var pathname: String = ""

    def datasource_is(aDataSource: GDataSource): GServiceResponseEntry_TYPE = {
      datasource = aDataSource
      this.asInstanceOf[GServiceResponseEntry_TYPE]
    }

    def file_is(aFile: File): GServiceResponseEntry_TYPE = {
      datasource = new FileDataSource(aFile, entity_context)
      this.asInstanceOf[GServiceResponseEntry_TYPE]
    }

    def pathname_is(aPathname: String): GServiceResponseEntry_TYPE = {
      pathname = aPathname
      this.asInstanceOf[GServiceResponseEntry_TYPE]
    }
  }

  class GServiceResponseEntityEntry(val entity: GEntity) extends GServiceResponseEntry {
    type GServiceResponseEntry_TYPE = GServiceResponseEntityEntry
  }

  class GServiceResponseContentEntry(val content: GContent) extends GServiceResponseEntry {
    type GServiceResponseEntry_TYPE = GServiceResponseContentEntry
  }
}

