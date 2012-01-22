package org.goldenport.entities.specdoc

import scala.collection.mutable
import org.goldenport.value.{GTreeNodeBase, GTable, PlainTable}
import org.goldenport.sdoc.SDoc
import org.goldenport.sdoc.inline.SElementRef
import com.asamioffice.goldenport.text.UJavaString

/*
 * Oct. 21, 2008
 * Oct. 23, 2008
 */
class SDSummary(aName: String) extends SDNode(aName) {
  var tableHead: Seq[SDoc] = _
  var tableRow: SDEntity => Seq[SDoc] = _

  override protected def element_Ref: SElementRef = {
    parent match {
      case pkg: SDPackage => SElementRef(UJavaString.pathname2className(pkg.pathname),
					 name, null)
      case _ => sys.error("not implemented yet.")
    }
  }

  final def summaryTable(entities: Seq[SDEntity]): GTable[SDoc] = {
    val table = new PlainTable[SDoc]
    table.setHead(tableHead)
    for (entity <- entities) {
      table += tableRow(entity)
    }
    table
  }
}
