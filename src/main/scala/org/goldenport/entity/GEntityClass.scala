package org.goldenport.entity

import org.goldenport.GClass
import org.goldenport.entity.datasource._
import org.goldenport.entity.locator._
import org.goldenport.entity.content._

/*
 * @since   Aug.  7, 2008
 *  version Jul. 15, 2010
 * @version Dec. 17, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GEntityClass extends GClass {
  type Instance_TYPE <: GEntity

  protected var is_accept_only_exists: Boolean = true

  def accept(aDataSource: GDataSource, aContext: GEntityContext): Boolean = {
    if (is_accept_only_exists) {
      for (f <- aDataSource.locator.getFile) {
        if (!f.exists) return false
      }
    }
    val maySuffix = aDataSource.getSuffix()
    if (maySuffix.isDefined) {
      if (accept_Suffix(maySuffix.get)) return true
      if (!reject_Suffix(maySuffix.get)) return false
    }
//    GMimeType mimetype = aDataSource.getMimeType()
    val locatorp = aDataSource.locator match {
      case locator: FileLocator => accept_Locator(locator)
      case locator: ResourceLocator => accept_Locator(locator)
      case locator: URLLocator => accept_Locator(locator)
      case locator: URILocator => accept_Locator(locator)
      case locator: GLocator => accept_Locator(locator)
    }
    locatorp.foreach(return _)
    accept_DataSource(aDataSource)
  }

  def accept(aNode: GContainerEntityNode, aContext: GEntityContext): Boolean = {
    val maySuffix = aNode.suffix
//    println("GEntityClass - node - suffix : " + maySuffix) 2008-10-31
    if (maySuffix.isDefined) {
      if (accept_Suffix(maySuffix.get)) return true
      if (reject_Suffix(maySuffix.get)) return false
    }
//    GMimeType mimetype = aDataSource.getMimeType()
    if (aNode.content != null)
      if (accept_Content(aNode.content)) return true
    accept_Node(aNode)
  }

  def acceptCreate(aDataSource: GDataSource, aContext: GEntityContext): Boolean = false

  protected def accept_Suffix(suffix: String): Boolean = false
  protected def reject_Suffix(suffix: String): Boolean = false
  protected def accept_Locator(aLocator: FileLocator): Option[Boolean] = None
  protected def accept_Locator(aLocator: ResourceLocator): Option[Boolean] = None
  protected def accept_Locator(aLocator: URLLocator): Option[Boolean] = None
  protected def accept_Locator(aLocator: URILocator): Option[Boolean] = None
  protected def accept_Locator(aLocator: GLocator): Option[Boolean] = None
  protected def accept_Content(aContent: GContent): Boolean = false
  protected def accept_DataSource(aDataSource: GDataSource): Boolean = false
  protected def accept_Node(aNode: GContainerEntityNode): Boolean = false

  // XXX Either[String, Instance_TYPE]
  def create(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = {
    if (!acceptCreate(aDataSource, aContext)) return None
    create_DataSource(aDataSource, aContext)
  }

  // XXX Either[String, Instance_TYPE]
  def reconstitute(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = {
    if (!accept(aDataSource, aContext)) return None
    reconstitute_DataSource(aDataSource, aContext)
  }

  // XXX Either[String, Instance_TYPE]
  def reconstitute(aNode: GContainerEntityNode, aContext: GEntityContext): Option[Instance_TYPE] = {
    if (!accept(aNode, aContext)) return None
    if (aNode.content != null)
      return reconstitute_DataSource(new ContentDataSource(aNode.content, aContext), aContext)
    val mayDataSource = aNode.getInputDataSource()
    if (mayDataSource.isDefined)
      return reconstitute_DataSource(mayDataSource.get, aContext)
    None
  }

  // XXX Either[String, Instance_TYPE]
  protected def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = None

  // XXX Either[String, Instance_TYPE]
  protected def create_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = None
}
