package org.goldenport.importer

import org.goldenport.GClass
import org.goldenport.service.GServiceCall

/*
 * @since   Nov.  1, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GImporterClass extends GClass {
   type Instance_TYPE <: GImporter

  final def accept(aCall: GServiceCall): Boolean = {
    accept_Service_Call(aCall) match {
      case Some(result) => return result
      case _ =>
    }
    false
  }

  protected def accept_Service_Call(aCall: GServiceCall): Option[Boolean] = {
    None
  }

  final def newImporter(aCall: GServiceCall): GImporter = {
    require (aCall != null)
    new_Importer(aCall) ensuring (_ != null)
  }

  final def newImporterOption(aCall: GServiceCall): Option[GImporter] = {
    require (aCall != null)
    if (!accept(aCall)) return None
    else Some(newImporter(aCall))
  }

  protected def new_Importer(aCall: GServiceCall): GImporter

}
