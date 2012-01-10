package org.goldenport.entities.smartdoc

import scala.xml.{Node, Elem}
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.FileDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc._
import org.goldenport.sdoc.structure._
import org.goldenport.value.GTreeBase

/*
 * Sep.  2, 2008
 * Oct. 18, 2008
 */
class SmartDocEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTreeEntityBase[SDoc](aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource
  override type TreeNode_TYPE = SDoc
  override def is_Text_Output = true
  private var sdoc_doc: SSDocument = null
  private val sdoc_factory = new SDocFactory

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
    require (sdoc_doc == null)
    build_body()
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    require (sdoc_doc == null)
    try {
      load_smartdoc(aDataSource) match {
	case snode: SSDocument => set_document(snode)
	case snode: SFragment => set_contents_fragment(snode)
	case snode => set_content(snode)
      }
    } catch {
      case e: java.io.IOException => not_implemented_yet(e)
    }
    assert(sdoc_doc != null)
    adapt_doc()
    normalize_doc()
  }

  private def set_document(sdoc: SSDocument) {
    require (sdoc != null)
    sdoc_doc = sdoc
    set_root(sdoc_doc)
  }

  private def set_contents_fragment(snode: SFragment) {
    require (snode != null)
    build_body().addChildren(snode)
  }

  private def set_content(snode: SDoc) {
    require (snode != null)
//    println("set_content = " + snode) ; 2008-09-29
    build_body().addChild(snode)
//    println("end of set_content") ; 2008-09-29
  }

  private def build_body(): SSBody = {
    require (sdoc_doc == null)
    sdoc_doc = new SSDocument()
    sdoc_doc.head = new SSHead()
    sdoc_doc.body = new SSBody()
    set_root(sdoc_doc)
    sdoc_doc.body
  }

  private def load_smartdoc(aDataSource: GDataSource): SDoc = {
    val elem = load_xml(aDataSource)
    if (elem != null) {
      sdoc_factory.create(elem)
    } else {
      val string = aDataSource.loadString()
// println("load_smartdoc = " + string) ; 2008-09-29
      sdoc_factory.create(string)
    }
  }

  private def load_xml(aDataSource: GDataSource): Elem = {
    try {
      aDataSource.loadXml()
    } catch {
      case e: Throwable => return null
    }
  }

  private def adapt_doc() {
  }

  private def normalize_doc() {
    normalize_node(sdoc_doc)
  }

  private def normalize_node(aNode: SDoc) {
    if (!aNode.normalize())
      aNode.children.foreach(normalize_node)
  }

  final def head: SSHead = {
    require (sdoc_doc != null)
    sdoc_doc.head
  }

  final def body: SSBody = {
    require (sdoc_doc != null)
    sdoc_doc.body
  }
}

class SmartDocEntityClass extends GEntityClass {
  type Instance_TYPE = SmartDocEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "sdoc"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new SmartDocEntity(aDataSource, aContext))
}
