package com.asamioffice.text

import java.io.Flushable

/*
 * Jan. 15, 2009
 * Jan. 15, 2009
 */
class AppendableTextBuilder(val out: Appendable) extends TextBuilder {
  set_appendable(out)
}
