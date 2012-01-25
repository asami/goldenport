package org.goldenport.value.util

import scala.collection.mutable.ArrayBuffer
import org.goldenport.value.{GTabular, GTableBase, PlainTable}

/*
 * @since   Feb.  3, 2009
 *  version Jul. 15, 2010
 * @version Jan. 24, 2012
 * @author  ASAMI, Tomoharu
 */
class AnnotatedCsvTabular(val csv: GTabular[String]) extends GTableBase[String] {
  val annotations = new PlainTable[Annotation]
  val prologueData = new PlainTable[String]

  final def build() {
    build_Prologue
    build_data
    build_Epilogue
  }

  protected def build_Prologue() {}
  protected def build_Epilogue() {}

  private def build_data() {
    val current = new ArrayBuffer[Annotation]

    def build_line(line: Seq[String]) {
      if (line.isEmpty) return
      if (line(0).startsWith("#")) {
      	build_annotations(line)
      } else {
        build_data(line)
      }
    }

    def build_data(line: Seq[String]) {
//      println("AnnotatedCsvTabular = " + line) 2009-02-10
      append(line)
      annotations.append(current)
    }

    def build_annotations(aLine: Seq[String]) {
      def get_key_value(aCell: String): (String, String) = {
        if (aCell == null) return null
        if (aCell == "#") return ("", "")
        try {
          val regex = """#?(\w+)\W*=\W*(\w+)\W*""".r
          val regex(key, value) = aCell
          (key, value)
        } catch {
          case e: MatchError => {
            try {
              val regex = """#?(\w+)\W*""".r
              val regex(key) = aCell
              (key, "")
            } catch {
            case ee: MatchError => sys.error("illegal cell = " + aCell)
            }
          }
        }
      }

      for (i <- 0 until aLine.length) {
        val keyValue = get_key_value(aLine(i))
        val attr = new Annotation(keyValue._1, keyValue._2)
        if (current.length > i) {
          current(i) = attr
        } else {
          current += attr
        } 
      }
    }

    prologueData.terseRows.foreach(build_line)
    csv.terseRows.foreach(build_line)
  }

  final def dump() {
    for (y <- 0 until height) {
      for (x <- 0 until width) {
	def print_annotation {
	  print("[")
	  annotations.get(x, y) match {
	    case null => print("N/A")
	    case anno => print(anno.key)
	  }
	  print("]")
	}

	if (x != 0) {
	  print(",")
	}
	get(x, y) match {
	  case null => {
	    print("N/A")
	    print_annotation
	  }
	  case data => {
	    print(get(x, y).toString)
	    print_annotation
	  }
	}
      }
      println()
    }
  }

  class Annotation(val key: String, value: String) {
  }
}
