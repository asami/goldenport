package org.goldenport.entities.graphviz

import java.io._
import org.goldenport.entity._
import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.datasource.GContentDataSource
import com.asamioffice.text.{AppendableTextBuilder, StringTextBuilder}

/*
 * Jan. 14, 2009
 * Jan. 27, 2009
 */
class GraphvizEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GEntity(aIn, aOut, aContext) {
  type DataSource_TYPE = GDataSource

  var graph: GVDigraph = null

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  override def is_Text_Output = true

  override protected def open_Entity_Create() {
    graph = new GVDigraph
  }

  override protected def open_Entity_Create(aDataSource: GDataSource) {
    error("not implemented yet")
  }

  override protected def open_Entity_Update(aDataSource: GDataSource) {
    error("not implemented yet")
  }

  override protected def write_Content(out: BufferedWriter) {
    val builder = new AppendableTextBuilder(out)
    graph.write(builder)
    builder.flush()
  }

  final def toDotText: String = {
    val builder = new StringTextBuilder
    graph.write(builder)
    builder.toString
  }
}

class GraphvizEntityClass extends GEntityClass {
  type Instance_TYPE = GraphvizEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "dot"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new GraphvizEntity(aDataSource, aContext))
}
