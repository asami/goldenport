package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.WorkFileLocator

/*
 * @since   Aug.  9, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class WorkFileDataSource(aUri: WorkFileLocator, aContext: GEntityContext) extends GDataSource(aUri, aContext) with GContentDataSource {

  def this(aContext: GEntityContext) = this(new WorkFileLocator(), aContext)
}
