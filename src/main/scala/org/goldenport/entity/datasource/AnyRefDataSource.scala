package org.goldenport.entity.datasource

import java.io._
import java.net.URL
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.locator.NameLocator

/*
 * @since   Aug. 30, 2008
 *  version Jul. 15, 2010
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class AnyRefDataSource(val anyRef: AnyRef, aContext: GEntityContext) extends GDataSource(aContext, new NameLocator(anyRef.toString)) with GContentDataSource {
}
