package $package$

import org.goldenport._
import org.goldenport.application.GApplicationDescriptor
import org.goldenport.service._
import org.goldenport.entity._
import $package$.entities._
import $package$.importers._
import $package$.services._
import $package$.transformers._

class $classname$Processor(args: Array[String]) {
  lazy val goldenport = new Goldenport(args, new $classname$Descriptor)

  final def executeShellCommand(args: Array[String]) {
    goldenport.open()
    goldenport.executeShellCommand(args)
    goldenport.close()
  }
}

object Main {
  def main(args: Array[String]) {
    val sample = new $classname$Processor(args)
    sample.executeShellCommand(args)
  }
}

class $classname$Descriptor extends GApplicationDescriptor {
  name = "$name$"
  version = "$version$"
  version_build = "$build$"
  copyright_years = "$year$"
  copyright_owner = "$owner$"
  command_name = "$command$"
  //
  importers($classname$Importer)
  entities($classname$Entity)
  services($classname$Service)
}
