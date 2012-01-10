package org.goldenport.sdoc

import java.util.Locale
import com.asamioffice.util.LocaleRepository

/*
 * parts???
 *
 * Sep. 16, 2008
 * Oct. 26, 2008
 */
class SSummary {
  private var _caption: SDoc = SEmpty
  private var _brief: SDoc = SEmpty
  private var _summary: SDoc = SEmpty
  private val _captions_locale = new LocaleRepository[SDoc]
  private val _briefs_locale = new LocaleRepository[SDoc]
  private val _summaries_locale = new LocaleRepository[SDoc]

  final def caption: SDoc = _caption
  final def caption_=(aDoc: SDoc) = {
    require (aDoc != null)
    _caption = aDoc
  }

  final def brief: SDoc = _brief
  final def brief_=(aDoc: SDoc) = {
    require (aDoc != null)
    _brief = aDoc
  }

  final def summary: SDoc = _summary
  final def summary_=(aDoc: SDoc) = {
    require (aDoc != null)
    _summary = aDoc
  }

  def caption_en: SDoc = {
    _captions_locale.getEnOrElse(caption)
  }

  def caption_en_=(aCaption: SDoc) {
    _captions_locale.putEn(aCaption)
  }

  def brief_en: SDoc = {
    _briefs_locale.getEnOrElse(brief)
  }

  def brief_en_=(aBrief: SDoc) = {
    _briefs_locale.putEn(aBrief)
  }

  def summary_en: SDoc = {
    _summaries_locale.getEnOrElse(summary)
  }

  def summary_en_=(aSummary: SDoc) = {
    _summaries_locale.putEn(aSummary)
  }

  def effectiveSummary: SDoc = {
    if (!summary.isNil) summary
    else if (!caption.isNil && brief.isNil) caption
    else if (caption.isNil && !brief.isNil) brief
    else if (!caption.isNil && !brief.isNil) SFragment(caption, brief)
    else SEmpty
  }

  final def copyIn(aSummary: SSummary) {
    _caption = aSummary._caption
    _brief = aSummary._brief
    _summary = aSummary._summary
    _captions_locale.copyIn(aSummary._captions_locale)
    _briefs_locale.copyIn(aSummary._briefs_locale)
    _summaries_locale.copyIn(aSummary._summaries_locale)
  }
}
