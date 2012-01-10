package com.asamioffice.text

import java.io.Flushable

/*
 * @since   May.  5, 2009
 * @version May.  7, 2009
 * @author  ASAMI, Tomoharu
 */
class XmlTextMaker extends TextMaker {
  def xmlNs(prefix: String, namespace: String)(content: => Unit) {
  }

  def element(qname: String)(content: => Unit) {
    print("<")
    print(qname)
    val checkpoint1 = checkpoint
    print(">")
    val checkpoint2 = checkpoint
    content
    if (isModified(checkpoint2)) {
      print("</")
      print(qname)
      print(">")
    } else {
      rollback(checkpoint1)
      print("/>")
    }
  }
}
