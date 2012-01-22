package org.goldenport.exporter

import org.goldenport.service._
import org.goldenport.entity.GEntity
import org.goldenport.entity.datasource.{GDataSource, NullDataSource}
import org.goldenport.entity.content.GContent

/*
 * derived from DefaultRExporter.java since Aug. 11, 2006
 *
 * @since   Aug. 29, 2008
 * @version Sep. 26, 2010
 * @author  ASAMI, Tomoharu
 */
class DefaultExporter(aCall: GServiceCall) extends GExporter(aCall) {
  override def execute_Export() {
    response.result match {
      case None => ;
      case result: Traversable[_] => {
        result.foreach(r => record_messageC(r.toString))
        record_message()
      }
      case result => record_message(result.toString)
    }
    for (entry <- response.entities) {
      if (entry.datasource == NullDataSource) {
//	println("DefaultExporter entity = " + entry.entity)
        val pathname = if (entry.pathname != "") entry.pathname
        else if (entry.entity.name != "") entry.entity.name
        else sys.error("not implemented yet.")
        output_realm.setEntity(pathname, entry.entity)
      } else {
        write_entity(entry.entity, entry.datasource)
      }
    }
    for (entry <- response.contents) {
      if (entry.datasource == NullDataSource) {
//	println("DefaultExporter content = " + entry.content)
        val pathname = if (entry.pathname != "") entry.pathname
        else sys.error("not implemented yet.")
        output_realm.setContent(pathname, entry.content)
      } else {
        write_content(entry.content, entry.datasource)
      }
    }
    for (realm <- response.realms) {
      output_realm.copyIn(realm)
    }
  }

  private def write_entity(anEntity: GEntity, aDataSource: GDataSource) {
    anEntity.open()
    try {
      anEntity.write(aDataSource)
    } finally {
      anEntity.close()
    }
  }

  private def write_content(aContent: GContent, aDataSource: GDataSource) {
    aContent.open()
    try {
      aContent.write(aDataSource)
    } finally {
      aContent.close()
    }
  }
}
