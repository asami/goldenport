package org.goldenport.sdoc.block

import scala.xml._
import org.goldenport.value.{GTable, PlainTable}
import org.goldenport.sdoc._

/*
 * derived from SBTable.java since Apr. 6, 2007
 *
 * Sep.  6, 2008
 * Oct. 21, 2008
 */
case class SBTable extends SBlock {
  val table: GTable[SDoc] = new PlainTable[SDoc]

  override protected def normalize_Node(): Boolean = {
    def build_table_content(aNode: SDoc) {
      def build_thead(aTHead: SBTableHead) {
	def build_tr_thead(aTr: SBTableRecord) {
	  val head = new PlainTable[SDoc]
	  val cursor = head.rowCursor

	  def build_th(aTh: SBTableHeader) {
	    // XXX attribute
	    cursor += aTh.toText
	  }

	  def build_td(aTd: SBTableData) {
	    cursor += aTd.toText
	  }

	  def build_text(aText: SText) {
	    // do nothing
	  }

	  for (content <- aTr.children) {
	    content match {
	      case th: SBTableHeader => build_th(th)
	      case td: SBTableData => build_td(td)
	      case text: SText => build_text(text)
	      case _ => throw new SSyntaxErrorException("Unsupported node = " + aTr)
	    }
	  }
	  table.setHead(head)
	}

	for (content <- aTHead.children if (content.isInstanceOf[SBTableRecord])) {
	  build_tr_thead(content.asInstanceOf[SBTableRecord])
	}
	// XXX
      }

      def build_tbody(aTBody: SBTableBody) {
	for (content <- aTBody.children if (content.isInstanceOf[SBTableRecord])) {
	  build_tr(content.asInstanceOf[SBTableRecord])
	}
      }

      def build_tfoot(aTFoot: SBTableFoot) {
	// XXX
      }

      def build_tr(aTr: SBTableRecord) {
	val cursor = table.rowCursor

	def build_th(aTh: SBTableHeader) {
	  // XXX attribute
	  cursor += aTh.toContents
	}

	def build_td(aTd: SBTableData) {
	  cursor += aTd.toContents
	}

	def build_text(aText: SText) {
	  // do nothing
	}

	for (content <- aTr.children) {
	  content match {
	    case th: SBTableHeader => build_th(th)
	    case td: SBTableData => build_td(td)
	    case text: SText => build_text(text)
	    case _ => throw new SSyntaxErrorException("Unsupported node = " + aTr)
	  }
	}
      }

      def build_text(aText: SText) {
	// do nothing
      }

      aNode match {
	case thead: SBTableHead => build_thead(thead)
	case tbody: SBTableBody => build_tbody(tbody)
	case tfoot: SBTableFoot => build_tfoot(tfoot)
	case tr: SBTableRecord => build_tr(tr)
	case text: SText => build_text(text)
	case _ => throw new SSyntaxErrorException("Unsupported node = " + aNode)
      }
    }

    children.foreach(build_table_content)
    true
  }

  override def xml_Node(): Elem = USmartDoc.makeTable(table)
}

object SBTableXml {
  def unapply(xnode: Node): Option[SBTable] = {
    if (xnode.label == "table") {
      val table = SBTable()
      table.putXmlAttributes(xnode.attributes)
      Some(table)
    } else None
  }
}

object SBTableString {
  def unapply(string: String): Option[(SBTable, Option[String], Option[String])] = None
}
