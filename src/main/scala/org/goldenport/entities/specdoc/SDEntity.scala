package org.goldenport.entities.specdoc

import scala.collection.mutable
import org.goldenport.value.GTable
import org.goldenport.value.PlainTable
import org.goldenport.sdoc.SDoc
import org.goldenport.sdoc.inline.SElementRef
import com.asamioffice.goldenport.text.UJavaString

/*
 * derived from SDBaseNode.java since Feb. 18, 2007
 *
 * Sep.  4, 2008
 * Oct. 23, 2008
 */
class SDEntity(aName: String) extends SDNode(aName) {
  var category: SDCategory = _

  def this() = this(null)

  override protected def element_Ref: SElementRef = {
    parent match {
      case pkg: SDPackage => new SElementRef(UJavaString.pathname2className(pkg.pathname),
					 name)
      case entity: SDEntity => new SElementRef(UJavaString.pathname2packageName(entity.pathname), entity.name, name)
      case _ => sys.error("not implemented yet.")
    }
  }

  // XXX DSL?
  final def entity(anEntity: SDEntity): SDEntity = addSubEntity(anEntity)

  final def addSubEntity(anEntity: SDEntity): SDEntity = {
    addChild(anEntity).asInstanceOf[SDEntity]
  }

  final def subEntities: Seq[SDEntity] = {
    children.filter(_.isInstanceOf[SDEntity]).asInstanceOf[Seq[SDEntity]]
  }

  final def subEntities(category: SDCategory): Seq[SDEntity] = {
    for (child <- children
	 if (child.isInstanceOf[SDEntity] &&
	     child.asInstanceOf[SDEntity].category == category)
       ) yield child.asInstanceOf[SDEntity]
  }

  final def subEntitiesTable(aCategory: SDCategory): GTable[SDoc] = {
    val table = new PlainTable[SDoc]
    table.setHead(aCategory.tableHead)
    for (entity <- subEntities(aCategory)) {
      table += aCategory.tableRow(entity)
    }
    table
  }
}
