package org.goldenport.values

/*
 * Nov. 24, 2008
 * Nov. 24, 2008
 */
class LayeredSequenceNumber(val numbers: Seq[Int]) {
  override def toString = {
    numbers.mkString(".")
  }
}

object NullLayeredSequenceNumber extends LayeredSequenceNumber(Nil)
