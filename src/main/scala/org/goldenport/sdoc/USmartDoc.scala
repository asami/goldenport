package org.goldenport.sdoc

import scala.xml._
import org.goldenport.value.GTable

/*
 * Sep.  1, 2008
 * Jan. 26, 2009
 */
object USmartDoc {
  def makeBody(string: String, prefix: String): (Option[String], Option[String]) = 
    makeBody(string, prefix, prefix)

  def makeBody(string: String, prefix: String, postfix: String): (Option[String], Option[String]) = {
    require (prefix.length == 1 && postfix.length == 1)
    require (string.startsWith(prefix))
    make_body(string, prefix(0), postfix(0)) ensuring (_ != null)
  }

  def makeBody(string: String, prefix: Char): (Option[String], Option[String]) = 
    makeBody(string, prefix, prefix)

  def makeBody(string: String, prefix: Char, postfix: Char): (Option[String], Option[String]) = {
    require (string(0) == prefix)
    make_body(string, prefix, postfix) ensuring (_ != null)
  }

  def make_body(string: String, prefix: Char, postfix: Char): (Option[String], Option[String]) = {
    var done = false
    var index = 1
    var start = 1
    var end = 1
    var tail = -1
    var boldCount = 0
    var italicCount = 0
    var anchorCount = 0
    while (!done) {
      if (index == string.length) {
//	end = index
//	done = true
	throw new SSyntaxErrorException("unmatch character (" + postfix + ")")
      } else {
	val c = string(index)
	if (c == postfix && boldCount == 0 && italicCount == 0 && anchorCount == 0) {
	  end = index
	  done = true
	  if (index + 1 != string.length) {
	    tail = index + 1
	  }
	} else {
	  if (is_bold_start(c)) {
	    boldCount += 1
	  } else if (is_bold_end(c)) {
	    boldCount -= 1
	  } else if (is_italic_start(c)) {
	    italicCount += 1
	  } else if (is_italic_end(c)) {
	    italicCount -= 1
	  } else if (is_anchor_start(c)) {
	    anchorCount += 1
	  } else if (is_anchor_end(c)) {
	    anchorCount -= 1
	  }
	  index += 1
	}
      }
    }
    if (boldCount > 0) {
      throw new SSyntaxErrorException("unmatch bold character (*)")
    } else if (italicCount > 0) {
      throw new SSyntaxErrorException("unmatch italic character (_)")
    } else if (anchorCount > 0) {
      throw new SSyntaxErrorException("unmatch anchor charactor ([])")
    } else {
      val body = string.substring(start, end)
      (if (body.length > 0) Some(body) else None,
       if (tail != -1) Some(string.substring(tail)) else None)
    }
  }

  def is_bold_start(c: Char) = c =='*'
  def is_bold_end(c: Char) = c =='*'
  def is_italic_start(c: Char) = c =='_'
  def is_italic_end(c: Char) = c =='_'
  def is_anchor_start(c: Char) = c =='['
  def is_anchor_end(c: Char) = c ==']'

  def isSpecialChar(c: Char): Boolean = {
    c match {
      case '*' | '_' | '[' | ']' => true
      case _ => false
    }
  }

  //
  def makeTable(aTable: GTable[SDoc]): Elem = {
    Elem(null, "table", table_attrs, TopScope, table_contents(aTable): _*)
  }

  private def table_attrs: MetaData = Null
  private def table_contents(aTable: GTable[SDoc]): Seq[Node] = {
    val mayColumns = aTable.columnNames // XXX tree
    val mayRows = aTable.rowNames // XXX tree
    // XXX side
    if (mayColumns.isEmpty) {
      <tbody>{tbody_contents(aTable)}</tbody>
    } else {
      List(<thead>{thead_contents(mayColumns)}</thead>,
	   <tbody>{tbody_contents(aTable)}</tbody>)
    }
  }

  private def thead_contents(theColumns: Seq[SDoc]): Seq[Node] = {
    <tr>{
      theColumns.map(column => <th>{ column_contents(column) }</th>)
    }</tr>
  }

  private def tbody_contents(aTable: GTable[SDoc]): Seq[Node] = {
    for (row <- aTable.rows) yield {
      <tr>{
	for (column <- row) yield {
	  column_contents(column)
	}
      }</tr>
    }
  }

  private def column_contents(aColumn: SDoc): Seq[Node] = {
    aColumn match {
      case e:SEmpty => <td></td>
      case f:SFragment => <td>{ f.children }</td>
      case c => <td>{ c }</td>
    }
  }
}
