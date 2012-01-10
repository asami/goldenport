package com.asamioffice.util

import java.util.Locale

/*
 * Sep. 11, 2008
 * Dec.  4, 2008
 */
object LocaleUtility {
  def localeTuple(locale: Locale): (String, String, String) =
    (locale.getLanguage, locale.getCountry, locale.getVariant)
}
