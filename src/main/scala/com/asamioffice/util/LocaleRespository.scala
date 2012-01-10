package com.asamioffice.util

import scala.collection.mutable.ArrayBuffer
import java.util.Locale

/*
 * Sep. 11, 2008
 * Feb.  9, 2009
 */
class LocaleRepository[E] {
  private val _data = new ArrayBuffer[(Locale, E)]

  final def get(aLocale: Locale): Option[E] = {
    val (language, country, variant) = LocaleUtility.localeTuple(aLocale)
    for ((key, value) <- _data) {
      if (language == key.getLanguage && country == key.getCountry && variant == key.getVariant) return Some(value)
    }
    for ((key, value) <- _data) {
      if (language == key.getLanguage && country == key.getCountry && "" == key.getVariant) return Some(value)
    }
    for ((key, value) <- _data) {
      if (language == key.getLanguage && "" == key.getCountry && "" == key.getVariant) return Some(value)
    }
    return None
  }

  final def getOrElse[E2 >: E](aLocale: Locale, aDefault: => E2): E2 = {
    require(aLocale != null && aDefault != null)
    val result = get(aLocale)
    if (result.isDefined) result.get
    else aDefault
  }

  final def put(aLocale: Locale, aValue: E): Option[E] = {
    require(aLocale != null && aValue != null)
    for (i <- 0 until _data.length) {
      val (key, value) = _data(i)
      if (aLocale.equals(key)) {
	_data(i) = (aLocale, aValue)
	return Some(value)
      }
    }
    _data += ((aLocale, aValue))
    None
  }

  // English
  final def getEn: Option[E] = get(Locale.ENGLISH)

  final def getEnOrElse[E2 >: E](aDefault: => E2): E2 =
    getOrElse(Locale.ENGLISH, aDefault)

  final def putEn(aValue: E): Option[E] = put(Locale.ENGLISH, aValue)

  // Japanese
  final def getJa: Option[E] = get(Locale.JAPANESE)

  final def getJaOrElse[E2 >: E](aDefault: => E2): E2 =
    getOrElse(Locale.ENGLISH, aDefault)

  final def putJa(aValue: E): Option[E] = put(Locale.JAPANESE, aValue)

  //
  final def copyIn(aRepository: LocaleRepository[E]) {
    _data ++= aRepository._data
  }
}
