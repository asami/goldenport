package org.goldenport.value

import scala.xml._
import scala.collection.mutable.ArrayBuffer
import java.util.Locale
import com.asamioffice.goldenport.xml.XmlAttributeBuffer

/*
 * @since   Aug. 13, 2008
 * @version Sep. 19, 2011
 * @author  ASAMI, Tomoharu
 */
trait GTreeNodeStructureBase[E] extends GTreeNode[E] {
  private var node_parent: GTreeNode[E] = _
  private val node_children = new ArrayBuffer[GTreeNode[E]]()
  private var node_facade: GTree[E] = _
  private var is_filled_children = false
  private var is_modified = false
  private var xml_attributes = new XmlAttributeBuffer

  override def isRoot: Boolean = { parent == null }

  override def isContainer: Boolean = {
    is_Container match {
      case Some(true) => true
      case Some(false) => false
      case None => !isEmpty || content == null
    }
  }

  protected def is_Container: Option[Boolean] = None

  override def isLeaf: Boolean = {
    is_Leaf match {
      case Some(true) => true
      case Some(false) => false
      case None => isEmpty && content != null
    }
  }

  protected def is_Leaf: Option[Boolean] = None

  override def isEmpty: Boolean = node_children.isEmpty
  override def parent: TreeNode_TYPE = node_parent.asInstanceOf[TreeNode_TYPE]
  override def parent_=(aParent: GTreeNode[E]) { node_parent = aParent }
  override def facade: GTree[E] = node_facade
  override def facade_=(aFacade: GTree[E]) { node_facade = aFacade }

  override def length: Int = {
    fill_children()
    node_children.length
  }

  override def getChild(index: Int): TreeNode_TYPE = {
    fill_children()
    node_children(index).asInstanceOf[TreeNode_TYPE]
  }

  override def getChild(name: String): Option[TreeNode_TYPE] = {
    fill_children()
    node_children.find(_.name == name).asInstanceOf[Option[TreeNode_TYPE]]
  }

  override def setChild(name: String): TreeNode_TYPE = {
    fill_children()
    val mayNode = getChild(name)
    if (mayNode.isDefined) mayNode.get
    else set_child(new_Node(name))
  }

  override def addChild(): TreeNode_TYPE = {
    addChild(new_Node(null))
  }

  override def addChild(child: GTreeNode[E]): TreeNode_TYPE = {
    set_child(child)
    set_Child(child)
    child.asInstanceOf[TreeNode_TYPE]
  }

  override def addChildren(parent: GTreeNode[E]) {
    for (child <- parent.children) addChild(child.asInstanceOf[TreeNode_TYPE])
  }

  override def addContent(aContent: E): TreeNode_TYPE = {
    val node = addChild()
    node.content = aContent
    node
  }

  protected final def set_child(child: GTreeNode[E]): TreeNode_TYPE = {
    fill_children()
    require(getChild(child.name).isEmpty, "GTreeNodeStructureBase: not empty = " + pathname + ", "  + child.name)
    child.setModified()
    child.parent = this.asInstanceOf[child.TreeNode_TYPE]
    child.facade = node_facade;
    node_children += child
    set_modified()
    child.asInstanceOf[TreeNode_TYPE]
  }

  protected def set_Child(child: GTreeNode[E]): Unit = null

  protected def new_Node(name: String): TreeNode_TYPE

  protected def copy_Node(): TreeNode_TYPE = error("missing copy_Node for deepCopy: " + this)

  override def removeChild(aChild: GTreeNode[E]) {
    node_children -= aChild.asInstanceOf[TreeNode_TYPE]
  }

  override def getNode(pathname: String): Option[TreeNode_TYPE] =
    getNode(new GPathname(pathname))

  override def getNode(pathname: GPathname): Option[TreeNode_TYPE] = {
    var child: GTreeNode[E] = this
    for (comp <- pathname.components) {
      val mayChild = child.getChild(comp)
      if (mayChild.isEmpty) {
	return mayChild.asInstanceOf[Option[TreeNode_TYPE]]
      }
      child = mayChild.get
    }
    new Some(child).asInstanceOf[Option[TreeNode_TYPE]]
  }

  override def setNode(pathname: String): TreeNode_TYPE =
    setNode(new GPathname(pathname))

  override def setNode(pathname: GPathname): TreeNode_TYPE = {
    var current: GTreeNode[E] = this
    for (comp <- pathname.components) {
      val mayNode = current.getChild(comp)
      current = mayNode.getOrElse(current.setChild(comp))
    }
    set_modified()
    current.asInstanceOf[TreeNode_TYPE]
  }

  override def setContent(pathname: String, content: E): TreeNode_TYPE = {
    setContent(new GPathname(pathname), content)
  }

  override def setContent(pathname: GPathname, content: E): TreeNode_TYPE = {
    val node = setNode(pathname)
    node.content = content
    set_modified()
    node
  }

  override def children: Seq[TreeNode_TYPE] = {
    fill_children()
    node_children.toList.asInstanceOf[Seq[TreeNode_TYPE]]
  }

  private def fill_children(): Unit = {
    if (!is_filled_children) {
      for (child <- load_Children()) {
	child.parent = this.asInstanceOf[child.TreeNode_TYPE]
	node_children += child.asInstanceOf[TreeNode_TYPE]
      }
    }
    is_filled_children = true
  }

  protected def load_Children(): Seq[TreeNode_TYPE] = Nil

  final def clear() {
    node_children.clear
  }

  override def indexOf(aNode: GTreeNode[E]): Int = {
    node_children.indexOf(aNode)
  }

  override def traverse(visitor: GTreeVisitor[E]) {
    traverse(visitor, null)
  }

  override def traverse(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean) {
    if (filter != null && !filter.apply(this)) return
    visitor.start(this)
    if (is_stop_start(visitor, this)) return
    traverseChildren(visitor, filter)
    visitor.end(this)
  }

  override def traverseChildren(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean) {
    val childs = children
    if (childs.length > 0) {
      var child = childs(0)
      child.traverseNode(visitor, filter)
      for (i <- 1 until childs.length) {
	val prev = child;
	child = childs(i)
	visitor.stay(this, i, prev, child)
	if (is_stop_stay(visitor, this, i, prev, child)) return
	child.traverseNode(visitor, filter)
      }
    }
  }

  override def traverseNode(visitor: GTreeVisitor[E], filter: GTreeNode[E] => Boolean) {
    if (filter != null && !filter.apply(this)) return
    visitor.enter(this)
    if (is_stop_enter(visitor, this)) return
    traverseChildren(visitor, filter)
    visitor.leave(this)
  }

  final def traverse(aFunction: E => Unit) {
    traverse(new FunctionVisitor[E](aFunction))
  }

  private def is_stop_start(visitor: GTreeVisitor[E], aNode: GTreeNode[E]): Boolean = {
    visitor.isDone(aNode)
  }

  private def is_stop_enter(visitor: GTreeVisitor[E], aNode: GTreeNode[E]): Boolean = {
    visitor.isDone(aNode)
  }

  private def is_stop_stay(visitor: GTreeVisitor[E], node: GTreeNode[E], index: Int, prev: GTreeNode[E], next: GTreeNode[E]): Boolean = {
    visitor.isDone(node)
  }

  final def isModified: Boolean = is_modified

  final def clearModified() {
    is_modified = false
  }

  final def setModified() {
    set_modified()
  }

  private def set_modified() {
    is_modified = true
    if (node_facade != null) node_facade.setModified()
  }

  final def cursor: GTreeCursor[E] = {
    new GTreeCursor[E](this)
  }

  def deepCopy: TreeNode_TYPE = {
    val node = copy_Node()
    if (is_filled_children) 
      for (child <- node_children)
	node.addChild(child.deepCopy.asInstanceOf[node.TreeNode_TYPE])
    val node2 = node.asInstanceOf[GTreeNodeStructureBase[E]]
    node2.is_filled_children = is_filled_children
    node2.is_modified = false
    node
  }

  //
  final def toText: String = {
    val buf = new StringBuilder
    buildText(buf)
    buf.toString
  }

  final def buildText(aBuffer: StringBuilder): StringBuilder = {
    build_Text(aBuffer)
    aBuffer
  }

  protected def build_Text(aBuffer: StringBuilder): StringBuilder = {
    build_Text_Prologue(aBuffer)
    children.foreach(_.buildText(aBuffer))
    build_Text_Epilogue(aBuffer)
    aBuffer
  }

  protected def build_Text_Prologue(aBuffer: StringBuilder): Unit = null
  protected def build_Text_Epilogue(aBuffer: StringBuilder): Unit = null

  // XML
  final def getXmlAttribute(key: String): Option[Any] = {
    xml_attributes.get(key)
  }

  final def getXmlAttributeString(key: String): Option[String] = {
    val mayValue = getXmlAttribute(key)
    if (mayValue == None) return None
    mayValue.get match {
      case v: String => mayValue.asInstanceOf[Option[String]]
      case v: Text => Some(v.toString)
      case v: PCData => Some(v.toString)
      case v => Some(v.toString)
    }
  }

  final def putXmlAttribute(key: String, value: Any): Option[Any] = {
    xml_attributes.put(key, value)
  }

  final def putXmlAttributes(theAttrs: MetaData) {
    for (attr <- theAttrs) {
      xml_attributes.put(attr.key, attr.value)
    }
  }

  final def getXmlLang: Option[String] = {
    xml_attributes.get("xml:lang").asInstanceOf[Option[String]]
  }

  final def getXmlLocale: Option[Locale] = {
    xml_attributes.get("locale").asInstanceOf[Option[Locale]]
  }

  final def toXml: Node = {
    var node = xml_Node()
    if (node != null) return node
    new Elem(make_prefix, make_element_label, make_attributes, TopScope, make_children: _*)
  }

  final def toPrettyXml: String = {
    val printer = new PrettyPrinter(80, 1)
    printer.format(toXml)
  }

  protected final def make_prefix: String = null // XXX

  protected final def make_element_label: String = {
    if (xml_Element_Label != null) xml_Element_Label
    else getClass.getSimpleName
  }

  protected final def make_attributes: MetaData = {
    xml_attributes.toMetaData(
      if (xml_Element_Label != null) xml_Attributes
      else if (name != null) Array((null, "name", name),
				   (null, "content", content))
      else Nil)
  }

  protected final def make_children: Seq[Node] = {
    children.map(_.toXml)
  }

  protected def xml_Node(): Node = null
  protected def xml_Element_Label: String = null
  protected def xml_Element_Uri: String = null
  protected def xml_Attributes: Seq[(String, String, Any)] = Nil
}
