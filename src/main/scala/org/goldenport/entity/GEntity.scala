package org.goldenport.entity

import com.asamioffice.goldenport.text.UPathString
import java.io._
import org.goldenport.GObject
import org.goldenport.entity.datasource._
import org.goldenport.entity.content.GContent
import org.goldenport.entity.content.EntityContent
import org.goldenport.entity.locator.GLocator
import org.goldenport.entity.locator.EntityLocator
import org.goldenport.entity.content.BinaryContent
import org.goldenport.entity.datasource.BinaryBag
import org.goldenport.entity.datasource.BinaryBag
import org.goldenport.entities.workspace.WorkspaceBag
import org.goldenport.entity.datasource.WorkspaceBagBinaryBag
import org.goldenport.entity.datasource.BinaryDataSource
import java.net.URI

/*
 * @since   Aug.  6, 2008
 *  version Sep. 17, 2010
 *  version Jan.  9, 2012
 *  version Jan. 27, 2012
 *  version May.  5, 2012
 *  version Jul. 21, 2012
 *  version Aug. 25, 2012
 *  version Sep.  8, 2012
 * @version Nov. 22, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GEntity(aIn: GDataSource, aOut: GDataSource, aContext: GEntityContext) extends GObject {
  type DataSource_TYPE <: GDataSource
  private val input_dataSource: DataSource_TYPE = aIn.asInstanceOf[DataSource_TYPE]
  private val output_dataSource: DataSource_TYPE = aOut.asInstanceOf[DataSource_TYPE]
  private var entity_context: GEntityContext = aContext
  private var open_count: Int = 0
  private var _is_exists: Boolean = false
  private var is_dirty: Boolean = false
  private var entity_dataSource: EntityDataSource = _
  private var entity_locator: EntityLocator = _
  var mountNode: GContainerEntityNode = null

  var commitMode: CommitMode = NormalCommit // XXX protected

  protected def is_Writable: Boolean = true
  protected def is_Special_Commit: Boolean = false
  protected def is_Text_Output: Boolean = false

  def this(aDataSource: GDataSource, aContext: GEntityContext) = this(aDataSource, aDataSource, aContext)
  def this(aContext: GEntityContext) = this(null, aContext)

  setup_Recordable(aContext)

  /*
   */
  final protected def assert_opened = {
    if (open_count < 0) sys.error("Illegal open count: " + open_count)
    else if (open_count == 0) sys.error("Entity must be opened")
  }

  /*
   */
  final def inputDataSource: DataSource_TYPE = input_dataSource
  final def outputDataSource: DataSource_TYPE = output_dataSource

  final def getEntityDataSource(): EntityDataSource = {
    if (entity_dataSource == null) {
      entity_dataSource = get_Entity_DataSource()
      if (entity_dataSource == null) {
	entity_dataSource = new EntityDataSource(this, entity_context)
      }
    }
    entity_dataSource
  }

  def get_Entity_DataSource(): EntityDataSource = null

  final def locator: EntityLocator = {
    if (entity_locator == null) {
      entity_locator = entity_Locator
      if (entity_locator == null) {
	entity_locator = new EntityLocator(this) {}
      }
    }
    entity_locator
  }

  protected def entity_Locator: EntityLocator = null

  final def openCount = open_count
  final def isOpened = open_count > 0

  final def open() {
    require(open_count >= 0)
    open_count += 1
    if (open_count == 1) {
      open_Pre_Condition()
      open_entity()
      open_Entity()
      open_Post_Condition()
      clear_dirty()
    }
  }

  def create() {
    require(open_count == 0)
    open_count += 1
    if (input_dataSource == null || input_dataSource == NullDataSource) {
      open_Entity_Create()
    } else if (!input_dataSource.isExist) {
      open_Entity_Create()
    } else {
      open_Entity_Create()
    }
    open_Entity()
  }


  final protected def open_entity() {
    if (input_dataSource == null || input_dataSource == NullDataSource) {
      open_Entity_Create()
    } else if (!input_dataSource.isExist) {
      _set_name_if_needed
      open_Entity_Create(input_dataSource)
    } else {
      _set_name_if_needed
      open_Entity_Update(input_dataSource)
    }
  }

  protected def open_Entity() {
  }

  private def _set_name_if_needed {
    if (name == "") {
      name = input_dataSource.simpleName
    }
  }

  protected def open_Entity_Create() {
    sys.error("missing open_Entity_Create():" + this)
  }

  protected def open_Entity_Create(aDataSource: DataSource_TYPE) {
    sys.error("missing open_Entity_Create(GDataSource):" + this)
  }

  protected def open_Entity_Update(aDataSource: DataSource_TYPE) {
    sys.error("missing open_Entity_Update(GDataSource):" + this)
  }

  protected def open_Pre_Condition() {
  }

  protected def open_Post_Condition() {
  }

  /*
   * The close does not mean to be a invalid state.
   * A closed entity can be reused after an next open.
   * The close should discard redundant resources like cache data,
   * but essential data must be active.
   * The entity can discard whole memory data if the entity can restore
   * data from own DataSource.
   */
  def close() {
    require(open_count > 0)
    if (open_count == 1) {
      close_Entity()
    }
    open_count -= 1
    assert(open_count >= 0, ":" + this)
  }

  protected def close_Entity() {
  }

  def commit() {
    if (!commitMode.isCommitable) {
      is_dirty = false
      return
    }
    if (!is_dirty) return
    if (is_Special_Commit) {
      entity_Commit()
    } else {
      entity_commit()
    }
    is_dirty = false
  }

  protected def entity_Commit(): Unit = null

  private def entity_commit() {
//    println("GEntity#entity_commit(%s)".format(name))
    if (output_dataSource == null || output_dataSource == NullDataSource)
      return
    if (is_Text_Output)
      entity_commit_text()
    else
      entity_commit_binary()
  }

  private def _output_mode = commitMode.outputMode

  private def entity_commit_text() {
    for (out <- output_dataSource.openBufferedWriter(_output_mode)) {
      try {
        write_Content(out)
        out.flush()
      } finally {
        if (out != null) out.close()
      }
    }
  }

  private def entity_commit_binary() {
    for (out <- output_dataSource.openOutputStream(_output_mode)) {
      try {
        write_Content(out)
        out.flush()
      } finally {
        if (out != null) out.close()
      }
    }
  }

  def rollback() {
    not_implemented_yet
  }

  def delete() {
    not_implemented_yet
  }

  def dispose() {
    not_implemented_yet
  }

  def isCommitable: Boolean = commitMode.isCommitable

  def isExist: Boolean = {
    assert_opened
    _is_exists
  }

  def isDirty: Boolean = {
    assert_opened
    is_dirty
  }

  private[entity] def setDirty(): Unit = { // visibility
    set_dirty()
  }

  protected final def set_dirty(): Unit = {
    if (is_dirty) return
    is_dirty = true
    if (mountNode != null) {
      mountNode.setDirty()
    }
  }

  protected final def clear_dirty() {
    if (!is_dirty) return
    is_dirty = false
    if (mountNode != null) {
      mountNode.clearDirty()
    }
  }

  def toContent: GContent = {
    new EntityContent(this, entityContext)
  }

/*
  def toContent(aIn: GDataSource, aOut: GDataSource): GContent = {
    new EntityContent(this, entityContext)
  }
*/

  def entityContext: GEntityContext = entity_context

  def getSuffix(): Option[String] = {
    if (entity_Suffix.isDefined)
      return entity_Suffix
    else if (input_dataSource != null) 
      input_dataSource.getSuffix()
    else None
  }

  protected def entity_Suffix: Option[String] = None

  final def write(aDataSource: GDataSource): Unit = {
//    println("GEntity#write(%s)".format(name))
    require (is_Writable)
    for (out <- aDataSource.openOutputStream(_output_mode)) {
      try {
        write(out)
        out.flush()
      } finally {
        out.close()
      }
    }
  }

  final def write(aOut: OutputStream): Unit = {
//    println("GEntity#write(%s)".format(name))
    require (is_Writable)
    if (is_Text_Output) {
      var writer = new BufferedWriter(
	    new OutputStreamWriter(aOut, entity_context.textEncoding))
      write_Content(writer)
      writer.flush()
    } else {
      write_Content(aOut)
      aOut.flush()
    }
  }

  protected def write_Content(aOut: OutputStream): Unit = {
    sys.error("missing write_Content(OutputStream):" + this)
  }

  protected def write_Content(aOut: BufferedWriter): Unit = {
    sys.error("missing write_Content(BufferedWriter):" + this)
  }

  //
  def toBinaryContent: BinaryContent = {
    val work = new WorkspaceBag(entity_context)
    val out = work.openOutputStream()
    try {
      write(out)
      val bag = new WorkspaceBagBinaryBag(work, entity_context)
      val ds = BinaryDataSource.createBinaryBag(bag, entity_context)
      new BinaryContent(ds, entity_context)
    } finally {
      out.close()
    }
  }

  def getBinaryContent(name: String, mimetype: String): BinaryContent = {
    val work = new WorkspaceBag(entity_context)
    val out = work.openOutputStream()
    try {
      write(out)
      val bag = new WorkspaceBagBinaryBag(work, entity_context)
      val ds = new BinaryDataSource(bag, entity_context, new URI(name), mimetype)
      new BinaryContent(ds, entity_context)
    } finally {
      out.close()
    }
  }

  def toText: String = {
    require (is_Text_Output)
    val s = new StringWriter()
    var writer = new BufferedWriter(s)
    write_Content(writer)
    writer.flush()
    s.toString()
  }

  def using[T](body: => T): T = {
    open()
    try {
      body
    } finally {
      close()
    }
  }

  def getBaseUriAsString(): Option[String] = {
    if (inputDataSource == null || inputDataSource == NullDataSource) {
      None
    } else {
      inputDataSource.getUrl.map(x => UPathString.getContainerPathname(x.toString))
    }
  }

  // utilities for concrete entities.
  protected final def m(s: String, args: Any*): String = {
    entityContext.formatString(s, args: _*)
  }
}

sealed trait CommitMode {
  def isCommitable: Boolean
  def outputMode: OutputMode
}
case object NormalCommit extends CommitMode {
  def isCommitable: Boolean = true
  def outputMode: OutputMode = OverwriteOutput
}
case object NoCommit extends CommitMode {
  def isCommitable: Boolean = false
  def outputMode: OutputMode = sys.error("Not reached.")
}
case object SampleCommit extends CommitMode {
  def isCommitable: Boolean = true
  def outputMode: OutputMode = SampleOutput
}
case object TryCommit extends CommitMode {
  def isCommitable: Boolean = true
  def outputMode: OutputMode = TryOutput
}
case object TrySampleCommit extends CommitMode {
  def isCommitable: Boolean = true
  def outputMode: OutputMode = TrySampleOutput
}
