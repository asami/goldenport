package org.goldenport.entities.smartdoc.transformers

import scala.collection.mutable.ArrayBuffer
import org.goldenport.value.{GTree, GTreeNode, GTreeVisitor, PlainTree}
import org.goldenport.value.{GTable, GAttributedTabular}
import org.goldenport.entity.content.BinaryContent
import org.goldenport.sdoc._
import org.goldenport.sdoc.attribute._
import org.goldenport.sdoc.inline._
import org.goldenport.sdoc.block._
import org.goldenport.sdoc.structure._
import org.goldenport.entities.smartdoc.block.SBBinaryContentFigure
import com.asamioffice.goldenport.text.UPathString
import org.goldenport.entities.smartdoc._

/*
 * @since   Sep. 24, 2008
 * @version Apr. 17, 2011
 * @author  ASAMI, Tomoharu
 */
class SmartDoc2HtmlStringTransformer(val sdoc: SDoc) {
  def this(aSmartDoc: SmartDocEntity) = this(aSmartDoc.root)
  require (sdoc != null)

  private var _doc_title: SATitle = null
  private var _toc = new PlainTree[SStructure]
  private var _viewses = new ArrayBuffer[SBViews]
  private var _managedImages = new ArrayBuffer[(String, BinaryContent)]

  var homeRelativePathname: String = ""
  var directory: GTree[DirectoryEntry] = null

  private def get_home_relative_pathname: String = {
    require (homeRelativePathname != null)
    if (homeRelativePathname == "" || homeRelativePathname.endsWith("/"))
      homeRelativePathname
    else
      homeRelativePathname + "/"
  }

  private def make_ref_pathname(aPath: String): String = {
    val x = UPathString.concatPathname(get_home_relative_pathname, aPath)
//    println("make_ref_pathname: " + aPath + " -> " + x)
    x
  }

  final def toHtml: String = {
    val buf = new StringBuilder
    buildHtml(buf)
    buf.toString
  }

  final def buildHtml(aBuffer: StringBuilder) {
    sdoc.traverse(new Setupper)
    sdoc.traverse(new Maker(aBuffer))
  }

  final def getManagedImages: Seq[(String, BinaryContent)] = {
    _managedImages
  }

  class Setupper extends GTreeVisitor[SDoc] {
    private var _current: GTreeNode[SStructure] = _

    override def start(aNode: GTreeNode[SDoc]) {
      _current = _toc.root
    }

    override def enter(aNode: GTreeNode[SDoc]) {
      aNode match {
        case s: SEmpty => 
        case s: SFragment => 
        case s: SText=> 
        //
        case s: SISpan =>
        case s: SIBold => 
        case s: SIItalic => 
        case s: SIAnchor =>
        //
        case s: SBDivision => 
        case s: SBParagraph => 
        case s: SBOrderedList => 
        case s: SBUnorderedList => 
        case s: SBListItem => 
        case s: SBDefinitionList => 
        case s: SBDefinitionTerm => 
        case s: SBDefinitionDescription => 
        case s: SBTable => done_traverse(s)
        case s: SBViews => done_traverse(s)
        case s: SBFigure => done_traverse(s)
        //
        case s: SSDocument => 
        case s: SSHead => open_Head(s)
        case s: SSBody => 
        case s: SSPart => 
        case s: SSChapter => open_Structure(s)
        case s: SSSection => open_Structure(s)
        case s: SSSubSection=> open_Structure(s)
        case s: SSSubSubSection => open_Structure(s)
        //
        case s: SATitle => 
        case _ => sys.error("Unsupported node = " + aNode)
      }
    }

    override def leave(aNode: GTreeNode[SDoc]) {
      aNode match {
        case s: SEmpty => 
        case s: SFragment => 
        case s: SText=> 
        //
        case s: SISpan =>
        case s: SIBold => 
        case s: SIItalic => 
        case s: SIAnchor =>
        //
        case s: SBDivision => 
        case s: SBParagraph => 
        case s: SBOrderedList => 
        case s: SBUnorderedList => 
        case s: SBListItem => 
        case s: SBDefinitionList => 
        case s: SBDefinitionTerm => 
        case s: SBDefinitionDescription => 
        case s: SBTable => 
        case s: SBViews => 
        case s: SBFigure => 
        //
        case s: SSDocument => 
        case s: SSHead =>
        case s: SSBody => 
        case s: SSPart => 
        case s: SSChapter => close_Structure(s)
        case s: SSSection => close_Structure(s)
        case s: SSSubSection=> close_Structure(s)
        case s: SSSubSubSection => close_Structure(s)
        //
        case s: SATitle => 
        case _ => sys.error("Unsupported node = " + aNode)
      }
    }

/* 2008-09-29
    override def end(aNode: GTreeNode[SDoc]) {
      println("toc = " + _toc.toPrettyXml)
    }
*/

    protected def open_Head(head: SSHead) {
      _doc_title = head.title
    }

    protected def open_Structure(structure: SStructure) {
      if (structure.isEmpty) return
      var number = count_structure(_current)
      _current = _current.addChild()
      _current.content = structure
      structure.tocNumber = number + 1
    }

    protected def close_Structure(structure: SStructure) {
      if (structure.isEmpty) return
      _current = _current.parent
    }

    private def count_structure(parent: GTreeNode[SStructure]): Int = {
      var last = 0
      for (child <- parent.children
           if (child.content.tocNumber > 0)) {
        last = child.content.tocNumber
      }
      last
    }
  }

  class Maker(val buffer: StringBuilder) extends GTreeVisitor[SDoc] {
    override def startEnter(aNode: GTreeNode[SDoc]) {
//      println("sdoc2html = " + aNode.content) ; 2008-09-29
      aNode match {
        case s: SEmpty => open_Empty(s)
        case s: SFragment => open_Fragment(s)
        case s: SText=> open_Text(s)
        //
        case s: SISpan => open_Span(s)
        case s: SIBold => open_Bold(s)
        case s: SIItalic => open_Italic(s)
        case s: SIAnchor => open_Anchor(s)
        //
        case s: SBDivision => open_Division(s)
        case s: SBParagraph => open_Paragraph(s)
        case s: SBOrderedList => open_OrderedList(s)
        case s: SBUnorderedList => open_UnorderedList(s)
        case s: SBListItem => open_ListItem(s)
        case s: SBDefinitionList => open_DefinitionList(s)
        case s: SBDefinitionTerm => open_DefinitionTerm(s)
        case s: SBDefinitionDescription => open_DefinitionDescription(s)
        case s: SBTable => open_Table(s)
        case s: SBViews => open_Views(s)
        case s: SBFigure => open_Figure(s)
        //
        case s: SSDocument => open_Document(s)
        case s: SSHead => open_Head(s)
        case s: SSBody => open_Body(s)
        case s: SSPart => open_Part(s)
        case s: SSChapter => open_Chapter(s)
        case s: SSSection => open_Section(s)
        case s: SSSubSection=> open_SubSection(s)
        case s: SSSubSubSection => open_SubSubSection(s)
        //
        case s: SATitle => open_Title(s)
        case _ => sys.error("Unsupported node = " + aNode)
      }
    }

    override def leaveEnd(aNode: GTreeNode[SDoc]) {
      aNode match {
        case s: SEmpty => close_Empty(s)
        case s: SFragment => close_Fragment(s)
        case s: SText=> close_Text(s)
        //
        case s: SISpan => close_Span(s)
        case s: SIBold => close_Bold(s)
        case s: SIItalic => close_Italic(s)
        case s: SIAnchor => close_Anchor(s)
        //
        case s: SBDivision => close_Division(s)
        case s: SBParagraph => close_Paragraph(s)
        case s: SBOrderedList => close_OrderedList(s)
        case s: SBUnorderedList => close_UnorderedList(s)
        case s: SBListItem => close_ListItem(s)
        case s: SBDefinitionList => close_DefinitionList(s)
        case s: SBDefinitionTerm => close_DefinitionTerm(s)
        case s: SBDefinitionDescription => close_DefinitionDescription(s)
        case s: SBTable => close_Table(s)
        case s: SBViews => close_Views(s)
        case s: SBFigure => close_Figure(s)
        //
        case s: SSDocument => close_Document(s)
        case s: SSHead => close_Head(s)
        case s: SSBody => close_Body(s)
        case s: SSPart => close_Part(s)
        case s: SSChapter => close_Chapter(s)
        case s: SSSection => close_Section(s)
        case s: SSSubSection=> close_SubSection(s)
        case s: SSSubSubSection => close_SubSubSection(s)
        //
        case s: SATitle => close_Title(s)
        case _ => sys.error("Unsupported node = " + aNode)
      }
    }

    //
    protected def open_Empty(empty: SEmpty) {
    }

    protected def close_Empty(empty: SEmpty) {
    }

    protected def open_Fragment(fragment: SFragment) {
    }

    protected def close_Fragment(fragment: SFragment) {
    }

    protected def open_Text(text: SText) {
      buffer.append(text.toString)
    }

    protected def close_Text(text: SText) {
    }

    // Span
    protected def open_Span(span: SISpan) {
      open_inline_tag("span", span)
    }

    protected def close_Span(span: SISpan) {
      close_inline_tag("span", span)
    }

    // Bold
    protected def open_Bold(bold: SIBold) {
      open_inline_tag("b", bold)
    }

    protected def close_Bold(bold: SIBold) {
      close_inline_tag("b", bold)
    }

    // Italic
    protected def open_Italic(italic: SIItalic) {
      open_inline_tag("i", italic)
    }

    protected def close_Italic(italic: SIItalic) {
      close_inline_tag("i", italic)
    }

    // Anchor
    protected def open_Anchor(anAnchor: SIAnchor) {
      def make_class: String = {
        anAnchor.unresolvedRef match {
          case obj: SObjectRef => "label-with-reference"
          case element: SElementRef => "label-with-reference"
          case help: SHelpRef => "label-with-help"
          case empty: SNullRef => "open_anchor" // XXX notify bug
        }
      }

      def make_href: String = {
        def make_object_ref(aRef: SObjectRef): String = {
          make_ref_pathname(aRef.name) + ".html"
        }

        def make_element_ref(aRef: SElementRef): String = {
          val buf = new StringBuilder
          buf.append(aRef.packageName)
          buf.append("/")
          if (aRef.objectName != "") {
            buf.append(aRef.objectName)
            buf.append(".html")
          } else {
            buf.append("index.html")
          }
          if (aRef.elementName != "") {
            buf.append("#")
            buf.append(aRef.elementName)
          }
          make_ref_pathname(buf.toString)
        }

        if (anAnchor.href != "") anAnchor.href
        else {
          anAnchor.unresolvedRef match {
            case obj: SObjectRef => make_object_ref(obj)
            case element: SElementRef => make_element_ref(element)
            case help: SHelpRef => "help:" + help.name + help.params.mkString // XXX
            case empty: SNullRef => "open_anchor" // XXX notify bug
          }
        }
      }

      def make_title: String = {
        require (anAnchor.summary != null)
        if (anAnchor.summary == SEmpty) null
        else anAnchor.summary.toString
      }

      open_inline_tag("a", Array(("class", make_class),
                                 ("href", make_href),
                                 ("title", make_title)))
    }

    protected def close_Anchor(anAnchor: SIAnchor) {
      close_inline_tag("a", anAnchor)
    }

    // Division
    protected def open_Division(division: SBDivision) {
      open_block_tag("div", division)
    }

    protected def close_Division(division: SBDivision) {
      close_block_tag("div", division)
    }

    // Paragraph
    protected def open_Paragraph(paragraph: SBParagraph) {
      open_inline_block_tag("p", paragraph)
    }

    protected def close_Paragraph(paragraph: SBParagraph) {
      close_block_tag("p", paragraph)
    }

    // OrderedList
    protected def open_OrderedList(orderedList: SBOrderedList) {
      open_block_tag("ol", orderedList)
    }

    protected def close_OrderedList(orderedList: SBOrderedList) {
      close_block_tag("ol", orderedList)
    }

    // UnorderedList
    protected def open_UnorderedList(unorderedList: SBUnorderedList) {
      open_block_tag("ul", unorderedList)
    }

    protected def close_UnorderedList(unorderedList: SBUnorderedList) {
      close_block_tag("ul", unorderedList)
    }

    // ListItem
    protected def open_ListItem(listItem: SBListItem) {
      open_inline_block_tag("li", listItem)
    }

    protected def close_ListItem(listItem: SBListItem) {
      close_block_tag("li", listItem)
    }

    // DefinitionList
    protected def open_DefinitionList(definitionList: SBDefinitionList) {
      open_block_tag("dl", definitionList)
    }

    protected def close_DefinitionList(definitionList: SBDefinitionList) {
      close_block_tag("dl", definitionList)
    }

    // DefinitionTerm
    protected def open_DefinitionTerm(definitionTerm: SBDefinitionTerm) {
      open_block_tag("dt", definitionTerm)
    }

    protected def close_DefinitionTerm(definitionTerm: SBDefinitionTerm) {
      close_block_tag("dt", definitionTerm)
    }

    // DefinitionDescription
    protected def open_DefinitionDescription(definitionDescription: SBDefinitionDescription) {
      open_block_tag("dd", definitionDescription)
    }

    protected def close_DefinitionDescription(definitionDescription: SBDefinitionDescription) {
      close_block_tag("dd", definitionDescription)
    }

    // Table
    protected def open_Table(aTable: SBTable) {
      def make_caption(aTitle: SATitle) {
        make_inline_block("caption") {
          make_inline_sdoc_children(aTitle)
        }
      }

      open_block_tag("table", Array(("class", "basic-table")))
      make_caption(aTable.title)
      make_thead(aTable.table)
      make_tbody(aTable.table)
      close_block_tag("table")
      done_traverse(aTable)
    }

    private def make_thead(aTable: GTable[SDoc]) {
      val mayHead = aTable.head
      if (mayHead.isEmpty) return
      val head = mayHead.get
      val maySide = aTable.side
      make_block("thead") {
        val rows = head.rows
        for (y <- 0 until rows.length) {
          val row = rows(y)
          make_block("tr") {
            if (maySide.isDefined) {
              val side = maySide.get
              for (i <- 0 until side.width) {
                make_inline_block("th") {
                  // XXX
                }
              }
            }
            for (x <- 0 until row.length) {
              val item = row(x)
              if (item != null) {
                val attr = head.getAttribute(x, y)
                if (attr == null) {
                  make_inline_block("th") {
                    make_inline_sdoc(item)
                  }
                } else {
                  make_inline_block("th", ("colspan", attr.colspan.toString), ("rowspan", attr.rowspan.toString)) {
                    make_inline_sdoc(item)
                  }
		}
              }                  
            }
          }
        }
      }
    }

    private def make_tbody(aTable: GTable[SDoc]) {
      val maySide = aTable.side
      make_block("tbody") {
        var odd = true

        def make_odd_even = {
          if (odd) {
            odd = false
            "odd"
          } else {
            odd = true
            "even"
          }
        }

        var y = 0
        for (row <- aTable.rows) {
          make_block("tr", ("class", make_odd_even)) {
            if (maySide.isDefined) {
              val side = maySide.get
	      for (i <- 0 until side.width) {
		val item = side.get(i, y)
		if (item != null) {
		  val attr = side.getAttribute(i, y)
		  if (attr == null) {
                    make_inline_block("th") {
                      make_inline_sdoc(item)
                    }
		  } else {
                    make_inline_block("th", ("colspan", attr.colspan.toString), ("rowspan", attr.rowspan.toString)) {
                      make_inline_sdoc(item)
                    }
		  }
		}
              }
	    }
            for (item <- row) {
              make_inline_block("td") {
                make_inline_sdoc(item)
              }
            }
          }
          y += 1
        }
      }
    }

    protected def close_Table(table: SBTable) {
      sys.error("unused")
    }

    // Views
    protected def open_Views(aViews: SBViews) {
      _viewses += aViews
      make_block("div", ("id", aViews.effectiveId), ("class", "yui-navset")) {
        val views = aViews.views.toList
        make_block("ul", ("class", "yui-nav")) {
          make_inline_block("li", ("class", "selected")) {
            make_inline("a", ("href", "#" + views.first.effectiveId)) {
              make_inline("em") {
                make_inline_sdoc_children(views.first.title)
              }
            }
          }
          for (view <- views.tail) {
            make_inline_block("li") {
              make_inline("a", ("href", "#" + view.effectiveId)) {
                make_inline("em") {
                  make_inline_sdoc_children(view.title)
                }
              }
            }
          }
        }
        make_block("div", ("class", "yui-content")) {
          for (view <- views) {
            make_block("div", ("id", view.effectiveId)) {
              make_inline_sdoc_children(view)
            }
          }
        }
      }
      done_traverse(aViews)
    }

    protected def close_Views(aViews: SBViews) {
      sys.error("unused")
    }

    // Figure
    protected def open_Figure(aFigure: SBFigure) {
      aFigure match {
        case image: SBBinaryContentFigure => {
          _managedImages += ((image.src, image.binary))
        }
      }
      open_block_tag("div")
      open_block_tag("image", Array(("src", aFigure.src)), aFigure)
    }

    protected def close_Figure(aFigure: SBFigure) {
      close_block_tag("image", aFigure)
      close_block_tag("div")
    }

    // Document
    protected def open_Document(document: SSDocument) {
      open_block_tag("html", document)
    }

    protected def close_Document(document: SSDocument) {
      close_block_tag("html", document)
    }

    // Head
    protected def open_Head(head: SSHead) {
      open_block_tag("head", head)
      buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n")
    }

    protected def close_Head(head: SSHead) {
      close_Head_Prologue(head)
      close_block_tag("head", head)
    }

    protected def close_Head_Prologue(head: SSHead) {
      add_link_css("lib/yui/yui.css")
      add_link_css("lib/smartdoc.css")
      add_link_css("lib/yui/menu.css")
      add_link_css("lib/yui/treeview-menu.css")
      add_link_css("lib/yui/tabview.css")
      add_style()
      add_script_src("lib/yui/yahoo-dom-event.js")
//      add_script_src("lib/yui/yuiloader-dom-event.js")
      add_script_src("lib/yui/container_core.js")
      add_script_src("lib/yui/menu-min.js")
      add_script_src("lib/yui/treeview-min.js")
      add_script_src("lib/yui/element-beta-min.js")
      add_script_src("lib/yui/tabview-min.js")
      add_script()
    }

    private def add_style() {
      buffer.append("<style type=\"text/css\">\n")
      buffer.append("#productsandservices {\n")
      buffer.append("  position: static;\n")
      buffer.append("}\n")
      buffer.append("</style>\n")
    }

    private def add_script() {
      buffer.append("<script type=\"text/javascript\">\n")
      buffer.append("  YAHOO.util.Event.onContentReady(\"productsandservices\", function () {\n")
      buffer.append("  var oMenu = new YAHOO.widget.Menu(\"productsandservices\", {\n")
      buffer.append("                                    position: \"static\",\n")
      buffer.append("                                    hidedelay:  750,\n")
      buffer.append("                                    lazyload: true });\n")
      buffer.append("                                    oMenu.render();\n")
      buffer.append("  });\n")
      buffer.append("</script>\n")
    }

    // Body
    protected def open_Body(body: SSBody) {
      open_block_tag("body", body_Attributes, body)
      open_Body_Epilogue(body)
    }

    protected def body_Attributes: Seq[(String, String)] = {
      Array(("id", "doc"), ("class", "yui-skin-sam"))
    }

    protected def open_Body_Epilogue(body: SSBody) {
      open_block_tag("div", Array(("class", "yui-t4")), body)
      make_block("div", ("id", "hd")) {
        make_header()
      }
      open_block_tag("div", Array(("id", "bd")))
      open_block_tag("div", Array(("id", "yui-main")))
      open_block_tag("div", Array(("id", "yui-b"), ("class", "t4-body")))
    }

    protected def close_Body(body: SSBody) {
      close_Body_Prologue(body)
      close_block_tag("body", body)
    }

    protected def close_Body_Prologue(body: SSBody) {
      close_block_tag("div")
      close_block_tag("div")
      make_block("div", ("class", "yui-b")) {
        make_block("div", ("id", "productsandservices"), ("class", "yuimenu")) {
          make_document_set_directory()
        }
        make_document_toc()
      }
      close_block_tag("div")
      make_block("div", ("id", "ft")) {
        make_footer()
      }
      close_block_tag("div")
      _toc.traverse(new TocMaker)
      script_views
    }

    class TocMaker extends GTreeVisitor[SStructure] {
      override def start(aNode: GTreeNode[SStructure]) {
        buffer.append("<script>\n")
        buffer.append("var tree = new YAHOO.widget.TreeView(\"tocNode\");\n")
        buffer.append("var root = tree.getRoot();\n")
      }

      override def enter(aNode: GTreeNode[SStructure]) {
        var nodeName = make_node_name(aNode.content)
        var parentName = make_parent_node_name(aNode.content)
        buffer.append("var ")
        buffer.append(nodeName)
        buffer.append("= new YAHOO.widget.TextNode(\"")
        buffer.append(make_node_title(aNode.content))
        buffer.append("\", ")
        buffer.append(parentName)
        buffer.append(", false);\n")
        buffer.append(nodeName)
        buffer.append(".href = \"")
        buffer.append("#")
        buffer.append(aNode.content.effectiveId)
        buffer.append("\";\n")
      }

      override def leave(aNode: GTreeNode[SStructure]) {
      }

      override def end(aNode: GTreeNode[SStructure]) {
        buffer.append("tree.draw();\n")
        buffer.append("</script>\n")
      }

      private def make_node_name(aNode: GTreeNode[SDoc]): String = {
        def make_seq(aNode: GTreeNode[SDoc]): List[Int] = {
          if (aNode.parent == null) Nil
          else if (aNode.content.tocNumber == 0) make_seq(aNode.parent)
          else aNode.content.tocNumber :: make_seq(aNode.parent)
        }
        "node" + make_seq(aNode).reverse.mkString("_")
      }

      private def make_parent_node_name(aNode: GTreeNode[SDoc]): String = {
        make_node_name(aNode.parent) match {
          case "node" => "root"
          case name => name
        }
      }

      private def make_node_title(aStructure: SStructure): String = {
        aStructure.title.toText
      }
    }

    private def script_views {
      buffer.append("<script>\n")
      buffer.append("(function() {\n")
      for (name <- get_views_names) {
        buffer.append("new YAHOO.widget.TabView('")
        buffer.append(name)
        buffer.append("');\n")
      }
      buffer.append("})();\n")
      buffer.append("</script>\n")
    }

    private def get_views_names: Seq[String] = {
      _viewses.map(_.effectiveId)
    }

    private def make_header() {
      make_block("div", ("id", "pagetitle")) {
        make_inline_block("h1") {
          make_inline_sdoc_children(_doc_title)
        }
      }
    }

    private def make_footer() {
    }

    private def make_document_set_directory() {
      if (directory == null) return
      make_block("div", ("class", "bd")) {
        make_block("ul", ("class", "first-of-type")) {
          for (child <- directory.root.children) {
            make_inline_block("li", ("class", "yuimenuitem first-of-type")) {
              make_inline("a",
                          ("class", "yuimenuitemlabel"),
                          ("href", get_href(child))) {
                make_inline_sdoc(child.content.title)
              }
              make_directory_ul(child)
            }
          }
        }
      }
    }

    private def make_directory_ul(aNode: GTreeNode[DirectoryEntry]) {
      if (aNode.isEmpty) return
      make_block("div", ("id", get_node_id(aNode)), ("class", "yuimenu")) {
        make_block("div", ("class", "bd")) {
          make_block("ul", ("class", "first-of-type")) {
            for (child <- aNode.children) {
              make_inline_block("li", ("class", "yuimenuitem")) {
                make_inline("a",
                            ("class", "yuimenuitemlabel"),
                            ("href", get_href(child))) {
                  make_inline_sdoc(child.content.title)
                }
                make_directory_ul(child)
              }
            }
          }
        }
      }
    }

    private def get_href(entry: GTreeNode[DirectoryEntry]): String = {
      val path = make_ref_pathname(entry.content.pathname)
      if (entry.isEmpty) path + ".html"
      else path + "/index.html"
    }

    private def get_node_id(aNode: GTreeNode[DirectoryEntry]): String = {
      aNode.name // XXX
    }

    private def make_document_toc() {
      buffer.append("<div id=\"tocNode\"></div>\n")
    }

    // Part
    protected def open_Part(part: SSPart) {
    }

    protected def close_Part(part: SSPart) {
    }

    // Chapter
    protected def open_Chapter(chapter: SSChapter) {
    }

    protected def close_Chapter(chapter: SSChapter) {
    }

    // Section
    protected def open_Section(section: SSSection) {
      make_inline_block("h2", ("id", section.effectiveId)) {
        make_inline_sdoc_children(section.title)
      }
    }

    protected def close_Section(section: SSSection) {
    }

    // SubSection
    protected def open_SubSection(subsection: SSSubSection) {
      make_inline_block("h3", ("id", subsection.effectiveId)) {
        make_inline_sdoc_children(subsection.title)
      }
    }

    protected def close_SubSubSection(subsubsection: SSSubSubSection) {
    }

    // SubSubSection
    protected def open_SubSubSection(subsubsection: SSSubSubSection) {
      make_inline_block("h4", ("id", subsubsection.effectiveId)) {
        make_inline_sdoc_children(subsubsection.title)
      }
    }

    protected def close_SubSection(subsection: SSSubSection) {
    }

    //
    protected def open_Title(aTitle: SATitle) {
      aTitle.parent match {
        case p: SSHead => open_Head_Title(aTitle)
        case p: SStructure => // do nothing
        case p: SBlock => // do nothing
        case p => sys.error("Unsupported node = " + p + " in " + aTitle)
      }
      done_traverse(aTitle)
    }

    protected def close_Title(title: SATitle) {
      title.parent match {
        case p: SSHead => close_Head_Title(title)
        case p: SStructure => // do nothing
        case p: SBlock => // do nothing
        case p => sys.error("Unsupported node = " + p + " in " + title)
      }
    }

    protected def open_Head_Title(title: SATitle) {
      make_inline_block("title") {
        make_inline_sdoc_children(title)
      }
    }

    protected def close_Head_Title(title: SATitle) {
      close_block_tag("title", title)
    }

    //
    protected final def make_block(aName: String, theAttrs: (String, String)*)(children: => Unit) {
      open_block_tag(aName, theAttrs)
      children
      close_block_tag(aName)
    }

    protected final def make_inline_block(aName: String, theAttrs: (String, String)*)(children: => Unit) {
      open_inline_block_tag(aName, theAttrs)
      children
      close_inline_block_tag(aName)
    }

    protected final def make_inline(aName: String, theAttrs: (String, String)*)(children: => Unit) {
      open_inline_tag(aName, theAttrs)
      children
      close_inline_tag(aName)
    }

    protected final def make_null_caution {
      buffer.append("<span style='color: red;background-color: yellow;text-decoration: line-through;'>null</span>")
    }

    protected final def make_inline_sdoc_children(aDoc: SDoc) {
      if (aDoc == null) {
        make_null_caution
      } else {
        make_inline_sdoc(aDoc.children)
      }
    }

    protected final def make_inline_sdoc(theDocs: Seq[SDoc]) {
      if (theDocs == null) {
        make_null_caution
      } else {
        theDocs.foreach(make_inline_sdoc)
      }
    }

    protected final def make_inline_sdoc(aDoc: SDoc) {
      if (aDoc == null) {
        make_null_caution
      } else {
        aDoc.traverse(new Maker(buffer))
      }
    }

    protected final def make_text(string: String) {
      buffer.append(string) // XXX make Xml String
    }

    protected final def open_block_tag(aName: String, theAttrs: Seq[(String, String)], isEmpty: Boolean) {
      buffer.append("<")
      buffer.append(aName)
      for (attr <- theAttrs) {
        buffer.append(" ")
        buffer.append(attr._1)
        buffer.append("=\"")
        buffer.append(attr._2)
        buffer.append("\"")
      }
      if (isEmpty)
        buffer.append(" />\n")
      else
        buffer.append(">\n")
    }

    protected final def open_block_tag(aName: String, theAttrs: Seq[(String, String)], aNode: GTreeNode[SDoc]) {
      open_block_tag(aName, theAttrs, aNode.isEmpty)
    }

    protected final def open_block_tag(aName: String, aNode: GTreeNode[SDoc]) {
      open_block_tag(aName, Nil, aNode)
    }

    protected final def open_block_tag(aName: String, theAttrs: Seq[(String, String)]) {
      open_block_tag(aName, theAttrs, false)
    }

    protected final def open_block_tag(aName: String) {
      open_block_tag(aName, Nil)
    }

    protected final def close_block_tag(aName: String, aNode: GTreeNode[SDoc]) {
      close_block_tag(aName, aNode.isEmpty)
    }

    protected final def close_block_tag(aName: String, isEmpty: Boolean) {
      if (isEmpty) return
      buffer.append("</")
      buffer.append(aName)
      buffer.append(">")
      buffer.append("\n")
    }

    protected final def close_block_tag(aName: String) {
      close_block_tag(aName, false)
    }

    protected final def open_inline_block_tag(aName: String, theAttrs: Seq[(String, String)], isEmpty: Boolean) {
      buffer.append("<")
      buffer.append(aName)
      for (attr <- theAttrs) {
        buffer.append(" ")
        buffer.append(attr._1)
        buffer.append("=\"")
        buffer.append(attr._2)
        buffer.append("\"")
      }
      if (isEmpty)
        buffer.append(" />\n")
      else
        buffer.append(">")
    }

    protected final def open_inline_block_tag(aName: String, theAttrs: Seq[(String, String)], aNode: GTreeNode[SDoc]) {
      open_inline_block_tag(aName, theAttrs, aNode.isEmpty)
    }

    protected final def open_inline_block_tag(aName: String, aNode: GTreeNode[SDoc]) {
      open_inline_block_tag(aName, Nil, aNode)
    }

    protected final def open_inline_block_tag(aName: String, theAttrs: Seq[(String, String)]) {
      open_inline_block_tag(aName, theAttrs, false)
    }

    protected final def open_inline_block_tag(aName: String) {
      open_inline_block_tag(aName, Nil)
    }

    protected final def close_inline_block_tag(aName: String, aNode: GTreeNode[SDoc]) {
      close_inline_block_tag(aName, aNode.isEmpty)
    }

    protected final def close_inline_block_tag(aName: String, isEmpty: Boolean) {
      if (isEmpty) return
      buffer.append("</")
      buffer.append(aName)
      buffer.append(">")
      buffer.append("\n")
    }

    protected final def close_inline_block_tag(aName: String) {
      close_inline_block_tag(aName, false)
    }

    protected final def open_inline_tag(aName: String, theAttrs: Seq[(String, String)], isEmpty: Boolean) {
      buffer.append("<")
      buffer.append(aName)
      for (attr <- theAttrs) {
        if (attr._2 != null) {
          buffer.append(" ")
          buffer.append(attr._1)
          buffer.append("=\"")
          buffer.append(attr._2)
          buffer.append("\"")
        }
      }
      if (isEmpty)
        buffer.append(" />")
      else
        buffer.append(">")
    }

    protected final def open_inline_tag(aName: String, theAttrs: Seq[(String, String)], aNode: GTreeNode[SDoc]) {
      open_inline_tag(aName, theAttrs, aNode.isEmpty)
    }

    protected final def open_inline_tag(aName: String, aNode: GTreeNode[SDoc]) {
      open_inline_tag(aName, Nil, aNode)
    }

    protected final def open_inline_tag(aName: String, theAttrs: Seq[(String, String)]) {
      open_inline_tag(aName, theAttrs, false)
    }

    protected final def open_inline_tag(aName: String) {
      open_inline_tag(aName, Nil)
    }

    protected final def close_inline_tag(aName: String, aNode: GTreeNode[SDoc]) {
      close_inline_tag(aName, aNode.isEmpty)
    }

    protected final def close_inline_tag(aName: String, isEmpty: Boolean) {
      if (isEmpty) return
      buffer.append("</")
      buffer.append(aName)
      buffer.append(">")
    }

    protected final def close_inline_tag(aName: String) {
      close_inline_block_tag(aName, false)
    }

/* 2008-11-04
    //XXX
    protected final def open_inline_tag(aName: String, aNode: GTreeNode[SDoc]) {
      buffer.append("<")
      buffer.append(aName)
      if (aNode.isEmpty)
        buffer.append(" />")
      else
        buffer.append(">")
    }

    //XXX
    protected final def close_inline_tag(aName: String, aNode: GTreeNode[SDoc]) {
      if (aNode.isEmpty) return
      buffer.append("</")
      buffer.append(aName)
      buffer.append(">")
    }
*/

    protected final def add_link_css(aHref: String) {
      buffer.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"")
      add_relative_path()
      buffer.append(aHref)
      buffer.append("\" />\n")
    }

    protected final def add_script_src(aSrc: String) {
      buffer.append("<script type=\"text/javascript\" src=\"")
      add_relative_path()
      buffer.append(aSrc)
      buffer.append("\"></script>\n")
    }

    private def add_relative_path() {
      buffer.append(get_home_relative_pathname)
/* 2008-10-22      
      require (homeRelativePathname != null)
      if (homeRelativePathname == "") return
      buffer.append(homeRelativePathname)
      buffer.append("/")
*/
    }
  }
}
