package org.goldenport.entities.graphviz

import scala.collection.mutable.ArrayBuffer
import com.asamioffice.goldenport.text.TextBuilder

/*
 * Jan. 14, 2009
 * Mar. 18, 2009
 * ASAMI, Tomoharu
 */
class GVDigraph {
  val defaultGraphAttributes = new GVGraphAttributes
  val defaultNodeAttributes = new GVNodeAttributes
  val defaultEdgeAttributes = new GVEdgeAttributes
  val elements = new ArrayBuffer[GVElement]
  val edges = new ArrayBuffer[GVEdge]

  final def write(out: TextBuilder) {
    out.println("digraph {")
    out.indentUp()
    defaultGraphAttributes.write(out)
    defaultNodeAttributes.write(out)
    defaultEdgeAttributes.write(out)
    elements.foreach(_.write(out))
    edges.foreach(_.write(out))
    out.indentDown()
    out.println("}")
    out.flush()
  }
}

class GVGraphAttributes extends GVAttributeHolder {
  final def write(out: TextBuilder) {
    if (!is_attributes_avaliable) return
    out.print("graph")
    print_attributes(out)
    out.println(";")
  }
}

class GVNodeAttributes extends GVAttributeHolder {
  final def write(out: TextBuilder) {
    if (!is_attributes_avaliable) return
    out.print("node")
    print_attributes(out)
    out.println(";")
  }
}

class GVEdgeAttributes extends GVAttributeHolder {
  final def write(out: TextBuilder) {
    if (!is_attributes_avaliable) return
    out.print("edge")
    print_attributes(out)
    out.println(";")
  }
}
