package org.goldenport.sdoc

import scala.collection.mutable
import scala.xml._
import java.util.UUID
import org.goldenport.value.GTreeNodeBase
import org.goldenport.sdoc.attribute._

/*
 * derived from SNode.java since Sep. 17, 2006
 *
 * Sep.  1, 2008
 * Nov.  9, 2008
 */
abstract class SDoc(theChildren: SDoc*) extends GTreeNodeBase[SDoc] {
  type TreeNode_TYPE = SDoc
  content = this
  theChildren.foreach(addChildOrChildren)

  var id: String = _
  var style: String = ""
  var tocNumber: Int = 0
  private var _effective_id: String = _
  private var _title: SATitle = _

  protected def new_Node(name: String): SDoc = {
    error("newnode : " + this + "/" + getClass + " by " + name)
  }

  def isNil: Boolean = false

  def effectiveId: String = {
    if (id != null) id
    else {
      if (_effective_id == null) {
	_effective_id = UUID.randomUUID().toString()
      }
      _effective_id
    }
  }

  final def title_=(aTitle: SDoc*) {
    title = SATitle(aTitle: _*)
  }

  final def title_=(aTitle: SATitle) {
    _title = aTitle
    addChild(_title)
  }

  final def title: SATitle = {
    if (_title != null) return _title
    val titles = children.filter(_.isInstanceOf[SATitle]).map(_.asInstanceOf[SATitle])
    if (!titles.isEmpty) return titles(0) // XXX
    val mayTitle = getXmlAttributeString("title")
    if (mayTitle.isDefined) return SATitle(mayTitle.get)
    SATitle()
  }

  final def style_is(aStyle: String): TreeNode_TYPE = {
    require (aStyle != null)
    style = aStyle
    this
  }

  final def addChildOrChildren(child: SDoc) {
//    println("add_child = " + parent + "/" + child) ; 2008-09-24
    if (child.isInstanceOf[SEmpty]) {
      // do nothing
    } else if (child.isInstanceOf[SFragment]) {
      addChildren(child)
    } else {
      addChild(child)
    }
  }

  // return whether normalizing is done or not
  final def normalize(): Boolean = {
    // XXX normalizer
    normalize_Node()
  }

  protected def normalize_Node(): Boolean = false

  final def toContents: SDoc = {
    length match {
      case 0 => SEmpty
      case 1 => getChild(0)
      case _ => SFragment(children.map(_.deepCopy): _*)
    }
  }
}

object SDoc {
  implicit def sdocWrapper(string: String): SDoc = SDocFactory.create(string)
  implicit def sdocWrapper(element: Elem): SDoc = SDocFactory.create(element)
}
