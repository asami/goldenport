package org.goldenport.entity.datasource

import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.WorkFileLocator

/*
 * @since   Aug.  9, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class WorkFileDataSource(aUri: WorkFileLocator, aContext: GEntityContext) extends GDataSource(aContext, aUri) with GContentDataSource {
  def this(aContext: GEntityContext) = this(new WorkFileLocator(), aContext)
}
