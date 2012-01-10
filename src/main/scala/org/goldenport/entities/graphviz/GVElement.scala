package org.goldenport.entities.graphviz

import scala.collection.mutable.LinkedHashMap
import com.asamioffice.text.TextBuilder

/*
 * @since   Jan. 14, 2009
 * Mar. 19, 2009
 * @version Nov.  3, 2011
 * @author  ASAMI, Tomoharu
 */
abstract class GVElement(val id: String) extends GVAttributeHolder {
  def write(out: TextBuilder) {
    out.print(id)
    print_Prologue(out)
    print_attributes(out)
    out.println(";")
  }

  protected def print_Prologue(out: TextBuilder) {}
}

trait GVAttributeHolder {
  var label: String = ""
  var labelfontcolor: String = ""
  var table: String = ""
  var fontname: String = ""
  var fontsize: String = ""
  var fontcolor: String = ""
  var shape: String = ""
  var style: String = ""
  var fixedsize: String = ""
  var width: String = ""
  var height: String = ""
  var color: String = ""
  var fillcolor: String = ""
  var bgcolor: String = ""
  var layout: String = ""
  var root: String = ""
  //
  var arrowhead: String = ""
  var arrowtail: String = ""
  var headlabel: String = ""
  var taillabel: String = ""
  //
  var compound: String = ""
  var lhead: String = ""
  var ltail: String = ""

  protected def is_attributes_avaliable = {
    label != "" ||
    labelfontcolor != "" ||
    table != "" ||
    fontname != "" ||
    fontsize != "" ||
    fontcolor != "" ||
    shape != "" ||
    style != "" ||
    fixedsize != "" ||
    width != "" ||
    height != "" ||
    color != "" ||
    fillcolor != "" ||
    bgcolor != "" ||
    layout != "" ||
    root != "" ||
    arrowhead != "" ||
    arrowtail != "" ||
    compound != "" ||
    lhead != "" ||
    ltail != ""
  }

  protected final def print_attributes(out: TextBuilder) {
    if (!is_attributes_avaliable) return
    var isExists = false
    out.print(" [")
    if (table != "") {
      if (isExists) out.print(", ")
      out.print("label=")
      out.print(table)
      isExists = true
    } else if (label != "") {
      if (isExists) out.print(", ")
      out.print("label=\"")
      out.print(label)
      out.print("\"")
      isExists = true
    }
    if (labelfontcolor != "") {
      if (isExists) out.print(", ")
      out.print("labelfontcolor=\"")
      out.print(labelfontcolor)
      out.print("\"")
      isExists = true
    }
    if (fontname != "") {
      if (isExists) out.print(", ")
      out.print("fontname=\"")
      out.print(fontname)
      out.print("\"")
      isExists = true
    }
    if (fontsize != "") {
      if (isExists) out.print(", ")
      out.print("fontsize=")
      out.print(fontsize)
      isExists = true
    }
    if (fontcolor != "") {
      if (isExists) out.print(", ")
      out.print("fontcolor=\"")
      out.print(fontcolor)
      out.print("\"")
      isExists = true
    }
    if (shape != "") {
      if (isExists) out.print(", ")
      out.print("shape=")
      out.print(shape)
      isExists = true
    }
    if (style != "") {
      if (isExists) out.print(", ")
      out.print("style=")
      out.print("\"")
      out.print(style)
      out.print("\"")
      isExists = true
    }
    if (fixedsize != "") {
      if (isExists) out.print(", ")
      out.print("fixedsize=")
      out.print(fixedsize)
      isExists = true
    }
    if (width != "") {
      if (isExists) out.print(", ")
      out.print("width=")
      out.print(width)
      isExists = true
    }
    if (height != "") {
      if (isExists) out.print(", ")
      out.print("height=")
      out.print(height)
      isExists = true
    }
    if (layout != "") {
      if (isExists) out.print(", ")
      out.print("layout=")
      out.print(layout)
      isExists = true
    }
    if (root != "") {
      if (isExists) out.print(", ")
      out.print("root=")
      out.print(root)
      isExists = true
    }
    if (color != "") {
      if (isExists) out.print(", ")
      out.print("color=")
      out.print("\"")
      out.print(color)
      out.print("\"")
      isExists = true
    }
    if (fillcolor != "") {
      if (isExists) out.print(", ")
      out.print("fillcolor=")
      out.print("\"")
      out.print(fillcolor)
      out.print("\"")
      isExists = true
    }
    if (bgcolor != "") {
      if (isExists) out.print(", ")
      out.print("bgcolor=")
      out.print("\"")
      out.print(bgcolor)
      out.print("\"")
      isExists = true
    }
    if (arrowhead != "") {
      if (isExists) out.print(", ")
      out.print("arrowhead=")
      out.print(arrowhead)
      isExists = true
    }
    if (arrowtail != "") {
      if (isExists) out.print(", ")
      out.print("arrowtail=")
      out.print(arrowtail)
      isExists = true
    }
    if (arrowhead != "" && arrowtail == "") {
      out.print(", dir=forward")
    } else if (arrowhead == "" && arrowtail != "") {
      out.print(", dir=back")
    } else if (arrowhead != "" && arrowtail != "") {
      out.print(", dir=both")
    }
    if (headlabel != "") {
      if (isExists) out.print(", ")
      out.print("headlabel=")
      out.print("\"")
      out.print(headlabel)
      out.print("\"")
      isExists = true
    }
    if (taillabel != "") {
      if (isExists) out.print(", ")
      out.print("taillabel=")
      out.print("\"")
      out.print(taillabel)
      out.print("\"")
      isExists = true
    }
    if (compound != "") {
      if (isExists) out.print(", ")
      out.print("compound=")
      out.print(compound)
      isExists = true
    }
    if (lhead != "") {
      if (isExists) out.print(", ")
      out.print("lhead=")
      out.print(lhead)
      isExists = true
    }
    if (ltail != "") {
      if (isExists) out.print(", ")
      out.print("ltail=")
      out.print(ltail)
      isExists = true
    }
    out.print(']')
  }

  protected final def print_attribute_lines(out: TextBuilder) {
    if (table != "") {
      out.print("label=")
      out.print(table)
      out.println(";")
    } else if (label != "") {
      out.print("label=\"")
      out.print(label)
      out.print("\"")
      out.println(";")
    }
    if (labelfontcolor != "") {
      out.print("labelfontcolor=\"")
      out.print(labelfontcolor)
      out.print("\"")
      out.println(";")
    }
    if (fontname != "") {
      out.print("fontname=\"")
      out.print(fontname)
      out.print("\"")
      out.println(";")
    }
    if (fontsize != "") {
      out.print("fontsize=")
      out.print(fontsize)
      out.println(";")
    }
    if (fontcolor != "") {
      out.print("fontcolor=\"")
      out.print(fontcolor)
      out.print("\"")
      out.println(";")
    }
    if (shape != "") {
      out.print("shape=")
      out.print(shape)
      out.println(";")
    }
    if (style != "") {
      out.print("style=")
      out.print("\"")
      out.print(style)
      out.print("\"")
      out.println(";")
    }
    if (fixedsize != "") {
      out.print("fixedsize=")
      out.print(fixedsize)
      out.println(";")
    }
    if (width != "") {
      out.print("width=")
      out.print(width)
      out.println(";")
    }
    if (height != "") {
      out.print("height=")
      out.print(height)
      out.println(";")
    }
    if (layout != "") {
      out.print("layout=")
      out.print(layout)
      out.println(";")
    }
    if (root != "") {
      out.print("root=")
      out.print(root)
      out.println(";")
    }
    if (color != "") {
      out.print("color=")
      out.print("\"")
      out.print(color)
      out.print("\"")
      out.println(";")
    }
    if (fillcolor != "") {
      out.print("fillcolor=")
      out.print("\"")
      out.print(fillcolor)
      out.print("\"")
      out.println(";")
    }
    if (bgcolor != "") {
      out.print("bgcolor=")
      out.print("\"")
      out.print(bgcolor)
      out.print("\"")
      out.println(";")
    }
    if (arrowhead != "") {
      out.print("arrowhead=")
      out.print(arrowhead)
      out.println(";")
    }
    if (arrowtail != "") {
      out.print("arrowtail=")
      out.print(arrowtail)
      out.println(";")
    }
    if (headlabel != "") {
      out.print("headlabel=")
      out.print("\"")
      out.print(headlabel)
      out.print("\"")
      out.println(";")
    }
    if (taillabel != "") {
      out.print("taillabel=")
      out.print("\"")
      out.print(taillabel)
      out.print("\"")
      out.println(";")
    }
    if (compound != "") {
      out.print("compound=")
      out.print(compound)
      out.println(";")
    }
    if (lhead != "") {
      out.print("lhead=")
      out.print(lhead)
      out.println(";")
    }
    if (ltail != "") {
      out.print("ltail=")
      out.print(ltail)
      out.println(";")
    }
  }
}
