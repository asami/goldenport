package org.goldenport.entities.opml

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

/**
 * @since   Sep. 15, 2010 (in g3)
 * @since   Nov. 29, 2011
 * @version Dec.  1, 2011
 * @author  ASAMI, Tomoharu
 */
class OpmlEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends OutlineEntityBase(aIn, aOut, aContext) {
  override type DataSource_TYPE = GDataSource

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  protected override def load_Datasource(aDataSource: GDataSource) {
    try {
      val xml = aDataSource.loadXml()
      val body = xml \\ "body"
      for (b <- body.elements) { 
        add_topic(b, root)
      }
    } finally {
    }
  }

  private def add_topic(aTopic: Node, aParent: OutlineNode) {
    val text = (aTopic \ "@text").text
    val topic = new TopicNode(text)
    topic.title = text
    aParent.addChild(topic)
    for (child <- aTopic \ "outline") {
      add_topic(child, topic)
    }
  }

  override protected def write_Content(anOut: OutputStream): Unit = {
    def make_title: String = name

    def make_outlines: Seq[Node] = make_children(root)

    def make_children(node: OutlineNode): Seq[Node] = {
      for (child <- node.children) yield {
        <outline text={child.name}>{make_children(child)}</outline>
      }
    }

/*
    def make_text(node: OpmlNode): String = {
      node.children.filter(_.content != null).map(_.content).mkString
    }
*/

    val xml =
<opml version="1.0">
<head><title>{make_title}</title></head>
<body>{make_outlines}</body>
</opml>
    anOut.write(xml.toString.getBytes("utf-8"))
  }
}

class OpmlEntityClass extends GEntityClass {
  type Instance_TYPE = OpmlEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "opml"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new OpmlEntity(aDataSource, aContext))
}

object OpmlEntity extends OpmlEntityClass
