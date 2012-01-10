package org.goldenport.entities.smartdoc.builder

import org.goldenport.sdoc._
import org.goldenport.entity.GEntityContext
import org.goldenport.value.{PlainTree, GTreeNode}
import org.goldenport.value.GTable
import org.goldenport.entities.smartdoc.SmartDocRealmEntity

/*
 * XXX unify SmartDocBuilder
 *
 * @since   Oct.  7, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class SmartDocRealmBuilder(val context: GEntityContext) {
  private val _tree = new PlainTree[SBNode]
  private var _current = _tree.root
  private var _is_empty_erase = true

  final def getCursor: Cursor = new Cursor

  final def make: SmartDocRealmEntity = {
    val maker = new SmartDocRealmMaker(context)
    _tree.traverse(maker)
    maker.realm
  }

  class Cursor {
    final def enterTopic(aTitle: SDoc, aName: String): TopicNode = {
      require(aTitle != null)
      val topic = TopicNode(aTitle, aName)
      _current = _current.addContent(topic)
      topic
    }

    final def leaveTopic() {
      _current = _current.parent
    }

    final def enterPage(aTitle: SDoc, aName: String): PageNode = {
      require(aTitle != null)
      val page = PageNode(aTitle, aName)
      _current = _current.addContent(page)
      page
    }

    final def leavePage() {
      _current = _current.parent
    }

    // unify SmartDocBuilder
    final def enterDivision(aTitle: SDoc): DivisionNode = {
      require(aTitle != null)
      val division = DivisionNode(aTitle)
      _current = _current.addContent(division)
      division
    }

    // unify SmartDocBuilder
    final def leaveDivision() {
      val current: GTreeNode[SBNode] = _current
      _current = _current.parent
      if (_is_empty_erase) {
	if (current.isEmpty) _current.removeChild(current)
      }
    }

    // unify SmartDocBuilder
    final def addDescription(aDescription: SDoc): DescriptionNode = {
      require(aDescription != null)
      val desc = DescriptionNode(aDescription)
      if (!aDescription.isNil) {
        _current.addContent(desc)
      }
      desc
    }

    // unify SmartDocBuilder
    final def addTable(aTable: GTable[SDoc]): TableNode = {
      require(aTable != null)
      val table = TableNode(aTable)
      if (aTable.height > 0) {
	_current.addContent(table)
      }
      table
    }
  }
}

