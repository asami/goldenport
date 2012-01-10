package org.goldenport.entities.specdoc

import scala.collection.mutable.{Buffer, ArrayBuffer}
import scala.xml.Elem
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.value._
import org.goldenport.entities.specdoc.plain._
import org.goldenport.sdoc.{SDoc, SEmpty, SText}
import com.asamioffice.text.UJavaString

/*
 * derived from SpecDocModel since Feb. 17, 2007
 *
 * @since   Sep.  4, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
class SpecDocEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeEntityBase[SDNode](aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource
  override type TreeNode_TYPE = SDNode
  override def is_Text_Output = true
  var title: SDoc = SEmpty

//  val packageCategories: Buffer[SDCategory] = new ArrayBuffer[SDCategory]
//  val entityCategories: Buffer[SDCategory] = new ArrayBuffer[SDCategory]

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
    set_root(new PlainRoot)
  }

  final def addEntity(aPackageName: String, aTitle: String): SDEntity = {
    var entity = new_Entity
    if (entity == null) {
      entity = new PlainEntity
      entity.title = aTitle
    }
    addEntity(aPackageName, entity)
  }

  protected def new_Entity: SDEntity = {
    return new PlainEntity
  }

  final def addEntity(aPackageName: String, anEntity: SDEntity): SDEntity = {
    val pkg = setPackage(aPackageName)
    pkg.addEntity(anEntity)
  }

  final def setPackage(aPackageName: String): SDPackage = { // XXX acquire, lookup, obtain?
    val mayPkg = getNode(aPackageName)
    if (mayPkg.isDefined) {
      mayPkg.get.asInstanceOf[SDPackage] // XXX
    } else {
      val pkg = setNode(aPackageName).asInstanceOf[SDPackage]
      pkg.title = SText(UJavaString.pathname2className(aPackageName))
      pkg
    }
  }

  final def entityPackages: Seq[SDPackage] = {
    val collector = new PackageCollector
    traverse(collector)
    collector.result
  }

  private class PackageCollector extends GTreeVisitor[SDNode] {
    val pkgs = new ArrayBuffer[SDPackage]
    override def startEnter(aNode: GTreeNode[SDNode]) {
      if (!aNode.isInstanceOf[SDPackage]) return
      if (aNode.children.exists(_.isInstanceOf[SDEntity])) {
	pkgs += aNode.asInstanceOf[SDPackage]
      }
    }
    def result: Seq[SDPackage] = pkgs
  }
}

class SpecDocEntityClass extends GEntityClass {
  type Instance_TYPE = SpecDocEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "spec"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new PlainSpecDocEntity(aDataSource, aContext))
}
