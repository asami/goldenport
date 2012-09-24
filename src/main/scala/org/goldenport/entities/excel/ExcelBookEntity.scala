package org.goldenport.entities.excel

import scalaz._, Scalaz._
import java.io.OutputStream
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.InputStream
import scala.collection.mutable.LinkedHashMap

/**
 * @since   Nov. 29, 2011
 *  version Jun. 19, 2012
 *  version Jul. 21, 2012
 * @version Sep. 25, 2012
 * @author  ASAMI, Tomoharu
 */
class ExcelBookEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTableListEntity[ExcelSheetEntity](aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource

  private var _workbook: HSSFWorkbook  = null;
  private val _sheets = new LinkedHashMap[String, ExcelSheetEntity]()
  private var _activeSheetName: Option[String] = None

  val excelContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override protected def open_Entity_Create() {
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    load_datasource(aDataSource)
  }

  private def load_datasource(ds: GDataSource) {
    var in: InputStream = null
    try {
      if (!ds.isExist) {
        _workbook = new HSSFWorkbook()
      } else {
        in = ds.openInputStream()
        _workbook = new HSSFWorkbook(in)
        val nSheets = _workbook.getNumberOfSheets()
        for (i <- 0 until nSheets) {
          val sheet = new ExcelSheetEntity(_workbook.getSheetAt(i), this, excelContext)
          sheet.open
          _sheets += sheet.name -> sheet
        }
      }
      _activeSheetName = ds.resource.flatMap(_.fragment)
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch {
          case e => {}
        }
      }
    }
  }

  override protected def close_Entity() {
    _sheets.values.foreach(_.close)
  }

  override protected def write_Content(anOut: OutputStream): Unit = {
  }

  def workbook = {
    _workbook ensuring(_workbook != null, "ExcelBookEntity#workbook")
  }

  def firstSheet: Option[ExcelSheetEntity] = {
    _sheets.values.headOption
  }

  def sheet(key: String): Option[ExcelSheetEntity] = {
    _sheets.get(key)
  }
  
  def activeSheet: Option[ExcelSheetEntity] = {
    _activeSheetName.fold(_sheets.get, firstSheet)
  }

  def head: ExcelSheetEntity = firstSheet.get
  def headOption: Option[ExcelSheetEntity] = firstSheet
  def active: ExcelSheetEntity = {
    activeSheet match {
      case Some(s) => s
      case None => {
        _activeSheetName match {
          case Some(n) => throw new NoSuchElementException(entityContext.formatString("シート「%s」がありません。", n))
          case None => {
            if (_sheets.isEmpty)
              throw new NoSuchElementException(entityContext.formatString("シートが空です。"))
            else
              sys.error(entityContext.formatString("シートが空です。"))
          }
        }
      }
    }
  }
  def activeOption: Option[ExcelSheetEntity] = activeSheet

/*
    public ExcelSheetModel getSheetModel(String key) throws RModelException {
        return (ExcelSheetModel)getModel(key);
        
    }

    public ExcelSheetModel getFirstSheetModel() throws RModelException {
        return (ExcelSheetModel)getModel(0);
    }

    public ExcelSheetModel[] getSheetModels() throws RModelException {
        String[] keys = keySetArray();
        ExcelSheetModel[] tables = new ExcelSheetModel[keys.length];
        for (int i = 0;i < keys.length;i++) {
            tables[i] = getSheetModel(keys[i]);
        }
        return tables;
    }

    public ExcelSheetModel createSheetModel() throws RModelException {
        ExcelSheetModel sheet = new ExcelSheetModel(this, _context);
        putModel(sheet.getName(), sheet);
        _setDirty();
        return sheet;
    }

    public ExcelSheetModel createSheetModel(String tableName) throws RModelException {
        ExcelSheetModel sheet = new ExcelSheetModel(tableName, this, _context);
        putModel(sheet.getName(), sheet);
        _setDirty();
        return sheet;
    }
*/
}

class ExcelBookEntityClass extends GEntityClass {
  type Instance_TYPE = ExcelBookEntity

  override def accept_Suffix(suffix: String): Boolean = {
    suffix == "xls"
  }

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new ExcelBookEntity(aDataSource, aContext))
}

object ExcelBookEntity extends ExcelBookEntityClass
