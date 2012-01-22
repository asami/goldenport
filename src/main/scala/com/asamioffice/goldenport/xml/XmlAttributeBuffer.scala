package com.asamioffice.goldenport.xml

import scala.collection.mutable.ArrayBuffer
import scala.xml._

/*
 * Sep. 14, 2008
 * Sep. 16, 2008
 */
class XmlAttributeBuffer {
  private val attr_buffer = new ArrayBuffer[(String, String, Any)]

  final def get(aKey: String): Option[Any] = {
    aKey match {
      case PrefixedName(prefix, label) => sys.error("")
      case _ => get(null, aKey)
    }
  }

  final def get(anUri: String, aLabel: String): Option[Any] = {
    for ((uri, label, value) <- attr_buffer) {
      if (uri == anUri && label == aLabel) return Some(value)
    }
    None
  }

  final def put(aKey: String, aValue: Any): Option[Any] = {
    aKey match {
      case PrefixedName(prefix, label) => sys.error("")
      case _ => put(null, aKey, aValue)
    }
  }

  final def put(anUri: String, aLabel: String, aValue: Any): Option[Any] = {
    for (i <- 0 until attr_buffer.length) {
      val (uri, label, value) = attr_buffer(i)
      if (uri == anUri && label == aLabel) {
	attr_buffer(i) = (anUri, aLabel, aValue)
	return Some(value)
      }
    }
    attr_buffer += ((anUri, aLabel, aValue))
    None
  }

  final def toMetaData(theAttrs: Seq[(String, String, Any)]): MetaData = {
    require(theAttrs != null)
    var attr: MetaData = Null
    for ((uri, label, value) <- theAttrs ++ attr_buffer.reverse) {
      val text = Text(to_string(value))
      attr = if (uri == null) new UnprefixedAttribute(label, text, attr)
	     else new PrefixedAttribute(uri_to_prefix(uri), label, text, attr)
    }
    attr
  }

  private def uri_to_prefix(anUri: String): String = {
    error(anUri)
  }

  private def to_string(value: Any): String = {
    value.toString // XXX
  }
}

