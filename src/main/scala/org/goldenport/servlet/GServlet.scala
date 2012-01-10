package org.goldenport.servlet

import scala.collection.JavaConverters._
import scala.collection.breakOut
import javax.servlet._
import javax.servlet.http._
import org.goldenport.application.GApplicationDescriptor
import org.goldenport.Goldenport
import org.goldenport.exporter.FirstLeafResultExporterClass
import org.goldenport.entity.content.BinaryContent
import com.asamioffice.text.UString
import com.asamioffice.text.UPathString
import org.apache.commons.fileupload.disk.DiskFileItemFactory
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.fileupload.FileItem

/*
 * @since   Nov. 25, 2011
 * @version Nov. 25, 2011
 * @author  ASAMI, Tomoharu
 */
class GServlet extends HttpServlet {
  private[this] var _goldenport: Option[Goldenport] = None

  override def init(config: ServletConfig) {
    println("Start GServlet")
    val descName = Option(config.getInitParameter("g.descriptor"))
    println("desc name = " + descName)
    val obj = descName flatMap(_new_instance(_))
    println("object = " + obj)
    val desc = obj collect {
      case desc: GApplicationDescriptor => desc
    }
    println("desc = " + desc)
    desc.foreach(_open_application(config, _))
  }

  private def _new_instance(qname: String): Option[AnyRef] = {
    try {
      Some(Class.forName(qname).newInstance().asInstanceOf[AnyRef])
    } catch {
       case e => { e.printStackTrace(); None }
    }
  }

  private def _new_instance(qnames: List[String]): Option[AnyRef] = {
    for (q <- qnames) {
      try {
        println("GServlet qname = " + q)
        val o = Class.forName(q).newInstance().asInstanceOf[AnyRef]
        println("GServlet qname = " + q + ", object = " + o)
        return Some(o)
      } catch {
       case e => { e.printStackTrace() }
      } 
    }
    return None
  }

  private def _open_application(config: ServletConfig, desc: GApplicationDescriptor) {
    println("_open_application: " + desc)
    val args: Array[String] = config.getInitParameterNames.asScala.map(_.toString).toArray 
    _goldenport = Some(new Goldenport(args, desc))
    _goldenport foreach { g => 
      g.addExporterClass(FirstLeafResultExporterClass)
      g.open()
    }
  }

  override def destroy() {
    _goldenport foreach { g =>
      g.close()
    }
  }

  override def doGet(req: HttpServletRequest, resp: HttpServletResponse) {
    println("doGet")
    _execute(req, resp)
  }

  override def doPost(req: HttpServletRequest, resp: HttpServletResponse) {
    println("doPost")
    _execute(req, resp)
  }

  override def doPut(req: HttpServletRequest, resp: HttpServletResponse) {
    println("doPost")
    _execute(req, resp)
  }

  private def _execute(req: HttpServletRequest, resp: HttpServletResponse) {
    for (g <- _goldenport) {
      val path = req.getPathInfo()
      val action = UPathString.getLastComponent(path)
      val params = _get_parameters(req)
      val (args, fields) = _get_arguments(req)
      println("params = " + params)
      println(action + "/" + args + "/" + params)
      g.execute(action, args, params) collect {
        case b: BinaryContent => _result(resp, b)
        case x => println("unknown: " + x)
      }
    }
  }

  private def _get_arguments(req: HttpServletRequest): (List[AnyRef], Map[String, AnyRef]) = {
    _get_form_items(req).foldLeft(Nil: List[AnyRef], Map.empty: Map[String, AnyRef])
    { (result, item) =>
      item match {
        case f if f.isFormField => {
          println("form field: " + f.getFieldName() + ", " + f.getString())
          (result._1, result._2 + ((f.getFieldName, f.getString)))
        } 
        case f => (result._1 ::: List(_goldenport.get.createDataSource(f.getName, f.get)), result._2)
      }
    }
  }

  private def _get_form_items(req: HttpServletRequest) = {
    val factory = new DiskFileItemFactory()
    val sfu = new ServletFileUpload(factory)
    sfu.parseRequest(req).asScala.toList collect {
      case item: FileItem => item
    }
  }
  
  private def _get_parameters(req: HttpServletRequest): Map[String, AnyRef] = {
    val entries = req.getParameterMap.entrySet.asScala.toList
    entries.map(kv => (kv.getKey.toString, kv.getValue.asInstanceOf[AnyRef]))(breakOut)
  }

  private def _result(resp: HttpServletResponse, b: BinaryContent) {
    val out = resp.getOutputStream()
    try {
      println("_result:" + b)
      b.mimeType match {
        case Some(mime) => resp.setContentType(mime)
        case None => resp.setContentType("application/octet-stream")
      }
      b.write(out)
    } finally {
      out.flush()
      out.close()
    }
  }
}
