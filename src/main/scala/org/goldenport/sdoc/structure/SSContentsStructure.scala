package org.goldenport.sdoc.structure

import scala.collection.mutable.ArrayBuffer
import org.goldenport.sdoc._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.attribute._

/*
 * @since   Oct.  5, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
abstract class SSContentsStructure extends SStructure {
  final override protected def normalize_Node(): Boolean = {
    val nodes = new ArrayBuffer[SDoc]
    var stringBuffer: StringBuilder = null
    var nodeBuffer: ArrayBuffer[SDoc] = null
    var state: ((SDoc) => Unit) = null

    def add_line(string: String) {
      add_buffer(string)
    }

    def add_buffer(string: String) {
      if (stringBuffer == null) {
	stringBuffer = new StringBuilder
      }
      stringBuffer.append(string)
    }

    def make_paragraph() {
      if (stringBuffer == null && nodeBuffer == null) {
	// do nothing
      } else if (stringBuffer != null && nodeBuffer == null) {
	nodes += SBParagraph(stringBuffer.toString)
	stringBuffer = null
      } else if (stringBuffer == null && nodeBuffer != null) {
	nodes += SBParagraph(nodeBuffer: _*)
	nodeBuffer = null
      } else if (stringBuffer != null && nodeBuffer != null) {
	nodeBuffer += SText(stringBuffer.toString)
	nodes += SBParagraph(nodeBuffer: _*)
	stringBuffer = null
	nodeBuffer = null
      } else {
	error("Should not reached.")
      }
    }

    def make_inline() {
      if (stringBuffer != null) {
	nodeBuffer += SText(stringBuffer.toString)
	stringBuffer = null
      }
    }

    def add_inline(inline: SInline) {
      make_inline()
      nodeBuffer += inline
    }

    def Init()(child: SDoc): Unit = {
      child match {
	case t: SText => {
	  for (line <- t.toString.linesWithSeparators) {
	    if (is_empty_line(line)) {
	      make_paragraph()
	    } else if (is_complete_line(line)) {
	      add_line(line)
	      // Init or EndOfLine()_
	    } else {
	      add_buffer(line)
	    }
	  }
	}
	case inline: SInline => {
	  add_inline(inline)
	  state = InLine()_
	}
	case block: SBlock => {
	  make_paragraph()
	  nodes += block
//	  state = Init()_
	}
	case structure: SStructure => {
	  make_paragraph()
	  nodes += structure
//	  state = Init()_
	}
	case attribute: SAttribute => // do nothing
	case _ => error("Init:" + child + ", " + child.getClass)
      }
    }

    def InLine()(child: SDoc): Unit = {
      child match {
	case t: SText => {
	  for (line <- t.toString.linesWithSeparators) {
	    if (is_empty_line(line)) {
	      make_paragraph()
	    } else if (is_complete_line(line)) {
	      add_line(line)
	      // Init or EndOfLine()_
	    } else {
	      add_buffer(line)
	    }
	  }
	}
	case inline: SInline => {
	  make_inline()
	  add_inline(inline)
	  state = InLine()_
	}
	case block: SBlock => {
	  make_paragraph()
	  nodes += block
	  state = Init()_
	}
	case structure: SStructure => {
	  make_paragraph()
	  nodes += structure
	  state = Init()_
	}
	case _ => error("InLine")
      }
    }

    state = Init()_
    for (child <- children) { // XXX removeChildren
      state(child)
    }
    make_paragraph()
    clear
    nodes.foreach(addChild(_))
    false
  }

  private def is_empty_line(line: String): Boolean = {
    line == "\n" || line == "\r" || line == "\r\n"
  }

  private def is_complete_line(line: String): Boolean = {
    line.endsWith("\n") || line.endsWith("\r")
  }
}
