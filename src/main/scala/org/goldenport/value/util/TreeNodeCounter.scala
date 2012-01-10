package org.goldenport.value.util

import scala.collection.mutable.HashMap
import org.goldenport.value._
import org.goldenport.values.LayeredSequenceNumber

/*
 * @since   Nov. 24, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
class TreeNodeCounter[E](val roots: GTreeNode[E]*) {
  private val _sequenceNumbers = new HashMap[GTreeNode[E], Int]
  private val _layeredSequenceNumbers = new HashMap[GTreeNode[E], LayeredSequenceNumber]
  private var _seqNumber = 0

  def this(aTree: GTree[E]) = this(aTree.root)

  for (root <- roots) {
    root.traverse(new GTreeVisitor[E] {
      override def enter(aNode: GTreeNode[E]) {
	_seqNumber += 1
	_sequenceNumbers.put(aNode, _seqNumber)
	_layeredSequenceNumbers.put(aNode, calc_layeredSequenceNumber(aNode))
      }

      override def leave(aNode: GTreeNode[E]) {
      }
    })
  }

  final def getSequenceNumber(aNode: GTreeNode[E]): Int = {
    _sequenceNumbers(aNode)
  }

  final def getLayeredSequenceNumber(aNode: GTreeNode[E]): LayeredSequenceNumber = {
    _layeredSequenceNumbers(aNode)
  }

  private def calc_layeredSequenceNumber(aNode: GTreeNode[E]): LayeredSequenceNumber = {
    def calc_number(aNode: GTreeNode[E]): (Int, Boolean) = {
      if (roots.contains(aNode.parent)) {
	if (roots.length > 1) {
	  var index = 0
	  var done = false
	  var i = 0
	  val length = roots.length
	  while (i < length && !done) {
	    val root = roots(i)
	    if (root == aNode.parent) {
	      index += aNode.parent.indexOf(aNode) + 1
	      done = true
	    } else {
	      index += root.length
	    }
	    i += 1
	  }
	  (index, false)
	} else {
	  (aNode.parent.indexOf(aNode) + 1, false)
	}
      } else {
	(aNode.parent.indexOf(aNode) + 1, true)
      }
    }

    def calc_number_list(aNode: GTreeNode[E], aList: List[Int]): List[Int] = {
      calc_number(aNode) match {
	case (0, false) => aList
	case (n, false) => n :: aList
	case (n, true) => n :: calc_number_list(aNode.parent, aList)
      }
    }

    new LayeredSequenceNumber(calc_number_list(aNode, Nil).reverse)
  }
}
