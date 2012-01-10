package org.goldenport.entities.csv

import java.io._
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.GContentDataSource
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.value.GTableBase
import com.asamioffice.text.ReaderCsvTableMaker;

/*
 * Aug.  6, 2008
 * Jan. 29, 2009
 */
class CsvEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GTableEntity[String](aIn, aOut, aContext) with GTableBase[String] {
  type DataSource_TYPE = GDataSource

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override def is_Text_Output = true
  override def table_Update() = set_dirty()

  override def entity_Locator: EntityLocator = new EntityLocator(this) {}

  override protected def open_Entity_Create() {
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    val reader = aDataSource.openBufferedReader()
    val maker = new ReaderCsvTableMaker(reader);
    var line = maker.readLine()
    var y = 0
    while (line != null) {
      for (x <- 0 until line.length) {
	put(x, y, line(x))
      }
      line = maker.readLine()
      y += 1
    }
  }

  override protected def write_Content(out: BufferedWriter): Unit = {
    if (width == 0) return
    for (y <- 0 until height) {
	write_node(get(0, y), out)
      for (x <- 1 until width) {
	out.write(',')
	write_node(get(x, y), out)
      }
      out.append(entityContext.newLine)
    }
  }

  private def write_node(element: String, out: BufferedWriter) {
    if (element == null) return
    if (element.indexOf(',') == -1 &&
        element.indexOf('"') == -1 &&
        element.indexOf('\n') == -1 &&
        element.indexOf('\r') == -1 &&
        element.indexOf('\t') == -1) {
          out.append(element);
    } else {
      out.append('"');
      for (c <- element.toCharArray()) {
        if (c == '"') {
          out.append("\"\"");
/*
	} else if (c == '\n') {
	  out.append("\\n");
	} else if (c == '\r') {
	  out.append("\\r");
	} else if (c == '\t') {
	  out.append("\\t");
*/  
        } else {
          out.append(c);
        }
      }
      out.append('"');
    }
  }
}

class CsvEntityClass extends GEntityClass {
  type Instance_TYPE = CsvEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "csv"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new CsvEntity(aDataSource, aContext))
}

object CsvEntity extends CsvEntityClass
