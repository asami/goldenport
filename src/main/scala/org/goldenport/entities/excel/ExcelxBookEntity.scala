package org.goldenport.entities.excel

import java.io.OutputStream
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream
import scala.collection.mutable.LinkedHashMap

/**
 * @since   Jun. 12, 2012
 * @version Jul. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class ExcelxBookEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTableListEntity[ExcelxSheetEntity](aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource

  private var _workbook: XSSFWorkbook  = null;
  private val _sheets = new LinkedHashMap[String, ExcelxSheetEntity]()

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
        _workbook = new XSSFWorkbook()
      } else {
        in = ds.openInputStream()
        _workbook = new XSSFWorkbook(in)
        val nSheets = _workbook.getNumberOfSheets()
        for (i <- 0 until nSheets) {
          val sheet = new ExcelxSheetEntity(_workbook.getSheetAt(i), this, null, excelContext)
          _sheets += sheet.name -> sheet
        }
      }
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

  override protected def write_Content(anOut: OutputStream): Unit = {
  }

  def workbook = {
    _workbook ensuring(_workbook != null, "ExcelxBookEntity#workbook")
  }

  def firstSheet: Option[ExcelxSheetEntity] = {
    _sheets.values.headOption
  }

  def head: ExcelxSheetEntity = firstSheet.get
/*
    public ExcelxSheetModel getSheetModel(String key) throws RModelException {
        return (ExcelxSheetModel)getModel(key);
        
    }

    public ExcelxSheetModel getFirstSheetModel() throws RModelException {
        return (ExcelxSheetModel)getModel(0);
    }

    public ExcelxSheetModel[] getSheetModels() throws RModelException {
        String[] keys = keySetArray();
        ExcelxSheetModel[] tables = new ExcelxSheetModel[keys.length];
        for (int i = 0;i < keys.length;i++) {
            tables[i] = getSheetModel(keys[i]);
        }
        return tables;
    }

    public ExcelxSheetModel createSheetModel() throws RModelException {
        ExcelxSheetModel sheet = new ExcelxSheetModel(this, _context);
        putModel(sheet.getName(), sheet);
        _setDirty();
        return sheet;
    }

    public ExcelxSheetModel createSheetModel(String tableName) throws RModelException {
        ExcelxSheetModel sheet = new ExcelxSheetModel(tableName, this, _context);
        putModel(sheet.getName(), sheet);
        _setDirty();
        return sheet;
    }
*/
}

class ExcelxBookEntityClass extends GEntityClass {
  type Instance_TYPE = ExcelxBookEntity

  override def accept_Suffix(suffix: String): Boolean = {
    suffix  == "xlsx"
  }

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new ExcelxBookEntity(aDataSource, aContext))
}

object ExcelxBookEntity extends ExcelxBookEntityClass
