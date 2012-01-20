package com.asamioffice.goldenport.xml

/*
 * Sep.  9, 2008
 * Sep. 11, 2008
 */
object PrefixedName {
  def apply(prefix: String, label: String) = prefix + ":" + label
  def unapply(name: String): Option[(String, String)] = {
    val parts = name.split(":")
    if (parts.length == 2) Some((parts(0), parts(1))) else None
  }
}


