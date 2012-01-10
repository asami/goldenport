package com.asamioffice.text

import scala.collection.mutable.ArrayBuffer

/*
 * Feb. 27, 2009
 * Feb. 27, 2009
 */
object CsvUtility {
  def makeItems(value: String): Seq[String] = {
    val items = new ArrayBuffer[String]
    var start = 0
    var state = "init"
    for (i <- 0 until value.length) {
      val c = value.charAt(i)
      state match {
	case "init" => {
	  if (c == ';' || c == ',' || c == ' ') {
	    state = "init"
	  } else {
	    state = "name"
	    start = i
	  }
	}
	case "name" => {
	  if (c == ' ') {
	    state = "space_after_name"
	  } else if (c == ';' || c == ',') {
	    items += value.substring(start, i)
	    state = "init"
	  } else if (c == '(') {
	    state = "labels"
	  } else {
	    state = "name"
	  }
	}
	case "space_after_name" => {
	  if (c == ' ') {
	    state = "space_after_name"
	  } else if (c == ';' || c == ',') {
	    items += value.substring(start, i).trim
	    state = "init"
	  } else if (c == '(') {
	    state = "labels"
	  } else {
	    state = "name"
	  }
	}
	case "labels" => {
	  if (c == ')') {
	    items += value.substring(start, i + 1).trim
	    state = "init"
	  }
	}
	case _ => error("bad state = " + state)
      }
    }
    if (state == "name") {
      items += value.substring(start)
    }
    items
  }

  def makeNameMark(value: String): (String, Option[String]) = {
    (getName(value),
     getMark(value))
  }

  def makeNameLabelsMark(value: String): (String, Seq[String], Option[String]) = {
    (getName(value),
     getLabels(value),
     getMark(value))
  }

  def getName(value: String): String = {
    val index = value.indexOf('(')
    if (index != -1) return value.substring(0, index)
    getMark(value) match {
      case Some(mark) => value.substring(0, value.length - mark.length)
      case None => value
    }
  }

  def getLabels(value: String): Seq[String] = {
    val start = value.indexOf('(')
    if (start == -1) return Nil
    val end = value.indexOf(')')
    if (end == -1) error("syntax error: = " + value) // XXX
    value.substring(start + 1, end).split("[;. ]+")
  }

  def getMark(value: String): Option[String] = {
    val regex = """[\p{Blank}\p{Punct}&&[^();. ]]+""".r
    regex.findFirstIn(value)
  }
}
