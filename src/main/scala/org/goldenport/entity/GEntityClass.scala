package org.goldenport.entity

import org.goldenport.GClass
import org.goldenport.entity.datasource._
import org.goldenport.entity.locator._
import org.goldenport.entity.content._

/*
 * @since   Aug.  7, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
 abstract class GEntityClass extends GClass {
   type Instance_TYPE <: GEntity

   def accept(aDataSource: GDataSource, aContext: GEntityContext): Boolean = {
     val maySuffix = aDataSource.getSuffix()
     if (maySuffix.isDefined) {
       if (accept_Suffix(maySuffix.get)) return true
       if (!reject_Suffix(maySuffix.get)) return false
     }
//    GMimeType mimetype = aDataSource.getMimeType()
    if (aDataSource.locator match {
      case locator: FileLocator => accept_Locator(locator)
      case locator: ResourceLocator => accept_Locator(locator)
      case locator: URLLocator => accept_Locator(locator)
      case locator: URILocator => accept_Locator(locator)
      case locator: GLocator => accept_Locator(locator)
    }) return true
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

  protected def accept_Suffix(suffix: String): Boolean = false
  protected def reject_Suffix(suffix: String): Boolean = false
  protected def accept_Locator(aLocator: FileLocator): Boolean = false
  protected def accept_Locator(aLocator: ResourceLocator): Boolean = false
  protected def accept_Locator(aLocator: URLLocator): Boolean = false
  protected def accept_Locator(aLocator: URILocator): Boolean = false
  protected def accept_Locator(aLocator: GLocator): Boolean = false
  protected def accept_Content(aContent: GContent): Boolean = false
  protected def accept_DataSource(aDataSource: GDataSource): Boolean = false
  protected def accept_Node(aNode: GContainerEntityNode): Boolean = false

  def reconstitute(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = {
    if (!accept(aDataSource, aContext)) return None
    reconstitute_DataSource(aDataSource, aContext)
  }

  def reconstitute(aNode: GContainerEntityNode, aContext: GEntityContext): Option[Instance_TYPE] = {
    if (!accept(aNode, aContext)) return None
    if (aNode.content != null)
      return reconstitute_DataSource(new ContentDataSource(aNode.content, aContext), aContext)
    val mayDataSource = aNode.getInputDataSource()
    if (mayDataSource.isDefined)
      return reconstitute_DataSource(mayDataSource.get, aContext)
    None
  }

  protected def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = None
}
