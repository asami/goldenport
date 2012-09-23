package org.goldenport.entities.smartdox

import com.asamioffice.goldenport.text.UPathString
import scalaz._
import Scalaz._
import org.goldenport.recorder.Recordable
import org.goldenport.Z._
import org.smartdox._
import Dox.TreeDoxVW
import Dox.TreeDoxW
import Dox.DoxVW
import org.goldenport.entity._
import org.goldenport.value.GTable

/**
 * @since   Jan. 11, 2012 (org.smartdox.processor.transformers)
 *  version Jan. 26, 2012
 *  version Jul. 21, 2012
 *  version Aug. 25, 2012
 *  version Sep.  9, 2012 (goldenport)
 * @version Sep. 24, 2012
 * @author  ASAMI, Tomoharu
 */
trait Dox2Dox {
  self: Recordable =>
  protected val entitySpace: GEntitySpace
  protected val baseUri: Option[String]

  protected final def dox2doxVW(d: Dox): DoxVW = {
    Dox.treeLensVW.mod(d.toVW, tdox2_TdoxVW)
  }

  protected def tdox2_TdoxVW(d: TreeDoxVW): TreeDoxVW = {
    aux_DoxVW(modify_doxVW(d))
  }

  protected final def modify_doxVW(d: TreeDoxVW): TreeDoxVW = {
    val root = d.map(_.map(find_Root))
    root.flatMap { w =>
      def trans(t: Tree[Dox]): TreeDoxVW = {
        def log(t: Tree[Dox]) = log_treedox("Dox2Dox transfered = ", t)
        eval_Dox(t) |> transform_Dox |> log |> (writer(nil[String], _).successNel[String])
      }
      w.over.fold(trans, "No root".failNel) 
    }
  }

  protected final def log_treedox(message: String, t: Tree[Dox]) = {
    record_trace(message + t.drawTree)
    t
  }
  
  protected def aux_DoxVW(d: TreeDoxVW): TreeDoxVW = d

  protected def find_Root(t:Tree[Dox]): Option[Tree[Dox]] = t.some

  protected def eval_Dox(t: Tree[Dox]): Tree[Dox] = {
    replace(t) {
      case (table: Table, cs) => {
        if (table.head.isEmpty && table.foot.isEmpty) {
          table.body.records match {
            case (t: TTable) :: Nil => load_table(table, t)
            case _ => (table, cs) // XXX
          }
        } else (table, cs)
      }
      case (table: TTable, cs) => load_table(Table(none, TBody(nil), none, none, none), table)
    }
  }

  protected final def load_table(t: Table, tt: TTable): (Table, Stream[Tree[Dox]]) = {
    val (h, b) = load_table(tt)
    val table = t.copy(caption = tt.caption, label = tt.label)
    (table, Stream(tt.caption, h, b.some).flatten.map(Dox.tree))
  }

  protected final def load_table(tt: TTable): (Option[THead], TBody) = {
    val uri = baseUri match {
      case Some(s) => UPathString.concatPathname(s, tt.uri)
      case None => tt.uri
    }
    entitySpace.reconstitute(uri) match {
      case Some(e) => load_table_entity(e)
      case None => {
        record_warning("%s not found in table.".format(tt.uri))
        (None, TBody(List(TR(List(TD(List(Text("%s not found.".format(tt.uri)))))))))
      }
    }
  }

  protected final def load_table_entity(entity: GEntity): (Option[THead], TBody) = {
    entity match {
      case table: GTableEntity[_] => {
        table using {
          _load_table_trees(table)
        }
      }
      case tables: GTableListEntity[_] => {
        tables using {
          _load_table_trees(tables.head)
        }
      }
      case _ => {
        val uri = entity.inputDataSource
        record_warning("%s not found in table.".format(uri))
        (None, TBody(List(TR(List(TD(List(Text("%s not found.".format(uri)))))))))
      }
    }
  }

  private def _load_table_trees(t: GTable[_]) = {
    val a = _load_table(t)
    (None, TBody(a.toList))    
  }

  private def _load_table(t: GTable[_]) = {
    for (y <- 0 until t.height) yield {
      val b = for (x <- 0 until t.width) yield {
        t.getOption(x, y) match {
          case Some(s) => TD(List(Text(s.toString)))
          case None => TD(Nil)
        }
      } 
      TR(b.toList)
    }    
  }

  protected def transform_Dox(t: Tree[Dox]): Tree[Dox]
}

class Dox2DoxEvaluater(
  val entitySpace: GEntitySpace,
  val baseUri: Option[String]
) extends Dox2Dox with Recordable {
  def eval(d: Dox): DoxVW = {
    dox2doxVW(d)
  }

  protected def transform_Dox(t: Tree[Dox]) = t
}
