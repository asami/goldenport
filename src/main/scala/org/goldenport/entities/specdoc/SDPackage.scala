package org.goldenport.entities.specdoc

import scala.collection.mutable
import org.goldenport.value.{GTreeNodeBase, GTable, PlainTable}
import org.goldenport.sdoc.SDoc
import org.goldenport.sdoc.inline.SElementRef
import com.asamioffice.text.UJavaString

/*
 * derived from SDPackageNode.java since Feb. 18, 2007
 *
 * Sep.  4, 2008
 * Oct. 22, 2008
 */
class SDPackage(aName: String) extends SDNode(aName) {
  def this() = this(null)

  override protected def element_Ref: SElementRef = {
    SElementRef(UJavaString.pathname2className(pathname), "index", null)
  }

  final def addEntity(anEntity: SDEntity): SDEntity = {
    addChild(anEntity).asInstanceOf[SDEntity]
  }

  final def addSummary(aSummary: SDSummary): SDSummary = {
    addChild(aSummary).asInstanceOf[SDSummary]
  }

  final def entities: Seq[SDEntity] = {
    children.filter(_.isInstanceOf[SDEntity]).asInstanceOf[Seq[SDEntity]]
  }

  final def entities(category: SDCategory): Seq[SDEntity] = {
    for (child <- children
	 if (child.isInstanceOf[SDEntity] &&
	     child.asInstanceOf[SDEntity].category == category)
       ) yield child.asInstanceOf[SDEntity]
  }

  final def entitiesTable(aCategory: SDCategory): GTable[SDoc] = {
//    println("SDPackage: " + name + ", category: " + aCategory) ; 2008-10-21
    val table = new PlainTable[SDoc]
    table.setHead(aCategory.tableHead)
    for (entity <- entities(aCategory)) {
      table += aCategory.tableRow(entity)
    }
    table
  }

  final def summaries: Seq[SDSummary] = {
    children.filter(_.isInstanceOf[SDSummary]).asInstanceOf[Seq[SDSummary]]
  }
}
