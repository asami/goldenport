package org.goldenport.value

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.matchers._

/*
 * @since   Jul. 27, 2008
 * @version Mar. 19, 2011
 * @author  ASAMI, Tomoharu
 */
@RunWith(classOf[JUnitRunner])
class PlainTableTest extends WordSpec with ShouldMatchers {
  "PlainTable" should {
    "作成" in {
      val width = 5
      val height = 4
      val table = new PlainTable[Int]
      for (y <- 0 until height) {
	for (x <- 0 until width) {
	  table.put(x, y, x * y)
	}
      }
      table.height should equal (height)
      table.width should equal (width)
      for (y <- 0 until table.height) {
	for (x <- 0 until table.width) {
	  println("x:" + x + " y:" + y + " = " + table.get(x, y))
	  table.get(x, y) should equal (x * y)
	}
      }
      println("height:" + table.height + " width:" + table.width)
    }
  }
}
