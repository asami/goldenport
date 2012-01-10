package org.goldenport.entities.graphviz

import com.asamioffice.text.TextBuilder

/*
 * Jan. 14, 2009
 * Jan. 29, 2009
 */
class GVEdge(val fromId: String, val fromPort: String, val toId: String, val toPort: String) extends GVAttributeHolder {
  require (fromId != null && fromPort != null && toId != null && toPort != null)

  final def write(out: TextBuilder) {
    out.print(fromId)
    if (fromPort != "") {
      out.print(":")
      out.print(fromPort)
    }
    out.print(" -> ")
    out.print(toId)
    if (toPort != "") {
      out.print(":")
      out.print(toPort)
    }
    print_attributes(out)
    out.println(";")
  }
}
