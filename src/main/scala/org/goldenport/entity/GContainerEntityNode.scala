package org.goldenport.entity

import org.goldenport.entity.datasource.GDataSource
import org.goldenport.entity.content.GContent
import org.goldenport.entity.content.EntityContent
import com.asamioffice.goldenport.text.UPathString

/**
 * @since   Aug.  5, 2008
 *  version Mar. 12, 2009
 * @version Feb.  3, 2012
 * @author  ASAMI, Tomoharu
 */
abstract class GContainerEntityNode(aName: String, aEntity: GContainerEntity) {
  type ContainerEntity_TYPE <: GContainerEntity
//  type DataSource_TYPE <: GDataSource

  val name: String = aName
  val container: ContainerEntity_TYPE = aEntity.asInstanceOf[ContainerEntity_TYPE]
  private var mounted_entity: GEntity = null
  private var node_content: GContent = null
  private var input_dataSource: GDataSource = _
  private var output_dataSource: GDataSource = _
  //
  private var is_filled: Boolean = false
  private var is_dirty: Boolean = false
  private var last_modified: Long = System.currentTimeMillis()

  def entity: Option[GEntity] = { // XXX getEntityOption
    fill_node()
    if (mounted_entity != null)
      Some(mounted_entity)
    else if (node_content.isInstanceOf[EntityContent])
      Some(node_content.asInstanceOf[EntityContent].entity)
    else
      None
  }

  def suffix: Option[String] = { // XXX getSuffixOption
    fill_node()
    var maySuffix = node_Suffix
    if (maySuffix != None) return maySuffix
    if (node_content != null) {
      maySuffix = node_content.getSuffix()
      if (maySuffix != None) return maySuffix
    }
    if (name != null) {
      var suffix = UPathString.getSuffix(name)
      if (suffix != null) return Some(suffix)
    }
    None
  }

  protected def node_Suffix: Option[String] = None

  def content: GContent = {
    fill_node()
    if (node_content == null) null
    else node_content
  }

  def content_=(aContent: GContent) {
    if (node_content == aContent) return
    set_content(aContent)
    setDirty()
    notify_Set_Content(aContent)
  }

  def getContent: Option[GContent] = {
    Option(content)
  }

  def isContent: Boolean = {
    content != null
  }

  final protected def set_content(aContent: GContent) {
    if (node_content != null) node_content.close()
    node_content = aContent
    if (node_content != null) {
      node_content.open()
    }
    is_filled = true
    if (is_automount) {
      mount()
    }
  }

  protected def notify_Set_Content(aContent: GContent): Unit = null

  private[entity] def setDirty() { // visibility
    if (is_dirty) return
    is_dirty = true
    set_Dirty()
    container.setDirty()
  }

  protected def set_Dirty(): Unit = null

  private[entity] def clearDirty() { // visibility
    is_dirty = false
    clear_Dirty()
    // should not invoke container.clearDirty() 
  }

  protected def clear_Dirty(): Unit = null

  final def isDirty: Boolean = is_dirty || is_Dirty

  protected def is_Dirty: Boolean = false

  final protected def set_dataSource(aDataSource: GDataSource) {
    input_dataSource = aDataSource
    output_dataSource = aDataSource
  }

  /*
   * XXX visibility
   */
  final def setInputDataSource(aDataSource: GDataSource) {
    set_input_dataSource(aDataSource)
//    if (this.isInstanceOf[org.goldenport.entities.fs.FileStoreNode]) { 2009-03-12
//      println("setInputDataSource = " + aDataSource)
//      sys.error("???")
//    }
  }

  final protected def set_input_dataSource(aDataSource: GDataSource) {
    input_dataSource = aDataSource
  }

  final protected def set_output_dataSource(aDataSource: GDataSource) {
    input_dataSource = aDataSource
  }

  final protected def get_dataSource(): Option[GDataSource] = {
    require(input_dataSource == output_dataSource, "input = " + input_dataSource + ", output = " + output_dataSource)
    if (input_dataSource == null) None
    else Some(input_dataSource)
  }

  final def getInputDataSource(): Option[GDataSource] = {
    if (input_dataSource == null) {
      val mayDs = get_Input_DataSource()
      if (mayDs.isEmpty) return None
      input_dataSource = mayDs.get
    }
    Some(input_dataSource)
  }

  protected def get_Input_DataSource(): Option[GDataSource] = None

  final def getOutputDataSource(): Option[GDataSource] = {
    if (output_dataSource == null) {
      val mayDs = get_Output_DataSource()
      if (mayDs.isEmpty) return None
      output_dataSource = mayDs.get
    }
    Some(output_dataSource)
  }

  protected def get_Output_DataSource(): Option[GDataSource] = None

  final def mount(): Option[GEntity] =  {
    if (mounted_entity != null) return Some(mounted_entity)
    fill_node()
    var mayEntity = container.entityContext.reconstitute(this)
    if (mayEntity == None) {
      if (node_content.isInstanceOf[EntityContent]) {
	mayEntity = Some(node_content.asInstanceOf[EntityContent].entity)
      }
    }
    mount_entity(mayEntity)
  }

  final def mount(anEntity: GEntity): GEntity = {
    require(mounted_entity == null)
    fill_node()
    mount_entity(anEntity)
  }

//  def mount(Class<? extends IRModelClass> modelClass)

  def mount(aEntityClass: GEntityClass): Option[GEntity] = {
    require(mounted_entity == null)
    fill_node()
    mount_entity(aEntityClass.reconstitute(this, container.entityContext))
  }

  def unmount() {
    if (mounted_entity == null) return
    if (mounted_entity.isDirty)
      set_content(mounted_entity.toContent)
    // unmount does not invoke the commit method
    mounted_entity.close()
    mounted_entity = null
    fire_unmount()
  }

  def mountedEntity: Option[GEntity] = {
    if (mounted_entity == null) None
    else Some(mounted_entity)
  }

  def lastModified: Long = {
    fill_node()
    if (isDirty) last_modified
    else if (node_content != null) node_content.lastModified
    else last_modified
  }

  /*
   * Output DataSource Priority
   *
   * 1. Mounted Entity
   * 2. Node (or implicitly container Entity via get_Output_DataSource)
   * 3. Content
   * 
   */
  def commit() {
    if (mounted_entity != null) {
      mounted_entity.commit()
    } else if (isDirty) {
      if (node_content != null && node_content.isCommitable) {
        val ds = getOutputDataSource()
        if (ds.isDefined) {
          node_content.write(ds.get)
        } else {
          node_content.commit()
        }
      } else {
        commit_Dirty_Node()
      }
    }
    clearDirty()
  }

  protected def commit_Dirty_Node(): Unit = null

  protected def fill_node() {
    if (is_filled) return
    set_content(load_Content())
    fill_Specific_Content()
    is_filled = true
    if (is_automount) mount()
  }

  protected def load_Content(): GContent = null

  protected def fill_Specific_Content(): Unit = null

  private def is_automount: Boolean = false // XXX

  private def mount_entity(mayEntity: Option[GEntity]): Option[GEntity] = {
    require(mayEntity != null)
    if (mayEntity == None) return None
    mount_entity(mayEntity.get)
    mayEntity
  }

  private def mount_entity(anEntity: GEntity): GEntity = {
    mounted_entity = anEntity
    mounted_entity.mountNode = this
    mounted_entity.open()
    fire_mount()
    mounted_entity
  }

  private def fire_mount() {
    // XXX
  }

  private def fire_unmount() {
    // XXX
  }
}
