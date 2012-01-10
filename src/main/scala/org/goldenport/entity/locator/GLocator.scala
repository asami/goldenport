package org.goldenport.entity.locator

import java.io.File
import java.net.{URL, URI}
import com.asamioffice.text.UPathString

/*
 * @since   Aug.  7, 2008
 * Aug. 14, 2008
 * @version Dec.  5, 2011
 * @author  ASAMI, Tomoharu 
 */
abstract class GLocator{
  def uri: URI
  def simpleName = UPathString.getLastComponent(_name_path)
  def simpleNameBody = UPathString.getLastComponentBody(_name_path)
  def getFile(): Option[File] = None
  def getUrl(): Option[URL] = None
  def getSuffix(): Option[String] = UPathString.getSuffix(_name_path) match {
    case null => None
    case "" => None
    case s => Some(s)
  }
  private def _name_path = {
    val s = uri.toString
    val index = s.indexOf(':')
    if (index == -1) s
    else s.substring(index + 1)
  }
  override def toString(): String = uri.toString()
}
