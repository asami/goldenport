package org.goldenport.util

/**
 * @since   May. 23, 2011
 * @version Jul. 13, 2011
 * @author  ASAMI, Tomoharu
 */
object Options {
  def lift[A](o: A): Option[A] = {
    if (o == null) None
    else Some(o)
  }

  def stringLift(o: String): Option[String] = {
    o match {
      case null => None
      case "" => None
      case s if s.trim.isEmpty => None
      case _ => Some(o)
    }
  }
}
