package org.goldenport.exporter

import org.goldenport.service._
import org.goldenport.entity.GEntity
import org.goldenport.entity.datasource.{GDataSource, NullDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.GTreeContainerEntity
import javax.crypto.Cipher.r

/*
 * @since   Nov. 14, 2011
 * @version Nov. 19, 2011
 * @author  ASAMI, Tomoharu
 */
class FirstLeafResultExporter(call: GServiceCall) extends GExporter(call) {
  override def execute_Export() {
    for (r <- _find_result(call.response.realms)) {
      call.response.setResult(r)
    }
  }

  private def _find_result(realms: Seq[GTreeContainerEntity]): Option[AnyRef] = {
    val results: Seq[AnyRef] = for {
        realm <- realms
        content <- realm.collect(_.isLeaf)
    } yield content.content.asInstanceOf[AnyRef]
    if (results.isEmpty) None else Some(results.head) 
  }
}

object FirstLeafResultExporterClass extends GExporterClass {
  override protected def accept_Service_Call(aCall: GServiceCall): Option[Boolean] = {
    Some(true)
  }

  override protected def new_Exporter(aCall: GServiceCall): GExporter = {
    new FirstLeafResultExporter(aCall)
  } 
}