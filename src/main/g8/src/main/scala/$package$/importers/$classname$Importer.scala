package $package$.importers

import org.goldenport.importer._
import org.goldenport.service._
import org.goldenport.entity.GEntity

class $classname$Importer(aCall: GServiceCall) extends GImporter(aCall) {
  val packageNames: Seq[String] = request.parameter("source.package") match {
    case Some(name) => name.asInstanceOf[AnyRef].toString.split(":").map(_.trim)
    case None       => Nil
  }
  val packageName = if (packageNames.isEmpty) "" else packageNames.head

  override def execute_Import() {
    val args = request.arguments.map(reconstitute_entity)
    if (!args.isEmpty) {
      request.setEntity(args(0).get)
      request.setEntities(args.map(_.get))
    }
  }
}

object $classname$Importer extends GImporterClass {
  type Instance_TYPE = $classname$Importer

  override protected def accept_Service_Call(aCall: GServiceCall): Option[Boolean] = {
    aCall.service.name match {
      case "$service$" => Some(true)
      case _         => None
    }
  }

  override protected def new_Importer(aCall: GServiceCall): GImporter = {
    new $classname$Importer(aCall)
  }
}
