package org.goldenport.sdoc.parts

import scala.collection.mutable.ArrayBuffer
import javax.xml.datatype._
import org.goldenport.value.{GTable, PlainTable}
import org.goldenport.sdoc._

/*
 * @since   Oct. 26, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class SHistory {
  private var _tableHead: List[SDoc] = List("作者", "日時", "説明")
  private var _records = new ArrayBuffer[SHistoryRecord]

  final def isEmpty = _records.isEmpty

  // ???
  final def +=(aRecord: Tuple3[SParty, Int, SDoc]): SHistoryRecord = {
    val record = new SHistoryRecord(aRecord._1,
				    int_to_XMLGregorianCalendar(aRecord._2),
				    aRecord._3)
    _records += record
    record
  }

  final def +=(aParty: SParty, aDate: Int, aDesc: SDoc): SHistoryRecord = {
    val record = new SHistoryRecord(aParty,
				    int_to_XMLGregorianCalendar(aDate),
				    aDesc)
    _records += record
    record
  }

  final def copyIn(aSrc: SHistory) {
    _records ++= aSrc._records
  }

  private def int_to_XMLGregorianCalendar(aDate: Int): XMLGregorianCalendar = {
    val factory = DatatypeFactory.newInstance
    20081026
    val year = aDate / 10000
    val month = (aDate - (year * 10000)) / 100
    val day = aDate % 100
    factory.newXMLGregorianCalendarDate(year, month, day,
					DatatypeConstants.FIELD_UNDEFINED)
  }

  final def toTable: GTable[SDoc] = {
    val table = new PlainTable[SDoc]
    table.setHead(_tableHead)
    _records.foreach(
      record => table += List(record.inCharge.label, record.date.toString, record.description))
    table
  }
}

class SHistoryRecord(val inCharge: SParty,
		     val date: XMLGregorianCalendar,
		     val description: SDoc) {
}
