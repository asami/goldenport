package org.goldenport.entities.smartdox

import scala.xml.{Node, Elem, Group}
import java.io.OutputStream
import scalaz._
import Scalaz._
import org.goldenport.z._
import org.goldenport.Z._
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
 *  version Mar.  3, 2012 (OrgmodeEntity)
 *  version Sep.  8, 2012 (SmartDoxEntity)
 * @version Sep.  9, 2012 (goldenport)
 * @author  ASAMI, Tomoharu
 */
class SmartDoxEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends OutlineEntityBase(aIn, aOut, aContext) {
  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  var dox: ValidationNEL[String, Dox] = m("文書が設定されていません。").failNel

  protected override def load_Datasource(aDataSource: GDataSource) {
    val reader = aDataSource.openBufferedReader()
    DoxParser.parseOrgmodeZ(reader).fold(_set_error, _load_dox)
  }

  private def _set_error(errors: NonEmptyList[String]) {
    dox = errors.fail
  }

  private def _load_dox(d: Dox) {
    val evaluater = new Dox2DoxEvaluater(entityContext.entitySpace, getBaseUriAsString)
    // type DoxVW = ValidationNEL[String, Writer[List[String], Dox]]
    val evaled = evaluater.eval(d)
    evaled match {
      case Success(s) => {
        dox = s.over.success
        _load_dox(s.over.tree)        
      }
      case Failure(f) => {
        _set_error(f)
      }
    }
  }

  private def _load_dox(tree: Tree[Dox]) {
    // XXX addresses doc meta data
    val home = ZTrees.find(tree)(_.rootLabel.isInstanceOf[Body]) | tree
    val (stubs, content) = _subs_content(home.subForest)
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

  private def _subs_content(cs: Stream[Tree[Dox]]): (Seq[TopicNode], Dox) = {
    def tosections(ss: Stream[Tree[Dox]]): Seq[TopicNode] = {
      ss.map(t => (t, t.rootLabel)) collect {
        case (t, s: Section) => {
          val (subs, content) = _subs_content(t.subForest)
          TopicNode(s.title, subs, content)
        }
      }
    }
    def tocontent(c: Stream[Tree[Dox]]): Dox = {
      c.toList.map(_.rootLabel) match {
        case Nil => EmptyDox
        case List(x) => x
        case xs => Fragment(xs)
      }
    }
    val (c, ss) = cs.span(!_.rootLabel.isInstanceOf[Section])
    (tosections(ss), tocontent(c)) 
  } 
  
  override protected def write_Content(anOut: OutputStream): Unit = {
    sys.error("not implemented yet.")
  }
}

class SmartDoxEntityClass extends GEntityClass {
  type Instance_TYPE = SmartDoxEntity

  override def accept_Suffix(suffix: String): Boolean = List("dox", "org").element(suffix)

  override def reconstitute_DataSource(aDataSource: GDataSource, aContext: GEntityContext): Option[Instance_TYPE] = Some(new SmartDoxEntity(aDataSource, aContext))
}

object SmartDoxEntity extends SmartDoxEntityClass
