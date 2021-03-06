package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.TemporaryDirectoryLocator

/*
 * @since   Aug.  9, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class TemporaryDirectoryDataSource(aLocator: TemporaryDirectoryLocator, aContext: GEntityContext) extends GDataSource(aContext, aLocator) with GContainerDataSource {

  def this(aContext: GEntityContext) = this(new TemporaryDirectoryLocator(), aContext)
}
