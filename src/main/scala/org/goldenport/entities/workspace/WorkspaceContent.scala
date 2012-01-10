package org.goldenport.entities.workspace

import java.io._
import org.goldenport.entity.GEntityContext
import org.goldenport.entity.content.GContent

/*
 * Aug. 19. 2008
 * Aug. 20, 2008
 */
class WorkspaceContent(aContext: GEntityContext) extends GContent(aContext) {
  private val content_bag = new WorkspaceBag(aContext)

  override def open_InputStream(): InputStream = {
    content_bag.openInputStream()
  }

  override def write_OutputStream(aOut: OutputStream): Boolean = {
    content_bag.write(aOut)
    true
  }
}
