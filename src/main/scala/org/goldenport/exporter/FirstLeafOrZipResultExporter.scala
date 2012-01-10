package org.goldenport.exporter

import scalaz._
import Scalaz._
import org.goldenport.service._
import org.goldenport.entity.GEntity
import org.goldenport.entity.datasource.{GDataSource, NullDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.GTreeContainerEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.entity.content.BinaryContent
import org.goldenport.Strings

/*
 * @since   Nov. 30, 2011
 * @version Dec. 14, 2011
 * @author  ASAMI, Tomoharu
 */
class FirstLeafOrZipResultExporter(call: GServiceCall) extends GExporter(call) {
  override def execute_Export() {
    for (r <- _first_leaf orElse _zip) {
      call.response.setResult(r)
    }
  }

  private def _first_leaf: Option[AnyRef] = {
    if (call.response.realms.length == 1) {
      call.response.realms(0).root.children match {
        case children if children.length == 1 && children.head.isLeaf => Some(children.head.content)
        case _ => None
      }
    } else {
      None
    }
  }

  private def _zip: Option[AnyRef] = {
    if (call.response.realms.isEmpty) {
      None
    } else {
//      val filename = Option(call.response.realms.head.name) | "files.zip"  
      val filename = "files.zip"  
      val zip = new ZipEntity(call.entityContext)
      zip.open()
      try {
        for (realm <- call.response.realms) {
          zip.copyIn(realm)
        }
        Some(zip.getBinaryContent(filename, Strings.mimetype.application_octet_stream))
      } finally {
        zip.close()
      }
    }
  }
}

object FirstLeafOrZipResultExporterClass extends GExporterClass {
  override protected def accept_Service_Call(aCall: GServiceCall): Option[Boolean] = {
    Some(true)
  }

  override protected def new_Exporter(aCall: GServiceCall): GExporter = {
    new FirstLeafOrZipResultExporter(aCall)
  } 
}