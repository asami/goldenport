package org.goldenport.entity.content

import java.io.InputStream
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.datasource.BinaryDataSource
import org.goldenport.entities.workspace.WorkspaceBag

/*
 * since Aug.  7, 2004
 *
 * @since   Jan. 15, 2009
 *  version Jul. 15, 2010
 *  version Nov. 13, 2011
 *  version Dec.  5, 2011
 * @version Jan. 20, 2012
 * @author  ASAMI, Tomoharu
 */
class BinaryContent(val binary: BinaryDataSource, aContext: GEntityContext) extends GDataSourceContent(binary, aContext) {
  def this(aBinary: Array[Byte], aContext: GEntityContext) = this(BinaryDataSource.createBinary(aBinary, aContext), aContext)
  def this(aIn: InputStream, aContext: GEntityContext) = this(BinaryDataSource.createInputStream(aIn, aContext), aContext)
  def this(bag: WorkspaceBag, aContext: GEntityContext) = this(BinaryDataSource.createWorkspaceBag(bag, aContext), aContext)

  final def size = binary.size
}

object BinaryContent {
  def apply(in: InputStream, ctx: GEntityContext, name: String, mimetype: String) = {
    val ds = BinaryDataSource.createInputStream(in, ctx, name, mimetype)
    new BinaryContent(ds, ctx)
  }

  def apply(binary: Array[Byte], ctx: GEntityContext, name: String, mimetype: String) = {
    val ds = BinaryDataSource.createBinary(binary, ctx, name, mimetype)
    new BinaryContent(ds, ctx)
  }
}
