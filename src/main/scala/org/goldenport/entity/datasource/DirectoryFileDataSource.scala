package org.goldenport.entity.datasource

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.FileLocator

/*
 * @since   Aug.  9, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class DirectoryFileDataSource(aDirectory: FileLocator, aContext: GEntityContext) extends FileDataSource(aContext, aDirectory, None) {
  def this(filename: String, aContext: GEntityContext) = this(new FileLocator(filename), aContext)
}
