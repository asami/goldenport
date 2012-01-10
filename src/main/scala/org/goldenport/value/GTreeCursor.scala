package org.goldenport.value

/*
 * Nov.  8, 2008
 * Jan.  7, 2009
 */
class GTreeCursor[E](aNode: GTreeNode[E]) {
  private var _current: GTreeNode[E] = aNode

  def this(aTree: GTree[E]) = this(aTree.root)

  final def enter(content: E) {
    _current = _current.addContent(content)
  }

  final def leave(content: E) {
    require (_current.content == content)
    _current = _current.parent
  }

  final def leave() {
    _current = _current.parent
  }

  final def add(content: E) {
    _current.addContent(content)
  }

  final def node = _current
  final def content = _current.content
}
