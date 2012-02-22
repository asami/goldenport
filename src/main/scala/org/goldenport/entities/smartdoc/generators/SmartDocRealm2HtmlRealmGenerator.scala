package org.goldenport.entities.smartdoc.generators

import scala.collection.mutable.HashSet
import org.goldenport.value.{GTree, PlainTree}
import org.goldenport.entity._
import org.goldenport.sdoc._
import org.goldenport.entities.smartdoc.transformers._
import org.goldenport.entities.html.HtmlRealmEntity
import com.asamioffice.goldenport.text.{UString, UPathString, UJavaString}
import org.goldenport.entities.smartdoc._

/*
 * @since   Oct.  6, 2008
 *  version Apr. 17, 2011
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class SmartDocRealm2HtmlRealmGenerator(val sdoc: SmartDocRealmEntity, val name: String, val context: GEntityContext) {
  def this(aSdoc: SmartDocRealmEntity, aName: String) = this(aSdoc, aName, aSdoc.entityContext)
  def this(aSdoc: SmartDocRealmEntity) = this(aSdoc, null)
  require (sdoc != null)

  final def toHtmlRealm: HtmlRealmEntity = {
    buildHtmlRealm(new HtmlRealmEntity(context))
  }

  final def buildHtmlRealm(aRealm: HtmlRealmEntity): HtmlRealmEntity = {
    aRealm.open()
    add_library(aRealm)
    val collector = new DirectoryCollector
    sdoc.traverse(collector)
    val directory = collector.result(0)
    sdoc.traverse(new HtmlRealmBuilder(aRealm, directory))
    aRealm ensuring (_.isOpened)
  }

  class DirectoryCollector extends GTreeContainerVisitor {
    private val __directory = new PlainTree[DirectoryEntry]
    private var __current = __directory.root
    private val __home_packages = new HashSet[GTreeContainerEntityNode]

    override def enter(aNode: GTreeContainerEntityNode) {
      if (aNode.name == "index") {
	aNode.entity.get match {
	  case sdoc: SmartDocEntity => {
	    val child = __current.setChild(aNode.name)
	    val dir = new DirectoryEntry
	    dir.title = "INDEX"
	    dir.pathname = aNode.pathname
	    dir.node = aNode
	    child.content = dir
	  }
	  case _ => //
	}
	done_traverse(aNode)
      } else if (aNode.name == "glossary") {
	aNode.entity.get match {
	  case sdoc: SmartDocEntity => {
	    val child = __current.setChild(aNode.name)
	    val dir = new DirectoryEntry
	    dir.title = "GLOSSARY"
	    dir.pathname = aNode.pathname
	    dir.node = aNode
	    child.content = dir
	  }
	  case _ => //
	}
	done_traverse(aNode)
      } else if (aNode.entity.isDefined) {
	aNode.entity.get match {
	  case sdoc: SmartDocEntity => {
	    val child = __current.setChild(aNode.name)
	    val dir = new DirectoryEntry
	    dir.title = sdoc.head.sdocTitle.toContents
	    dir.pathname = aNode.pathname
	    dir.node = aNode
	    child.content = dir
	  }
	  case _ => //
	}
	done_traverse(aNode)
      } else if (aNode.children.exists(_.entity.isDefined)) {
	val child = __current.setChild(aNode.name)
	val dir = new DirectoryEntry
	dir.title = make_theme_title(aNode)
	dir.pathname = aNode.pathname
	dir.node = aNode
	child.content = dir
	__current = child
	set_home_package(aNode)
      }
    }

    private def make_theme_title(aNode: GTreeContainerEntityNode): SDoc = {
      if (is_home_package(aNode))
	SText(UJavaString.pathname2className(aNode.pathname))
      else aNode.name
    }

    private def set_home_package(aNode: GTreeContainerEntityNode) {
      __home_packages += aNode
    }

    private def is_home_package(aNode: GTreeContainerEntityNode): Boolean = {
      var node = aNode
      while (node != null) {
	if (__home_packages.contains(node)) return false
	node = node.parent
      }
      return true
    }

/*
    private def make_theme_title(aNode: GTreeContainerEntityNode): SDoc = {
      aNode.children.find(_.name == "index.html") match {
	case Some(index: GTreeContainerEntityNode) => {
	  val sdoc = index.entity.get.asInstanceOf[SmartDocEntity]
	  sdoc.head.title.toContents
	}
	case None => UString.capitalize(aNode.name)
      }
    }
*/

    override def leave(aNode: GTreeContainerEntityNode) {
      if (__current.content.node == aNode) {
	__current = __current.parent
      }
    }

    def result: Seq[GTree[DirectoryEntry]] = Array(__directory)
  }

  class HtmlRealmBuilder(val htmlRealm: HtmlRealmEntity, val directory: GTree[DirectoryEntry]) extends GTreeContainerVisitor {
    override def enter(aNode: GTreeContainerEntityNode) {
      aNode.entity match {
	case Some(sdoc: SmartDocEntity) => build_smartdoc(aNode, sdoc)
	case _ => //
      }
    }

    private def build_smartdoc(aNode: GTreeContainerEntityNode, aSdoc: SmartDocEntity) {
//      println("HtmlRealmBuilder = " + aSdoc.toPrettyXml) 2008-10-14
      val trans = new SmartDoc2XHtmlYuiTransformer(aSdoc)
      trans.homeRelativePathname = relative_pathname(aNode)
      trans.directory = directory
      val html = trans.toHtml
      htmlRealm.setString(path_name(aNode), html)
      val images = trans.getManagedImages
      for (image <- images) {
	val pathname = aNode.parent.pathname + "/" + image._1
	htmlRealm.setContent(pathname, image._2)
      }
    }

    private def path_name(aNode: GTreeContainerEntityNode): String = {
      UPathString.changeSuffix(aNode.pathname, "html")
    }

    private def relative_pathname(aNode: GTreeContainerEntityNode): String = {
      var node: GTreeContainerEntityNode = aNode.parent
      if (node == null) return ""
      node = node.parent
      if (node == null) return ""
      node = node.parent
      if (node == null) return ".."
      val buffer = new StringBuilder
      buffer.append("..")
      while (node != null) {
	buffer.append("/..")
	node = node.parent
      }
      buffer.toString
    }
  }

  private def add_library(aRealm: HtmlRealmEntity) {
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
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/assets/skins/sam/tabview.css")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/assets/skins/sam/sprite.png")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/yahoo-dom-event/yahoo-dom-event.js")
//    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/yuiloader-dom-event/yuiloader-dom-event.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/container/container_core.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/menu/menu-min.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/treeview/treeview-min.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/element/element-beta-min.js")
    yui.addReference("resource:/org/goldenport/entities/smartdoc/lib/yui/tabview/tabview-min.js")
  }
}
