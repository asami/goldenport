package org.goldenport.util

/**
 * @since   May. 23, 2011
 * @version May. 23, 2011
 * @author  ASAMI, Tomoharu
 */
object Booleans {
  def or(exprs: (() => Boolean)*): Boolean = {
    for (expr <- exprs) {
      if (expr()) return true
    }
    false
  }

  def orElse(exprs: (() => Boolean)*)(elsefunc: => Unit): Boolean = {
    for (expr <- exprs) {
      if (expr()) return true
    }
    elsefunc
    false
  }
}
