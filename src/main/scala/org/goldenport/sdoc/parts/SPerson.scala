package org.goldenport.sdoc.parts

import scala.collection.mutable.{Buffer, ArrayBuffer}
import org.goldenport.sdoc.SDoc

/*
 * Oct. 26, 2008
 * Nov.  2, 2008
 */
trait SPerson extends SParty {
  val prefix: Buffer[String] = new ArrayBuffer[String]
  var given_name: String = ""
  val middle_name: Buffer[String] = new ArrayBuffer[String]
  var family_name: String = ""
  var preferred_name: String = ""
  val suffix: Buffer[String] = new ArrayBuffer[String]

  override def label: SDoc = {
    if (preferred_name != "") preferred_name
    else if (family_name != "" && given_name != "") family_name + " " + given_name
    else if (family_name != "") family_name
    else if (given_name != "") given_name
    else getClass.getSimpleName
  }
}
