package org.goldenport.entities.specdoc.generators

import org.goldenport.entities.smartdoc._
import org.goldenport.entities.smartdoc.builder._
import com.asamioffice.text.UJavaString
import org.goldenport.entities.specdoc._


/*
 * @since   Oct.  6, 2008
 * @version Jul. 15, 2010
 * @author  ASAMI, Tomoharu
 */
class SpecDoc2SmartDocRealmGenerator(val specdoc: SpecDocEntity) {
  private val builder = new SmartDocRealmBuilder(specdoc.entityContext)
  private val cursor = builder.getCursor
  private val title_summary = "概要" // XXX
  private val title_feature = "特性" // XXX
  private val title_description = "説明" // XXX
  private val title_history = "履歴" // XXX

  final def transform: SmartDocRealmEntity = {
    transform_with_category
    builder.make ensuring (_.isOpened)
  }

  private def transform_with_category {
    for (pkg <- specdoc.entityPackages) {
      cursor.enterTopic(pkg.title, UJavaString.pathname2className(pkg.pathname))
      build_package_prologue(pkg)
      for (entity <- pkg.entities) {
	cursor.enterPage(entity.effectiveTitle, entity.name)
	build_entity_prologue(entity)
	for (category <- entity.categories) {
	  cursor.enterDivision(category.label)
	  build_category_prologue(category)
	  for (subEntity <- entity.subEntities(category)) {
	    cursor.enterDivision(subEntity.effectiveTitle)
	    build_sub_entity_prologue(subEntity)
	    cursor.leaveDivision()
	  }
	  cursor.leaveDivision()
	}
	cursor.enterDivision(title_history)
	build_history_prologue(entity)
	cursor.leaveDivision()
	cursor.leavePage()
      }
      for (summary <- pkg.summaries) {
	cursor.enterPage(summary.effectiveTitle, summary.name)
	build_summary_prologue(summary, pkg.entities)
	cursor.leavePage()
      }
      cursor.leaveTopic()
    }
  }

  private def build_package_prologue(aPackage: SDPackage) {
    cursor.addDescription(aPackage.effectiveSummary)
    cursor.addDescription(aPackage.overview)
    cursor.addTable(aPackage.featureTable) caption_is title_feature
    for (category <- aPackage.categories) {
      cursor.addTable(aPackage.entitiesTable(category)) caption_is category.label
    }
    cursor.addDescription(aPackage.specification)
  }

  private def build_entity_prologue(anEntity: SDEntity) {
    cursor.addDescription(anEntity.effectiveSummary)
    cursor.addDescription(anEntity.overview)
    cursor.addTable(anEntity.featureTable) caption_is title_feature
    for (category <- anEntity.categories) {
      cursor.addTable(anEntity.subEntitiesTable(category)) caption_is category.label
    }
    cursor.addDescription(anEntity.specification)
    cursor.enterDivision(title_description)
    cursor.addDescription(anEntity.description)
    cursor.leaveDivision()
  }

  private def build_category_prologue(aCategory: SDCategory) {
  }

  private def build_sub_entity_prologue(anEntity: SDEntity) {
    cursor.addDescription(anEntity.effectiveSummary)
    cursor.addDescription(anEntity.overview)
    cursor.addTable(anEntity.featureTable) caption_is title_feature
    cursor.addDescription(anEntity.specification)
    cursor.enterDivision(title_description)
    cursor.addDescription(anEntity.description)
    cursor.leaveDivision()
  }

  private def build_history_prologue(anEntity: SDEntity) {
    if (anEntity.history.isEmpty) return
    cursor.addTable(anEntity.history.toTable)
  }

  private def build_summary_prologue(aSummary: SDSummary, theEntities: Seq[SDEntity]) {
    cursor.addDescription(aSummary.effectiveSummary)
    cursor.addDescription(aSummary.overview)
    cursor.addTable(aSummary.summaryTable(theEntities))
  }
}
