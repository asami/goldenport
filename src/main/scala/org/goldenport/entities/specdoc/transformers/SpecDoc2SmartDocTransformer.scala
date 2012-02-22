package org.goldenport.entities.specdoc.transformers

import org.goldenport.entities.smartdoc._
import org.goldenport.entities.smartdoc.builder._
import org.goldenport.entities.specdoc._

/*
 * derived from SpecDoc2SmartDocTransformer.java since Mar. 12, 2007
 *
 * @since   Sep.  6, 2008
 *  version Jul. 15, 2010
 * @version Feb. 22, 2012
 * @author  ASAMI, Tomoharu
 */
class SpecDoc2SmartDocTransformer(val specdoc: SpecDocEntity) {
  private val builder = new SmartDocBuilder(specdoc.entityContext)
  private val cursor = builder.getCursor
  private val title_summary = "概要" // XXX
  private val title_feature = "特性" // XXX
  private val title_description = "説明" // XXX

  final def transform: SmartDocEntity = {
    transform_head
    transform_body
    builder.make ensuring(_.isOpened)
  }

/*
  private def transform_without_category {
    for (pkg <- specdoc.entityPackages) {
      cursor.enterDivision(pkg.title)
      build_package_prologue(pkg)
      for (entity <- pkg.entities) {
	cursor.enterDivision(entity.title)
	build_entity_prologue(entity)
//	build_sub_entity_directory(entity, title_category)
	for (subEntity <- entity.subEntities) {
	  cursor.enterDivision(subEntity.title)
	  build_sub_entity_prologue(subEntity)
	  cursor.leaveDivision()
	}
	cursor.leaveDivision()
      }
      cursor.leaveDivision()
    }
  }
*/

  private def transform_head {
    builder.name = specdoc.name
    builder.title = specdoc.sdocTitle
  }

  private def transform_body {
    for (pkg <- specdoc.entityPackages) {
      cursor.enterDivision(pkg.effectiveTitle)
      build_package_prologue(pkg)
      for (entity <- pkg.entities) {
	cursor.enterDivision(entity.effectiveTitle)
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
	cursor.leaveDivision()
      }
      cursor.leaveDivision()
    }
  }

  private def build_package_prologue(aPackage: SDPackage) {
  }

  private def build_entity_prologue(anEntity: SDEntity) {
    cursor.addDescription(anEntity.effectiveSummary)
    cursor.addTable(anEntity.featureTable) caption_is title_feature
    for (category <- anEntity.categories) {
      cursor.addTable(anEntity.subEntitiesTable(category)) caption_is category.name
    }
    cursor.addDescription(anEntity.specification)
    cursor.addDescription("TEST - SpecDoc2SmartDocTransformer")
    cursor.enterDivision(title_description)
    cursor.addDescription(anEntity.description)
    cursor.leaveDivision()
  }

  private def build_category_prologue(aCategory: SDCategory) {
  }

  private def build_sub_entity_prologue(anEntity: SDEntity) {
    cursor.addDescription(anEntity.effectiveSummary)
    cursor.addDescription(anEntity.specification)
  }
}
