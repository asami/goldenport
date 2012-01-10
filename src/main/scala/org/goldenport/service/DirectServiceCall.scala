package org.goldenport.service

import org.goldenport.parameter._
import com.asamioffice.util._

/*
 * @since   Nov. 12, 2011
 * @version Nov. 12, 2011
 * @author  ASAMI, Tomoharu
 */
class DirectServiceCall(name: String, args: Seq[Object], props: Map[String, Object] = Map.empty) extends GServiceCall {
  request.setServiceName(name)
  request.addArguments(args)
  request.setParameters(get_params(props))

  private def get_params(map: Map[String, Object]): GParameterRepository = {
    val params = new GParameterRepository
    params.setCategory(Parameter_Command)
    for (entry <- map.elements) {
      params.put(entry._1, entry._2)
    }
    params
  }
}
