package org.goldenport.entities.smartdoc.builder

import org.goldenport.sdoc._
import org.goldenport.entity.GEntityContext
import org.goldenport.value.{PlainTree, GTreeNode}
import org.goldenport.value.GTable
import org.goldenport.entities.smartdoc._

/*
 * derived from SmartDocMakerModel.java since Sep. 27, 2005
 *
 * @since   Sep.  6, 2008
 * @version Oct. 20, 2008
 * @author  ASAMI, Tomoharu
 */
class SmartDocBuilder(val context: GEntityContext) {
  private val _tree = new PlainTree[SBNode]
  private var _current = _tree.root
  private var _is_empty_erase = true

  var name: String = null
  var title: SDoc = SEmpty

  final def getCursor: Cursor = new Cursor

  final def make: SmartDocEntity = {
    val maker = new ChapterSmartDocMaker(context) // XXX
    maker.setName(name)
    maker.setTitle(title)
    _tree.traverse(maker)
    maker.doc
  }

  class Cursor {
    final def enterDivision(aTitle: SDoc): DivisionNode = {
      require(aTitle != null && !aTitle.isNil)
      val division = DivisionNode(aTitle)
      _current = _current.addContent(division)
      division
    }

    final def leaveDivision() {
      val current: GTreeNode[SBNode] = _current
      _current = _current.parent
      if (_is_empty_erase) {
	if (current.isEmpty) _current.removeChild(current)
      }
    }

    final def addDescription(aDescription: SDoc): DescriptionNode = {
      require(aDescription != null)
      val desc = DescriptionNode(aDescription)
      if (!aDescription.isNil) {
        _current.addContent(desc)
      }
      desc
    }

    final def addTable(aTable: GTable[SDoc]): TableNode = {
      require(aTable != null)
      val table = TableNode(aTable)
      _current.addContent(table)
      table
    }
  }
}

