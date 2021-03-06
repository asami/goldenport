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
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.InputStream
import org.goldenport.value.GTableBase

/**
 * @since   Dec.  1, 2011
 *  version Dec.  1, 2011
 *  version Jul. 21, 2012
 * @version Sep. 25, 2012
 * @author  ASAMI, Tomoharu
 */
class ExcelTableEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) 
    extends GTableEntity[AnyRef](aIn, aOut, aContext) with GTableBase[AnyRef] {
  type DataSource_TYPE = GDataSource

  val excelContext = new GSubEntityContext(entityContext) {
    override def text_Encoding = Some("UTF-8")
  }
  private var _book: ExcelBookEntity  = null

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
      _book = new ExcelBookEntity(ds, excelContext)
      copyIn(_book.active)
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

/*
  def workbook = {
    _workbook ensuring(_workbook != null, "ExcelTableEntity#workbook")
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

class ExcelTableEntityClass extends GEntityClass {
  type Instance_TYPE = ExcelTableEntity

  override def accept_Suffix(suffix: String): Boolean = {
    suffix == "xls"
  }

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new ExcelTableEntity(aDataSource, aContext))
}

object ExcelTableEntity extends ExcelTableEntityClass
