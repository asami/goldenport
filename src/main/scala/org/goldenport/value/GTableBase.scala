package org.goldenport.value

import scala.collection.mutable.ArrayBuffer
import org.goldenport.sdoc._

/*
 * XXX immutable
 *
 * @since   Aug.  6, 2008
 *  version Mar. 19, 2011
 *  version Dec.  1, 2011
 * @version Mar. 17, 2012
 * @author  ASAMI, Tomoharu
 */
trait GTableBase[E] extends GTable[E] {
  private var currentWidth = 0
  private var currentHeight = 0
  private var allocWidth = 0
  private var allocHeight = 0
  private var contents: ArrayBuffer[ArrayBuffer[Option[E]]] = null
  private var table_head: GAttributedTabular[SDoc] = null
  private var table_side: GAttributedTabular[SDoc] = null
  private var _attributes: GTabular[GTabularAttribute] = null

  init(0, 0)

  private def init(width: Int, height: Int): Unit = {
    currentWidth = width;
    currentHeight = height;
    allocWidth = if (width == 0) 16 else width
    allocHeight = if (height == 0) 16 else height
    contents = create_matrix(allocHeight, allocWidth)
  }

  def width: Int = currentWidth

  def height: Int = currentHeight

  def get(x: Int, y: Int): E = contents(y)(x).get

  def getOrElse(x: Int, y: Int, value: E): E = {
    contents(y)(x).getOrElse(value)
  }

  def getOption(x: Int, y: Int): Option[E] = contents(y)(x)

  def put(x: Int, y: Int, value: E): Option[E] = {
    putOption(x, y, Some(value))
  }

  def putOption(x: Int, y: Int, value: Option[E]): Option[E] = {
//println("x, y = " + x + ", " + y)
    ensure_space(y + 1, x + 1)
    val old = contents(y)(x)
    contents(y)(x) = value
//    val row = contents(y)
//    println("row = " + row)
//    row(x) = value
    if (x >= currentWidth) currentWidth = x + 1
    if (y >= currentHeight) currentHeight = y + 1
    table_Update()
    return old
  }

  def +=(aSeq: Seq[E]) {
    append(aSeq)
  }

/*
  def +=(cell1: E) {
    append(List(cell1).asInstanceOf[Seq[E]])
  }

  def +=(cell1: E, cell2: E) {
    append((cell1, cell2))
  }

  def +=(cell1: E, cell2: E, cell3: E) {
    append((cell1, cell2, cell3))
  }

  def +=(cell1: E, cell2: E, cell3: E, cell4: E) {
    append((cell1, cell2, cell3, cell4))
  }

  def +=(cell1: E, cell2: E, cell3: E, cell4: E, cell5: E) {
    append((cell1, cell2, cell3, cell4, cell5))
  }

  def +=(tuple: Product1[E]) {
    append(tuple)
  }

  def +=(tuple: Product2[E, E]) {
    append(tuple)
  }

  def +=(tuple: Product3[E, E, E]) {
    append(tuple)
  }

  def +=(tuple: Product4[E, E, E, E]) {
    append(tuple)
  }

  def +=(tuple: Product5[E, E, E, E, E]) {
    append(tuple)
  }

  def append(tuple: Product) {
    val y = height
    for (x <- 0 until tuple.productArity) 
      put(x, y, tuple.productElement(x).asInstanceOf[E])
  }
*/

  def append(seq: Seq[E]) {
    val y = height
    for (x <- 0 until seq.length) 
      put(x, y, seq(x).asInstanceOf[E])
  }

  def rows: List[List[E]] = {
    val rTarget = new ArrayBuffer[List[E]]
    for (y <- 0 until height) {
      val cSource = contents(y)
      val cTarget = new ArrayBuffer[E]
      for (x <- 0 until width) {
	cTarget += cSource(x).get
      }
      rTarget += cTarget.toList
    }
    rTarget.toList
  }

  def terseRows: Seq[Seq[E]] = {
    val rTarget = new ArrayBuffer[List[E]]
    for (y <- 0 until height) {
      val cSource = contents(y)
      val cTarget = new ArrayBuffer[E]
      var done = false
      var x = 0
      while (!done) {
//println(" c(" + x + ") = " + cSource(x) + ", " + cSource(x).getClass)
	if (x == width) {
	  done = true
	} else if (cSource(x) == null || cSource(x).isEmpty) {
	  done = true
	} else {
	  cTarget += cSource(x).get
	}
	x += 1
      }
      rTarget += cTarget.toList
    }
    rTarget.toList
  }

  // shallow copy
  final def putAll(aSrc: GTabular[E]) {
    aSrc.rows.foreach(append) // XXX optimize
  }

  // shallow copy
  final def putAllWithAttributes(aSrc: GAttributedTabular[E]) {
    // XXX attributes
    aSrc.rows.foreach(append) // XXX optimize
  }

  // shallow copy
  final def copyIn(aSrc: GTable[E]) {
    if (aSrc.head.isDefined) {
      if (table_head == null) {
        table_head = new PlainTable[SDoc]
      }
      table_head.putAllWithAttributes(aSrc.head.get)
    }
    if (aSrc.side.isDefined) {
      if (table_side == null) {
        table_side = new PlainTable[SDoc]
      }
      table_side.putAllWithAttributes(aSrc.side.get)
    }
    aSrc.rows.foreach(append) // XXX optimize
  }

  final override def rowCursor = new Cursor {
    val y = height
    var x = 0

    def +=(content: E) {
      put(x, y, content)
      x += 1
    }
  }

  protected def table_Update(): Unit = null

  private def create_matrix(height: Int, width:Int): ArrayBuffer[ArrayBuffer[Option[E]]] = {
    val matrix = new ArrayBuffer[ArrayBuffer[Option[E]]]
    for (y <- 0 until height) {
      val row = new ArrayBuffer[Option[E]]
      matrix += row
      for (x <- 0 until width) {
        row += None
      }
    }
    return matrix
  }

  private def ensure_space(height: Int, width: Int): Unit = {
    for (y <- 0 until currentHeight) {
      val row = contents(y)
      for (x <- row.size until width) {
        row += None
      }
//      println("inner width = " + width + " row size = " + row.size)
    }
    for (y <- currentHeight until height) {
      val row = new ArrayBuffer[Option[E]]
      contents += row
      for (x <- 0 until width) {
        row += None
      }
//      println("outer width = " + width + " row size = " + row.size)
    }
  }

/*
  private def ensure_space(height: Int, width: Int): Unit = {
    def calc_space(alloc: Int, spec: Int): Int = {
      if (alloc < spec) {
	var l = alloc * 2;
	while (l < spec) {
	  l *= 2
	}
	return l
      } else {
	return alloc
      }
    }

    if (allocWidth >= width && allocHeight >= height) {
      return
    }
    val newWidth = calc_space(allocWidth, width)
    val newHeight = calc_space(allocHeight, height)
    val newContents = create_matrix(newHeight, newWidth)
    for (y <- 0 until currentHeight) {
      for (x <- 0 until currentWidth) {
	newContents(y)(x) = contents(y)(x)
      }
    }
    contents = newContents;
    allocWidth = newWidth;
    allocHeight = newHeight;
    return
  }
*/

  private def ensure_table_head() {
    if (table_head == null) table_head = new PlainTable[SDoc]
  }

  private def ensure_table_side() {
    if (table_side == null) table_side = new PlainTable[SDoc]
  }

  override def head: Option[GAttributedTabular[SDoc]] = {
    if (table_head == null) return None
    Some(table_head)
  }

  override def headAsStringList: Option[List[String]] = {
    if (table_head == null) None
    else if (table_head.height == 0) None
    else {
      // XXX join upper label for prefix
      Some(table_head.rows(table_head.height - 1).map(_.toText).toList)
    }
  }

  override def setHead(aHead: GAttributedTabular[SDoc]) = {
    ensure_table_head()
    table_head.putAllWithAttributes(aHead)
  }

  override def setHead(aHead: GTree[SDoc]) = {
    ensure_table_head()
    val depth = calc_depth(aHead.root)
    val root = aHead.root
    var x = 0;
    var y = 0;

    def build(child: GTreeNode[SDoc]) {
      if (child.isEmpty) {
	table_head.put(x, y, child.content)
	val attr = new GTabularAttribute
	attr.rowspan = depth - y
	table_head.setAttribute(x, y, attr)
	x += 1
      } else {
	table_head.put(x, y, child.content)
	val attr = new GTabularAttribute
	attr.colspan = calc_breadth(child)
	table_head.setAttribute(x, y, attr)
	y += 1
	child.children.foreach(build)
	y -= 1
      }
    }

    root.children.foreach(build)
/*
    for (child <- root.children) {
      if (child.isEmpty) {
	table_head.put(x, y, child.content)
	val attr = new GTabularAttribute
	attr.rowspan = 2
	table_head.setAttribute(x, y, attr)
	x += 1
      } else {
	table_head.put(x, y, child.content)
	val attr = new GTabularAttribute
	attr.colspan = child.length
	table_head.setAttribute(x, y, attr)
	var yy = 1
	var xx = x
	for (subchild <- child.children) {
	  table_head.put(xx, yy, subchild.content)
	  xx += 1
	}
	x += child.length
      }
    }
*/
  }

  override def setHead(aHead: Seq[SDoc]) = {
    ensure_table_head()
    table_head += aHead
  }

  override def setHeadString(aHead: Seq[String]) = {
    val head = new Array[SDoc](aHead.size)
    for (i <- 0 until aHead.size) {
      head(i) = aHead(i)
    }
    setHead(head)
  }

  override def side: Option[GAttributedTabular[SDoc]] = {
    if (table_side == null) return None
    Some(table_side)
  }

  override def setSide(aSide: GAttributedTabular[SDoc]) = {
    sys.error("setSide")
  }

  private def calc_depth(node: GTreeNode[SDoc]) = {
    var maxDepth = 0
    var currentDepth = 0

    def calc_child(aChild: GTreeNode[SDoc]) {
      currentDepth += 1
      if (maxDepth < currentDepth) {
	maxDepth = currentDepth
      }
      aChild.children.foreach(calc_child)
      currentDepth -= 1
    }

    node.children.foreach(calc_child)
    maxDepth
  }

  private def calc_breadth(node: GTreeNode[SDoc]) = {
    var breadth = 0
    
    def calc_child(aChild: GTreeNode[SDoc]) {
      if (aChild.isLeaf) breadth += 1
      aChild.children.foreach(calc_child)
    }

    node.children.foreach(calc_child)
    breadth
  }

  override def setSide(aSide: GTree[SDoc]) = {
    ensure_table_side()
    val depth = calc_depth(aSide.root)
    val root = aSide.root
    var x = 0;
    var y = 0;

    def build(child: GTreeNode[SDoc]) {
      if (child.isEmpty) {
	table_side.put(x, y, child.content)
	val attr = new GTabularAttribute
	attr.colspan = depth - x
	table_side.setAttribute(x, y, attr)
	y += 1
      } else {
	table_side.put(x, y, child.content)
	val attr = new GTabularAttribute
	attr.rowspan = calc_breadth(child)
	table_side.setAttribute(x, y, attr)
	x += 1
	child.children.foreach(build)
	x -= 1
      }
    }

    root.children.foreach(build)
  }

  override def setSide(aSide: Seq[SDoc]) = {
    ensure_table_side()
    for (y <- 0 until aSide.length) {
      table_side.put(0, y, aSide(y))
    }
  }

  override def setSideString(aSide: Seq[String]) = {
    val side = new Array[SDoc](aSide.size)
    for (i <- 0 until aSide.size) {
      side(i) = aSide(i)
    }
    setSide(side)
  }

  override def headTree: Option[GTree[SDoc]] = {
    if (table_head == null) return None
    val tree = new PlainTree[SDoc]
    var current = tree.root
    for (x <- 0 until table_head.width) {
      var column = current
      for (y <- 0 until table_head.height) {
	column = column.addChild()
	column.content = table_head.get(x, y)
      }
    }
    Some(tree)
  }

  override def sideTree: Option[GTree[SDoc]] = {
    if (table_side == null) return None
    sys.error("")
  }

  override def columnNames: Seq[SDoc] = {
    if (table_head == null) return Nil
    headTree.get.collect(_.isLeaf).map(_.pathname.substring(1)).map(SText)
  }

  override def rowNames: Seq[SDoc] = {
    if (table_side == null) return Nil
    sideTree.get.collect(_.isLeaf).map(_.pathname.substring(1)).map(SText)
  }

  override def getAttribute(x: Int, y: Int): GTabularAttribute = {
    if (_attributes == null) null
    else _attributes.get(x, y)
  }

  override def setAttribute(x: Int, y: Int, attr: GTabularAttribute) {
    if (_attributes == null) _attributes = new PlainTable[GTabularAttribute]
    _attributes.put(x, y, attr)
  }
}
