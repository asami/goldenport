package $package$.services

import org.goldenport.service._
import org.goldenport.entity._
import $package$._
import $package$.entities.$classname$Entity
import $package$.transformers.$classname$2RealmTransformer

class $classname$Service(aCall: GServiceCall, serviceClass: GServiceClass) extends GService(aCall, serviceClass) {
  def execute_Service(aRequest: GServiceRequest, aResponse: GServiceResponse) = {
    val in = aRequest.entity.asInstanceOf[$classname$Entity]
    val trans = new $classname$2RealmTransformer(aCall.serviceContext, in)
    val out = trans.transform()
    aResponse.addRealm(out)
  }
}

object $classname$Service extends GServiceClass("$service$") {
  def new_Service(aCall: GServiceCall): GService =
    new $classname$Service(aCall, this)
}
