package com.asamioffice.goldenport.text

/**
 * @since   Apr.  4, 2012
 * @version Apr.  7, 2012
 * @author  ASAMI, Tomoharu
 */
class JavaScriptTextMaker(aTemplate: CharSequence, theReplaces: Map[String, String]
                        ) extends ProgramTextMaker(aTemplate, theReplaces) {
  def this() = this(null, null)
  def this(aTemplate: CharSequence) = this(aTemplate, null)
}
