package org.goldenport.exporter

import org.goldenport.GClass
import org.goldenport.service.GServiceCall

/*
 * @since   Nov.  2, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class GExporterClass extends GClass {
   type Instance_TYPE <: GExporter

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

  final def newExporter(aCall: GServiceCall): GExporter = {
    require (aCall != null)
    new_Exporter(aCall) ensuring (_ != null)
  }

  final def newExporterOption(aCall: GServiceCall): Option[GExporter] = {
    require (aCall != null)
    if (!accept(aCall)) return None
    else Some(newExporter(aCall))
  }

  protected def new_Exporter(aCall: GServiceCall): GExporter
}
