package org.goldenport.entities.graphviz

import scala.collection.mutable.ArrayBuffer
import com.asamioffice.goldenport.text.TextBuilder

/*
 * @since   Jan. 14, 2009
 * @version Mar. 21, 2011
 * @author  ASAMI, Tomoharu
 */
class GVNode(aId: String) extends GVElement(aId) {
  val compartments = new ArrayBuffer[GVCompartment]

  final def addCompartment(aLabel: String): GVCompartment = {
    val compartment = new GVCompartment
    compartment.addLine(aLabel)
    compartments += compartment
    compartment
  }

  override protected def print_Prologue(out: TextBuilder) {
    if (compartments.isEmpty) return
    val buffer = new StringBuilder

    def make_table {
      buffer.append("<table title=\"")
      buffer.append(label)
      buffer.append("\" border=\"0\" cellborder=\"1\" cellspacing=\"0\" cellpadding=\"2\" port=\"p\"")
      if (bgcolor != "") {
	buffer.append(" bgcolor=\"")
	buffer.append(bgcolor)
	buffer.append("\"")
      }
      buffer.append(">")
      for (compartment <- compartments) {
	buffer.append("<tr><td><table border=\"0\" cellspacing=\"0\" cellpadding=\"1\">")
	if (compartment.lines.isEmpty) {
// "<font> </font>" causes segmentation fault in 2.26.3.
//	  buffer.append("<tr><td align=\"center\" balign=\"center\"><font point-size=\"1\"> </font></td></tr>")
	  buffer.append("<tr><td align=\"center\" balign=\"center\"></td></tr>")
	} else {
	  for (line <- compartment.lines) {
	    buffer.append("<tr><td align=\"")
	    buffer.append(line.align)
	    buffer.append("\" balign=\"center\">")
	    buffer.append(line.label)
	    buffer.append("</td></tr>")
	  }
	}
	buffer.append("</table></td></tr>")
      }
      buffer.append("</table>")
    }

    def make_record { // XXX
      def make_compartment(aComp: GVCompartment) {
	if (aComp.lines.isEmpty) return
	buffer.append(aComp.lines.first.label)
	for (line <- aComp.lines.drop(1)) {
	  buffer.append("\\n")
	  buffer.append(line.label)
	}
      }

      if (compartments.isEmpty) return
      if (compartments.length == 1) {
	make_compartment(compartments.first)
      } else {
	buffer.append("{")
	make_compartment(compartments.first)
	for (compartment <- compartments.drop(1)) {
	  buffer.append("|")
	  make_compartment(compartment)
	}
	buffer.append("}")
      }
    }

    if (shape == "ellipse") {
      buffer.append("\"")
      make_record
      buffer.append("\"")
    } else {
      buffer.append("<")
      make_table
      buffer.append(">")
    }
    table = buffer.toString
  }
}
