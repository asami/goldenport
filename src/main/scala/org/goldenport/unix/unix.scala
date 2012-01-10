package org.goldenport

import java.io._
import com.asamioffice.io.{UIO, UFile}
import com.asamioffice.text.UPathString
import org.goldenport.unix._

/*
 * @since   Dec. 12, 2010
 * @version Jan. 26, 2011
 * @author  ASAMI, Tomoharu
 */
package object unix {
  def ls: UnixText = ls(".")

  def ls(file: String = "."): UnixText = {
    ls(new File(file))
  }

  def ls(file: File): UnixText = {
    if (file.isDirectory) {
      UnixText(file.list)
    } else {
      UnixText(file.getName)
    }
  }

  def cat(file: String): UnixText = {
    UnixText(UIO.uri2String(file, "UTF-8"))
  }

  def cp(src: String, dest: String) {
    cp(new File(src), new File(dest))
  }

  def cp(src: File, dest: File) {
    if (dest.isDirectory) {
      UFile.copyFile(src, new File(dest, src.getName))
    } else {
      UFile.copyFile(src, dest)
    }
  }

  def mv(src: String, dest: String) {
    val sf = new File(src)
    val df = new File(dest)
    sf.renameTo(df)
  }

  def cmp(src: String, dest: String): Boolean = {
    cmp(new File(src), new File(dest))
  }

  def cmp(sf: File, df: File): Boolean = {
    if (sf.length() != df.length()) return false
    var sin: InputStream = null
    var din: InputStream = null
    try {
      sin = new BufferedInputStream(new FileInputStream(sf))
      din = new BufferedInputStream(new FileInputStream(df))
      var sc: Int = sin.read()
      while (sc > 0) {
        val dc = din.read()
        if (sc != dc) return false
        sc = sin.read()
      }
      return true
    } finally {
      if (sin != null) sin.close()
      if (din != null) din.close()
    }
  }

  def path(dir: String, file: String) = {
    UPathString.concatPathname(dir, file)
  }
}
