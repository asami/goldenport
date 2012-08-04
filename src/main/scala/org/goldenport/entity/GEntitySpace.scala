package org.goldenport.entity

import java.io.File
import java.net.URL
import java.net.URI
import scala.collection.mutable.ArrayBuffer
import org.goldenport.GTreeContainerObject
import org.goldenport.container.GContainerContext
import org.goldenport.parameter.GParameterRepository
import org.goldenport.entity.datasource._
import org.goldenport.entity.content._

import org.goldenport.entity._

/*
 * derived from IRModelSpace.java and AbstractRModelSpace.java
 * since Feb. 6, 2006
 *
 * @since   Aug.  6, 2008
 *  version Sep. 18, 2010
 *  version Feb.  1, 2012
 * @version Aug.  4, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GEntitySpace(val containerContext: GContainerContext, theParams: GParameterRepository) extends GTreeContainerObject {
  val context: GEntityContext = new EntitySpaceEntityContext(containerContext, theParams)
  private val _entityClasses = new ArrayBuffer[GEntityClass]
  private val _dataSourceClasses = new ArrayBuffer[GDataSourceClass]
  def tree: GTreeContainerEntity
  setup_Recordable(containerContext)

  final def entityClasses: Seq[GEntityClass] = _entityClasses
  final def dataSourceClasses: Seq[GDataSourceClass] = _dataSourceClasses

  final def addEntityClass(aClass: GEntityClass) {
    _entityClasses += aClass
  }

  final def addDataSourceClass(aClass: GDataSourceClass) {
    _dataSourceClasses += aClass
  }

  final def reconstitute[R <: GEntity](name: String): Option[R] = {
    reconstitute[R](getDataSource(name))
  }

  final def reconstitute[R <: GEntity](anAny: Any): Option[R] = {
    if (anAny.isInstanceOf[GEntity]) Some(anAny.asInstanceOf[R])
    else reconstitute[R](getDataSource(anAny)) 
  }

  final def reconstitute[R <: GEntity](aDataSource: GDataSource): Option[R] = {
    entityClasses.find(_.accept(aDataSource, context)) match {
      case Some(clazz) => return clazz.reconstitute(aDataSource, context).asInstanceOf[Option[R]]
      case _ => 
    }
    reconstitute_DataSource(aDataSource).asInstanceOf[Option[R]]
  }

  protected def reconstitute_DataSource(aDataSource: GDataSource): Option[GEntity] = None

  final def reconstitute(aNode: GContainerEntityNode): Option[GEntity] = {
    entityClasses.find(_.accept(aNode, context)) match {
      case Some(clazz) => return clazz.reconstitute(aNode, context)
      case _ => 
    }
    reconstitute_Node(aNode)
  }

  protected def reconstitute_Node(aNode: GContainerEntityNode): Option[GEntity] = None

  final def getDataSource(anAny: Any): GDataSource = {
    require (anAny != null)
    anAny match {
      case ds: GDataSource => ds
      case dsc: GDataSourceContent => dsc.inputDataSource
      case c: GContent => new ContentDataSource(c, context)
      case string: String => getDataSource(string)
      case file: File => new FileDataSource(file, context)
      case url: URL => getDataSource(url.toURI)
      case uri: URI => getDataSource(uri)
      case anyref: AnyRef => new AnyRefDataSource(anyref, context)
      case _ => new AnyDataSource(anAny, context)
    }
  }

  final def getDataSource(name: String): GDataSource = {
    require (name != null && name.length > 0)
    getDataSource(new URI(name))
  }

  final def getDataSource(uri: URI): GDataSource = {
    val scheme = uri.getScheme();
    if (scheme == null)
      new FileDataSource(uri.getSchemeSpecificPart(), context)
    else if (scheme.length == 1)
      new FileDataSource(name, context)
    else if (scheme == "file")
      new FileDataSource(uri.getSchemeSpecificPart(), context)
    else if (scheme == "resource")
      new ResourceDataSource(uri.getSchemeSpecificPart(), context)
//    else if (scheme == "jar")
    else if (scheme == "http" || scheme == "https" || scheme == "ftp")
      new URLDataSource(uri.toURL(), context)
    else if (scheme == "tmp")
      new TemporaryDirectoryDataSource(context)
    else if (scheme == "work")
      new WorkFileDataSource(context)
    else
      new URIDataSource(uri, context)
  } ensuring(_ != null)

  final def makeReferenceContent(aName: String): GContent = {
    makeReferenceContent(aName, context)
  }

  final def makeReferenceContent(aName: String, aContext: GEntityContext): GContent = {
    require (aName != null && aName.length > 0)
    val uri = new URI(aName)
    val scheme = uri.getScheme();
    if (scheme == null)
      new FileContent(uri.getSchemeSpecificPart(), aContext)
    else if (scheme.length == 1)
      new FileContent(aName, aContext)
    else if (scheme == "file")
      new FileContent(uri.getSchemeSpecificPart(), aContext)
    else if (scheme == "resource")
      new ResourceContent(uri.getSchemeSpecificPart(), aContext)
//    else if (scheme == "jar")
    else if (scheme == "http" || scheme == "https" || scheme == "ftp")
      new URLContent(uri.toURL(), aContext)
    else if (scheme == "tmp")
      sys.error("no TemporaryDirectoryContent")
//      new TemporaryDirectoryContent(aContext)
    else if (scheme == "work")
      sys.error("no WorkFileContent")
//      new WorkFileContent(aContext)
    else
      new URIContent(uri, aContext)
  } ensuring(_ != null)

  class EntitySpaceEntityContext(aContainerContext: GContainerContext, theParams: GParameterRepository) extends GRootEntityContext(aContainerContext, theParams) {
    final def reconstitute(aNode: GContainerEntityNode): Option[GEntity] = {
      GEntitySpace.this.reconstitute(aNode)
    }

    final def makeReferenceContent(aReference: String): GContent = {
      GEntitySpace.this.makeReferenceContent(aReference, this)
    }

    def formatString(s: String, args: Any*): String = {
      containerContext.formatString(s, args: _*)
    }
  }
}
