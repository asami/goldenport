package org.goldenport.entities.specdoc

import scala.collection.mutable.{Buffer, ArrayBuffer}
import org.goldenport.value.{GTreeNodeBase, GTable, PlainTable, GKey}
import org.goldenport.sdoc._
import org.goldenport.sdoc.inline.{SIAnchor, SElementRef}
import org.goldenport.sdoc.parts.SHistory
import org.goldenport.entities.specdoc.plain._
import com.asamioffice.goldenport.text.UJavaString

/**
 * SDNode
 * derived from SDNode.java since Feb. 17, 2007
 *
 * <ul>
 *   <li>Name (+ subtitle or headline)</li>
 *   <li>Summary</li>
 *   <li>Overview</li>
 *   <li>Features</li>
 *   <li>Grammar</li>
 *   <li>Slot summary</li>
 *   <li>Description</li>
 *   <li>Slots</li>
 * </ul>
 *
 * @since   Sep.  4, 2008
 *  version Aug.  6, 2009
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class SDNode(aName: String) extends GTreeNodeBase[SDNode] {
  type TreeNode_TYPE = SDNode
  content = this
  set_name(aName)
  var sdocTitle: SDoc = SEmpty
  var subtitle: SDoc = SEmpty
  val resume = new SSummary
  private var _overview: SDoc = SEmpty
  private var _specification: SDoc = SEmpty
  private var _description: SDoc = SEmpty
  private var _features = new SDFeatureSet
  var note: SDoc = SEmpty
  val categories: Buffer[SDCategory] = new ArrayBuffer[SDCategory]
  val history: SHistory = new SHistory

  def this() = this(null)

  def caption: SDoc = resume.caption
  def caption_=(aDoc: SDoc) { resume.caption = aDoc }

  def brief: SDoc = resume.brief
  def brief_=(aDoc: SDoc) { resume.brief = aDoc }

  def summary: SDoc = resume.summary
  def summary_=(aDoc: SDoc) { resume.summary = aDoc }

  def overview: SDoc = _overview
  def overview_=(aDoc: SDoc) {
    require(aDoc != null)
    _overview = aDoc
  }

  def specification: SDoc = _specification
  def specification_=(aDoc: SDoc) {
    require(aDoc != null)
    _specification = aDoc
  }

  def description: SDoc = _description
  def description_=(aDoc: SDoc) {
    require(aDoc != null)
    _description = aDoc
  }

  final def qualifiedName: String = {
    UJavaString.pathname2className(pathname)
  }

  final def effectiveTitle: SDoc = {
    if (!sdocTitle.isNil) sdocTitle
    else if (title != null) SText(title)
    else SText(name)
  }

  final def anchor: SIAnchor = {
    SIAnchor(effectiveTitle) unresolvedRef_is element_Ref
  }

  protected def element_Ref: SElementRef

  final def features: Seq[SDFeature] = _features.features

  final def feature(key: GKey) = _features(key)

  final def featureHead: Seq[SDoc] = _features.tableHead
  final def featureRows: Seq[Seq[SDoc]] = _features.tableRows

  final def featureTable: GTable[SDoc] = {
    val table = new PlainTable[SDoc]
    table.setHead(featureHead)
    featureRows.foreach(table += _)
/* 2008-10-13
    for (feature <- featureRows)
      table += (feature.key, feature.value, feature.description)
*/
    table
  }

  final def addFeature(aKey: GKey, aValue: SDoc): SDFeature = {
    require(aKey != null && aValue != null)
    val feature = new SDFeature(aKey, aValue)
    _features += feature
    feature
  }

/* 2008-10-21
  // DSL
  final def feature(aKey: GKey, aValue: SDoc): SDFeature = {
    addFeature(aKey, aValue)
  }
*/

  final def effectiveSummary: SDoc = resume.effectiveSummary

  protected def new_Node(aName: String): SDNode = {
    new PlainPackage(aName)
  }
}
