package org.goldenport.entities.workspace

import java.io._
import org.goldenport.entity.GEntityContext
import com.asamioffice.goldenport.io.UIO

/*
 * original since Aug. 16, 2005
 *
 * @since   Aug. 19. 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class WorkspaceBag(aContext: GEntityContext) {
  private val OnMemorySize = 8192
  private val bag_context = aContext
  private var bag_count = 0
  private var bag_buffer = new Array[Byte](OnMemorySize)
  private var bag_file: File = _
  private var bag_out: BufferedOutputStream = _

  final def dispose(): Unit = {
    bag_buffer = null
    if (bag_file != null) {
      bag_file.delete()
      bag_file = null
    }
  }

  final def size: Int = bag_count

  final def getBytes: Array[Byte] = {
    if (bag_buffer != null) {
      bag_buffer // XXX
    } else {
      UIO.file2Bytes(bag_file)
    }
  }

  final def openInputStream(): InputStream = {
    if (bag_buffer != null) {
      new ByteArrayInputStream(bag_buffer, 0, bag_count)
    } else {
      new FileInputStream(bag_file)
    }
  }

  final def openOutputStream(): OutputStream = {
    new WorkspaceBagOutputStream()
  }

  final def add(aBinary: Array[Byte]) { // XXX optimize
    val in = new ByteArrayInputStream(aBinary)
    try {
      add(in)
    } finally {
      in.close()
    }
  }

  final def add(anIn: InputStream) {
    val out = openOutputStream()
    try {
      UIO.stream2stream(anIn, out)
    } finally {
      out.close()
    }
  }

  final def write(aOut: OutputStream):Unit = {
    if (bag_buffer != null) {
      aOut.write(bag_buffer, 0, bag_count)
    } else {
      var in: InputStream = null
      try {
	in = openInputStream()
	UIO.stream2stream(in, aOut)
      } finally {
	if (in != null) in.close()
      }
    }
  }

  class WorkspaceBagOutputStream extends OutputStream {
    override def write(aByte: Int) {
      if (bag_count == OnMemorySize) {
	bag_file = bag_context.createWorkFile()
	val fileOut = new FileOutputStream(bag_file)
	try {
	  bag_out = new BufferedOutputStream(fileOut)
	  bag_out.write(bag_buffer)
	  bag_out.write(aByte)
	  bag_buffer = null
	} catch {
	  case _ => fileOut.close()
	}
      } else if (bag_out != null) {
	bag_out.write(aByte)
      } else if (bag_buffer != null) {
	bag_buffer(bag_count) = aByte.asInstanceOf[Byte]
      } else {
	sys.error("internal error")
      }
      bag_count += 1
    }

    override def flush(): Unit = {
      if (bag_out != null) bag_out.flush()
    }

    override def close(): Unit = {
      if (bag_out != null) {
	bag_out.close()
	bag_out = null
      }
    }
  }
}
