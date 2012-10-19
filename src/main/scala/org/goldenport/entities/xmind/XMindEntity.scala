package org.goldenport.entities.xmind

import scala.xml.{Node, Elem, Group}
import java.io.OutputStream
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.entities.outline._
import org.goldenport.value.GTreeBase

/*
 * @since   Jan. 31, 2009
 * @version Oct. 19, 2012
 * @version Oct. 19, 2012
 * @author  ASAMi, Tomoharu
 */
class XMindEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends OutlineEntityBase(aIn, aOut, aContext) {

  val xmindContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
    set_root(new RootNode)
    val templateDs = new ResourceDataSource("org/goldenport/entities/xmind/template.xmind", xmindContext)
    load_datasource(templateDs)
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
    set_root(new RootNode)
    val templateDs = new ResourceDataSource("org/goldenport/entities/xmind/template.xmind", xmindContext)
    load_datasource(templateDs)
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    set_root(new RootNode)
    load_datasource(aDataSource)
  }

  protected def load_datasource(aDataSource: GDataSource) {
    load_Datasource(aDataSource)
  }

  override protected def load_Datasource(aDataSource: GDataSource) {
    val file = new ZipEntity(aDataSource, xmindContext)
    file.open()
    try {
      val mayContent = file.getContent("/content.xml")
      val content = mayContent.get
//      println("XMind = " + content) 2009-02-27
      val xml = content.getXml
//      println("XMind xml = " + xml)
//      println("sheet = " + (xml \ "sheet"))
      for (child <- xml \ "sheet") {
	add_sheet(child)
      }
/* 2009-02-05
      for (node <- xml.child) {
	node match {
	  case <sheet>{_*}</sheet> => add_sheet(node)
	  case _ => //
	}
      }
*/
    } finally {
      file.close()
    }
  }    

  private def add_sheet(aSheet: Node) {
//    println("shhet = " + aSheet)
    val id = (aSheet \ "@id").text
    val sheet = new SheetNode(id)
    sheet.title = (aSheet \ "title").text
    root.addChild(sheet)
    for (child <- aSheet \ "topic") {
      add_topic(child, sheet)
    }
  }

  private def add_topic(aTopic: Node, aParent: OutlineNode) {
    val id = (aTopic \ "@id").text
    val topic = TopicNode(id)
    topic.title = (aTopic \ "title").text
    aParent.addChild(topic)
    for (child <- aTopic \ "children" \ "topics" \ "topic") {
      add_topic(child, topic)
    }
  }

  override protected def write_Content(anOut: OutputStream): Unit = {
//    println("XMindEntity root = " + root.toPrettyXml)
    val file = template_file
    file.open()
    val xml = make_content_xml
    file.setString("/content.xml", xml.toString)
    file.write(anOut)
    file.close()
  }

  override def firstThema: TopicNode = {
    root.getChild(0).getChild(0).asInstanceOf[TopicNode]
  }

  private def template_file = {
    val templateDs = new ResourceDataSource("org/goldenport/entities/xmind/template.xmind", xmindContext)
    new ZipEntity(templateDs, NullDataSource, xmindContext)
  }

  private def make_content_xml: Elem = {
    def make_child(child: OutlineNode): Elem = {
      child match {
	case root: RootNode => sys.error("not reached.")
	case sheet: SheetNode => {
	  <sheet id={sheet.id} style-id="$NULL$map">
	    { make_xml_node(sheet) }
	    <title>{sheet.title}</title>
            <legend visibility="hidden">
              <position svg:x="602" svg:y="11"/>
            </legend>
	  </sheet>
	}
	case topic: TopicNode => {
	  if (topic.parent.isInstanceOf[SheetNode]) {
	    <topic id={topic.id} structure-class="org.xmind.ui.map">
	      <title>{topic.title}</title>
	      {
		if (!topic.isEmpty) {
		  <children>
		    <topics type="attached">
                      { make_xml_node(topic) }
		    </topics>
		  </children>
		} else {
//		  println("topic = " + topic.toPrettyXml)
		}
	      }
	    </topic>
	  } else {
	    <topic id={topic.id}>
	      <title>{topic.title}</title>
	      {
		if (!topic.isEmpty) {
		  <children>
		    <topics type="attached">
		      { make_xml_node(topic) }
		    </topics>
		  </children>
		} else {
//		  println("topic = " + topic.toPrettyXml)
		}
	      }
	    </topic>
	  }
	}
      }
    }

    def make_xml_node(aNode: OutlineNode): Seq[Node] = {
      aNode.children.map(make_child)
    }

    <xmap-content xmlns="urn:xmind:xmap:xmlns:content:2.0" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:svg="http://www.w3.org/2000/svg" xmlns:xhtml="http://www.w3.org/1999/xhtml" xmlns:xlink="http://www.w3.org/1999/xlink" version="2.0">
      { make_xml_node(root) }
    </xmap-content>
  }
}

class XMindEntityClass extends GEntityClass {
  type Instance_TYPE = XMindEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "xmind"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new XMindEntity(aDataSource, aContext))
}

object XMindEntity extends XMindEntityClass
