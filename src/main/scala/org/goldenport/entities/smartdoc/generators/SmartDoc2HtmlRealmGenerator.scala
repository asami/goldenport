package org.goldenport.entities.smartdoc.generators

import org.goldenport.entity._
import org.goldenport.sdoc._
import org.goldenport.entities.smartdoc._
import org.goldenport.entities.smartdoc.transformers._
import org.goldenport.entities.html.HtmlRealmEntity

/**
 * @since   Oct.  5, 2008
 *  version Jul. 15, 2010
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDoc2HtmlRealmGenerator(val sdoc: SmartDocEntity, val name: String, val context: GEntityContext) {
  def this(aSdoc: SmartDocEntity, aName: String) = this(aSdoc, aName, aSdoc.entityContext)
  def this(aSdoc: SmartDocEntity) = this(aSdoc, "")
  require (sdoc != null && name != null)

  final def toHtmlRealm: HtmlRealmEntity = {
    buildHtmlRealm(new HtmlRealmEntity(context))
  }

  final def buildHtmlRealm(realm: HtmlRealmEntity): HtmlRealmEntity = {
    realm.open()
    val trans = new SmartDoc2XHtmlYuiTransformer(sdoc)
    val html = trans.toHtml
    realm.setString(file_name, html)
    add_library(realm)
    realm ensuring (_.isOpened)
  }

  private def file_name: String = {
    document_name + ".html"
  }

  private def document_name = {
    if (name != "") name
    else if (sdoc.name != "") sdoc.name
    else normalize_file_name(sdoc.head.sdocTitle.toText)
  }

  private def normalize_file_name(aName: String): String = {
    context.normalizeFilename(aName)
  }

  // XXX unify SmartDocRealm2HtmlRealmGenerator
  private def add_library(aRealm: GTreeContainerEntity) {
    val lib = aRealm.setNode("lib")
    lib.addReference("resource:/org/goldenport/entities/smartdoc/lib/smartdoc.css")
    val yui = lib.setNode("yui")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/yui.css")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menu.css")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menu_down_arrow.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menu_down_arrow_disabled.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menu_up_arrow.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menu_up_arrow_disabled.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menubaritem_submenuindicator.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menubaritem_submenuindicator_disabled.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menubaritem_submenuindicator_selected.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menuitem_checkbox.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menuitem_checkbox_disabled.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menuitem_checkbox_selected.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menuitem_submenuindicator.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menuitem_submenuindicator_disabled.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/assets/menuitem_submenuindicator_selected.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/treeview/assets/treeview-menu.css")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/treeview/assets/treeview-loading.gif")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/treeview/assets/sprite-menu.gif")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/treeview/assets/sprite-orig.gif")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/yuiloader-dom-event/yuiloader-dom-event.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/menu-min.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/treeview/treeview-min.js")
  }
}
