package org.goldenport.entities.zip

import java.util.zip.{ZipFile, ZipEntry}
import org.goldenport.entity._
import org.goldenport.entity.content.GContent

/*
 * derived from ZipEntryContent.java since Apr. 4, 2007
 *
 * Feb.  1, 2009
 * Feb.  1, 2009
 */
class ZipEntryContent(val zipEntry: ZipEntry, val zipFile: ZipFile, aContext: GEntityContext) extends GContent(aContext) {
  override protected def open_InputStream() = {
    zipFile.getInputStream(zipEntry)
  }
}
