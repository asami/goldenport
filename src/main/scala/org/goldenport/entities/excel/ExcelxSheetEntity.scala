package org.goldenport.entities.excel

import java.io._
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.GContentDataSource
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTableBase
import com.asamioffice.goldenport.text.ReaderCsvTableMaker;
import org.apache.poi.hssf.usermodel._

/**
 * derived from ExcelSheetModel (Aug. 12, 2005)
 * 
 * @since   Nov. 30, 2011
 * @version Jun. 19, 2012
 * @author  ASAMI, Tomoharu
 */
class ExcelxSheetEntity(sheet: HSSFSheet, val book: ExcelxBookEntity,
    properties: Map[String, AnyRef], aContext: GEntityContext)
    extends GTableEntity[AnyRef](null, null, aContext) with GTableBase[AnyRef] {
  type DataSource_TYPE = GDataSource
  override def table_Update() = set_dirty()
  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  private val _workbook = book.workbook
  private val _sheet = if (sheet != null) sheet else _workbook.createSheet()

  private def _sheet_name = {
    _workbook.getSheetName(_sheet_number)
  }

  private def _sheet_number: Int = {
    val length = _workbook.getNumberOfSheets();
    for (i <- 0 to length) {
      val target = _workbook.getSheetAt(i);
      if (target == _sheet) {
        return i;
      }
    }
    throw new IllegalStateException("Illega sheet number")
  }

  def this(book: ExcelxBookEntity, properties: Map[String, AnyRef], aContext: GEntityContext) = this(null, book, properties, aContext)

  def this(sheet: HSSFSheet, book: ExcelxBookEntity, aContext: GEntityContext) = this(sheet, book, null, aContext)

  def this(book: ExcelxBookEntity, aContext: GEntityContext) = this(book, null, aContext)

  override protected def open_Entity_Create() {
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    import org.apache.poi.ss.usermodel.Cell
    name = _sheet_name
    var nRows = _sheet.getPhysicalNumberOfRows();
    var y = 0;
    while (nRows > 0) {
      val row = _sheet.getRow(y)
      if (row != null) {
        nRows -= 1
        var nColumns = row.getPhysicalNumberOfCells() 
        var x = 0
        while (nColumns > 0) {
          val cell = row.getCell(x);
          if (cell != null) {
            nColumns -= 1
            cell.getCellType() match {
              case Cell.CELL_TYPE_NUMERIC => {
                val numericValue: Double = cell.getNumericCellValue()
                put(x, y, numericValue.asInstanceOf[AnyRef]) // XXX non dirty
              }
              case Cell.CELL_TYPE_FORMULA => {
                val numericValue: Double = cell.getNumericCellValue() // XXX
                put(x, y, numericValue.asInstanceOf[AnyRef]) // XXX non dirty
              }
              case Cell.CELL_TYPE_STRING => {
                val stringValue = cell.getStringCellValue()
                put(x, y, stringValue) // XXX non dirty
              }
              case Cell.CELL_TYPE_BLANK => {
              }
              case Cell.CELL_TYPE_BOOLEAN => {
                val booleanValue = cell.getBooleanCellValue()
                put(x, y, booleanValue.asInstanceOf[AnyRef]) // XXX non dirty
              }
              case Cell.CELL_TYPE_ERROR => {}
              case _ => {}
            }
            x += 1
          }
        }
        y += 1
      }
    }
  }

  override protected def write_Content(out: BufferedWriter): Unit = {
    _flush_table()
  }

  protected def _flush_table() {
/*
    try {
      val sheetName = name
      val currentName = _sheet.getSheetName()
      if (!currentName.equals(sheetName)) {
        _workbook.setSheetName(_sheet_number, sheetName) // , HSSFWorkbook.ENCODING_UTF_16)
      }
//      val width = getWidth()
//      val height = getHeight()
      val spans = new Array[Int](width)
      val array = new PlainTable[AnyRef]()
      val emptyRows = new Array[Boolean](height)
      for (y <- 0 to height) {
        emptyRows(y) = true
        for (x <- 0 to width) {
          val content = get(x, y)
          if (content != null) {
            val string = content.toString()
            array.put(x, y, string)
            val length = string.length()
            spans(x) = Math.max(spans(x), length)
            emptyRows(y) = false
          }
        }
      }
      for (x <- 0 to width) {
        val span = calcColumnWidth_(spans[x])
        if (span > 0) {
          _sheet.setColumnWidth(x, calcColumnWidth_(span))
        }
//              _sheet.setColumnWidth(x, _sheet.getColumnWidth(x))
      }
      for (y <- 0 to height) {
        val row = _sheet.getRow(y)
        if (emptyRows(y)) {
          if (row != null) {
            _sheet.removeRow(row)
          }
          continue
        }
        if (row == null) {
          row = _sheet.createRow(y)
        }
        for (x <- 0 to width) {
          val value = array.get(x, y)
          var cell = row.getCell(x)
          if (value != null) {
            if (cell == null) {
              cell = row.createCell(x)
            }
            cell.setEncoding(HSSFCell.ENCODING_UTF_16)
            cell.setCellValue(value)
            //
            HSSFCellStyle style = workbook_.createCellStyle()
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN)
            style.setBottomBorderColor(HSSFColor.BLACK.index)
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN)
            style.setLeftBorderColor(HSSFColor.BLACK.index)
            style.setBorderRight(HSSFCellStyle.BORDER_THIN)
            style.setRightBorderColor(HSSFColor.BLACK.index)
            style.setBorderTop(HSSFCellStyle.BORDER_THIN)
            style.setRightBorderColor(HSSFColor.BLACK.index)
            //
            val attr = getAttribute(x, y)
            if (attr != null) {
              val rowspan = attr.getEffectiveRowSpan()
              val colspan = attr.getEffectiveColSpan()
              if (! (rowspan == 1 && colspan == 1)) {
                _sheet.addMergedRegion(new Region(y, x, y + rowspan - 1, (short)(x + colspan - 1)))
              }
              //
              val halign = attr.hAlign
              if (halign != null) {
                switch (halign) {
                  case LEFT => {
                    style.setAlignment(HSSFCellStyle.ALIGN_LEFT)
                  }
                  case CENTER => {
                    style.setAlignment(HSSFCellStyle.ALIGN_CENTER)
                  }
                  case RIGHT => {
                    style.setAlignment(HSSFCellStyle.ALIGN_RIGHT)
                  }
                }
              }
              val valign = attr.vAlign
              if (valign != null) {
                switch (valign) {
                  case TOP => {
                    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP)                      
                  }
                  case MIDDLE => {
                    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER)
                  }
                  case BOTTOM => {
                    style.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM)
                  }
                }
              }
              val bgColor = attr.backgroundColor
              if (bgColor != null) {
//                                 style.setFillForegroundColor((short)bgColor.getRGB())
                style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index) // XXX
                style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND)
              }
              val fontWeight = attr.fontWeight
              if (fontWeight != null || attr.fontStyle != null) {
                val font = workbook_.createFont()
                if (fontWeight != null) { 
                  switch (fontWeight) {
                    case BOLD => {
                      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD)
                    }
                  }
                }
                if (attr.fontStyle != null) {
                  switch (attr.fontStyle) {
                    case ITALIC => font.setItalic(true)
                  }
                  style.setFont(font)
                }
              }
            }
            //
            cell.setCellStyle(style)
          } else {
            if (cell != null) {
              row.removeCell(cell)
            }
          }
        }
      }
    } finally {
    }
*/
  }
}
