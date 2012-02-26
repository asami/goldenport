package org.goldenport.entities.orgmode

import scala.xml.{Node, Elem, Group}
import java.io.OutputStream
import scalaz._
import Scalaz._
import org.goldenport.entity._
import org.goldenport.entity.datasource.{GDataSource, NullDataSource, ResourceDataSource}
import org.goldenport.entity.content.GContent
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.sdoc.structure._
import org.goldenport.entities.workspace.TreeWorkspaceEntity
import org.goldenport.entities.zip.ZipEntity
import org.goldenport.value.GTreeBase
import org.goldenport.entities.outline._
import org.smartdox._
import org.smartdox.parser.DoxParser

/**
 * @since   Sep. 15, 2010 (in g3)
 *  since   Nov. 29, 2011
 *  version Nov. 29, 2011
 * @version Feb. 26, 2012
 * @author  ASAMI, Tomoharu
 */
class OrgmodeEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends OutlineEntityBase(aIn, aOut, aContext) {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  protected override def load_Datasource(aDataSource: GDataSource) {
    val reader = aDataSource.openBufferedReader()
    DoxParser.parseOrgmodeZ(reader).fold(_set_error, _load_dox)
  }

  private def _set_error(errors: NonEmptyList[String]) {
  }

  private def _load_dox(dox: Dox) {
    _load_dox(dox.tree)
  }

  private def _load_dox(tree: Tree[Dox]) {
    // XXX addresses doc meta data
    val (stubs, content) = _subs_content(tree.subForest)
    stubs.foreach(root.addChild)
  }

/*
  private def _traverse(d: Tree[Dox], o: OutlineNode) {
    o.addChild(_transform0(d))
  }

  private def _transform0(d: Tree[Dox]): OutlineNode = {
    
  }

  private def _transform(d: Tree[Dox]): Either[Dox, OutlineNode] = {
    d.rootLabel match {
      case s: Section => {
        val (subs, content) = _subs_content(d.subForest)
        TopicNode(s.title, subs, content).right
      }
      case c => c.left
    }
  }
*/

  private def _subs_content(cs: Stream[Tree[Dox]]): (Seq[TopicNode], Option[Dox]) = {
    def tosections(ss: Stream[Tree[Dox]]): Seq[TopicNode] = {
      ss.map(t => (t, t.rootLabel)) collect {
        case (t, s: Section) => {
          val (subs, content) = _subs_content(t.subForest)
          TopicNode(s.title, subs, content)
        }
      }
    }
    def tocontent(c: Stream[Tree[Dox]]): Option[Dox] = {
      c.toList.map(_.rootLabel) match {
        case Nil => none
        case List(x) => x.some
        case xs => Fragment(xs).some
      }
    }
    val (c, ss) = cs.span(!_.rootLabel.isInstanceOf[Section])
    (tosections(ss), tocontent(c)) 
  } 
  
  override protected def write_Content(anOut: OutputStream): Unit = {
    sys.error("not implemented yet.")
  }
}

class OrgmodeEntityClass extends GEntityClass {
  type Instance_TYPE = OrgmodeEntity

  override def accept_Suffix(suffix: String): Boolean = suffix == "org"

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new OrgmodeEntity(aDataSource, aContext))
}

object OrgmodeEntity extends OrgmodeEntityClass
