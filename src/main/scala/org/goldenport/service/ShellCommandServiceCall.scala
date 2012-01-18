package org.goldenport.service

import org.goldenport.parameter._
import com.asamioffice.goldenport.util._

/*
 * Aug. 28, 2008
 * Oct. 30, 2008
 */
class ShellCommandServiceCall(theArgs: Array[String], theParams: GParameterRepository) extends GServiceCall {
  val parser = new CommandParameterParser(theArgs)
  val params = get_params(parser, theParams)
  request.setServiceName(get_service_name(parser))
  request.addArguments(parser.getArguments)
  request.setParameters(params)

  private def get_params(aParser: CommandParameterParser, theParams: GParameterRepository): GParameterRepository = {
    val params = new GParameterRepository
    params.setParent(theParams)
    params.setCategory(Parameter_Command)
    for (option <- parser.getOptions) {
      params.put(option.getKey, option.getValue)
    }
    params
  }

  private def get_service_name(aParser: CommandParameterParser) = {
    aParser.getService match {
      case null => ""
      case name => name
    }
  }
}
