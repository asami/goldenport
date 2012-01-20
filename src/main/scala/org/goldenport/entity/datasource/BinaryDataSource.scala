package org.goldenport.entity.datasource

import scalaz._
import Scalaz._
import java.io._
import java.net.URI
import org.goldenport.entity.GEntityContext
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
class BinaryDataSource(aBag: BinaryBag, aContext: GEntityContext,
    uri: URI = null, mimetype: String = null) extends GDataSource(aContext, uri, mimetype) {

  val bag = aBag.newWorkspaceBag

  override protected def open_InputStream: InputStream = {
    bag.openInputStream
  }

  override protected def write_OutputStream(out: OutputStream): Boolean = {
    bag.write(out)
    true
  }

  final def size = bag.size
}

object BinaryDataSource {
  def createBinary(aBinary: Array[Byte], aContext: GEntityContext, 
      name: String = null, mimetype: String = null) = {
    val uri = if (name == null) null else new URI(name)
    new BinaryDataSource(new ByteArrayBinaryBag(aBinary, aContext), aContext, uri, mimetype)    
  }
  
  def createInputStream(aIn: InputStream, aContext: GEntityContext,
      name: String = null, mimetype: String = null) = {
    val uri = if (name == null) null else new URI(name)
    new BinaryDataSource(new InputStreamBinaryBag(aIn, aContext), aContext, uri, mimetype) 
  }

  def createWorkspaceBag(bag: WorkspaceBag, aContext: GEntityContext,
      name: String = null, mimetype: String = null) = {
    val uri = if (name == null) null else new URI(name)
    new BinaryDataSource(new WorkspaceBagBinaryBag(bag, aContext), aContext, uri, mimetype)
  }

  def createBinaryBag(bag: BinaryBag, aContext: GEntityContext,
      name: String = null, mimetype: String = null) = {
    val uri = if (name == null) null else new URI(name)
    new BinaryDataSource(bag, aContext, uri, mimetype)
  }
}

abstract class BinaryBag(val context: GEntityContext) {
  def newWorkspaceBag: WorkspaceBag
}

class ByteArrayBinaryBag(val binary: Array[Byte], aContext: GEntityContext) extends BinaryBag(aContext) {
  final def newWorkspaceBag: WorkspaceBag = {
    val bag = new WorkspaceBag(context)
    bag.add(binary)
    bag
  }
}

class InputStreamBinaryBag(val in: InputStream, aContext: GEntityContext) extends BinaryBag(aContext) {
  final def newWorkspaceBag: WorkspaceBag = {
    val bag = new WorkspaceBag(context)
    bag.add(in)
//    println("Bag size = " + bag.size) 2009-01-25
    bag
  }
}

// 2011-12-05
class WorkspaceBagBinaryBag(val workspace: WorkspaceBag, aContext: GEntityContext) extends BinaryBag(aContext) {
  final def newWorkspaceBag = workspace
}
