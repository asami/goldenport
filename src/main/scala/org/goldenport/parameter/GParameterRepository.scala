package org.goldenport.parameter

import scala.collection.mutable.{ArrayBuffer, HashMap}
import java.util.Date
import java.net.URL
import scala.math.BigInt
import com.asamioffice.goldenport.text.UString
import com.asamioffice.goldenport.io.UURL

/*
 * @since   Oct. 30, 2008
 *  version Feb.  7, 2009
 *  version Jan.  7, 2011
 * @version Dec. 26, 2012
 * @author  ASAMI, Tomoharu
 */
class GParameterRepository {
  private var _parent: GParameterRepository = null
  private var _category: GParameterCategory = _
  private var _entries = new HashMap[String, GParameterEntry]
  private var _aliases = new HashMap[String, String]

  final def parent = _parent

  final def setParent(aParent: GParameterRepository) {
    require (aParent != null)
    require (_parent == null)
    _parent = aParent
  }

  final def setCategory(aCategory: GParameterCategory) {
    require (aCategory != null)
    require (_category == null)
    _category = aCategory
  }

  final def getParameterValue(aKey: String): GParameterValue = {
    val key = get_real_key(aKey)
    // XXX cache
    val pv = new GParameterValue(key)
    if (get_parameter_value_with_substitute(key, pv) == null)
      if (_parent != null) {
	_parent.get_parameter_value_with_substitute(key, pv)
      }
    pv
  }

  private def get_parameter_value_with_substitute(aKey: String, aPv: GParameterValue): Any = {
    if (get_parameter_value_vertical(aKey, aPv) == null) {
      val mayEntry = _entries.get(aKey)
      if (mayEntry.isDefined) {
	val entry = mayEntry.get
	if (entry.value == null) {
	  if (entry.substituteKey != null) {
	    get_parameter_value_with_substitute(entry.substituteKey, aPv)
	  }
	}
      }
    }
    aPv.value
  }

  private def get_parameter_value_vertical(aKey: String, aPv: GParameterValue): Any = {
    val mayEntry = _entries.get(aKey)
    if (mayEntry.isEmpty) {
      if (_parent != null) {
	_parent.get_parameter_value_vertical(aKey, aPv)
      } else null
    } else {
      val entry = mayEntry.get
      aPv.addEntry(entry)
      if (entry.value != null) {
	aPv.value = entry.value
	aPv.value
      } else {
	if (_parent != null) {
	  _parent.get_parameter_value_vertical(aKey, aPv)
	} else null
      }
    }
  }

  final def getEntry(aKey: String): Option[GParameterEntry] = {
    val pv = getParameterValue(aKey)
    if (pv.value == null) None
    else Some(pv.entries.last)
  }

  final def get(aKey: String): Option[Any] = {
    val pv = getParameterValue(aKey)
    if (pv.value == null) None
    else Some(pv.value)
  }

  final def put(aKey: String, aValue: Any) = {
    _entries.put(get_real_key(aKey), new GParameterEntry(aKey, aValue, _category))
  }

  final def put(aKey: String, aValue: Any, aCategory: GParameterCategory) = {
    _entries.put(get_real_key(aKey), new GParameterEntry(aKey, aValue, aCategory))
  }

  final def setSubstitute(aKey: String, aSubstituteKey: String) {
    require (!is_alias_key(aKey), aKey)
    require (!is_alias_key(aSubstituteKey), aSubstituteKey)
    val entry = get_entry(aKey)
    entry.substituteKey = aSubstituteKey
  }

  private def get_entry(aKey: String): GParameterEntry = {
    val mayEntry = _entries.get(aKey)
    if (mayEntry.isDefined) mayEntry.get
    else {
      val entry = new GParameterEntry(aKey, null, _category)
      _entries.put(aKey, entry)
      entry
    }
  }

  final def setAlias(anAlias: String, aKey: String) {
    _aliases.put(anAlias, aKey)
  }

  private def get_real_key(aKey: String): String = {
    _aliases.get(aKey) match {
      case Some(key) => key
      case None => if (_parent == null) aKey
		   else _parent.get_real_key(aKey)
    }
  }

  private def is_alias_key(aKey: String) = {
    _aliases.get(aKey).isDefined
  }

  private def is_real_key(aKey: String) = {
    _entries.get(aKey).isDefined
  }

  //
  final def getString(aKey: String): Option[String] = {
    get(aKey) match {
      case None => None
      case Some(string: String) => Some(string)
      case Some(value) => Some(value.toString)
    }
  }

  final def getStrings(aKey: String): List[String] = {
    getString(aKey) match {
      case Some(s) => UString.getTokens(s, ";, ").toList
      case None => Nil
    }
  }

  def getBoolean(aKey: String): Boolean = {
    get(aKey) match {
      case None => false
      case Some(string: String) => string.toLowerCase() == "true"
      case Some(value: Boolean) => value
      case _ => false
    }
  }

  def getBooleanOption(aKey: String): Option[Boolean] = {
    get(aKey) collect {
      case string: String => string.toLowerCase() == "true"
      case value: Boolean => value
    }
  }

  final def getBooleans(aKey: String): List[Boolean] = {
    throw new UnsupportedOperationException(aKey)
  }

  final def getByte(key: String): Option[Byte] = {
    get(key) collect {
      case string: String => string.toByte
      case value: Byte => value
    }
  }

  final def getBytes(key: String): List[Byte] = {
    getStrings(key).map(_.toByte)
  }

  final def getShort(key: String): Option[Short] = {
    get(key) collect {
      case string: String => string.toShort
      case value: Short => value
    }
  }

  final def getShorts(key: String): List[Short] = {
    getStrings(key).map(_.toShort)
  }

  final def getInt(key: String): Option[Int] = {
    get(key) collect {
      case string: String => string.toInt
      case value: Int => value
    }
  }

  final def getInts(key: String): List[Int] = {
    getStrings(key).map(_.toInt)
  }

  final def getLong(key: String): Option[Long] = {
    get(key) collect {
      case string: String => string.toLong
      case value: Long => value
    }
  }

  final def getLongs(key: String): List[Long] = {
    getStrings(key).map(_.toLong)
  }

  final def getFloat(key: String): Option[Float] = {
    get(key) collect {
      case string: String => string.toFloat
      case value: Float => value
    }
  }

  final def getFloats(key: String): List[Float] = {
    getStrings(key).map(_.toFloat)
  }

  final def getDouble(key: String): Option[Double] = {
    get(key) collect {
      case string: String => string.toDouble
      case value: Double => value
    }
  }

  final def getDoubles(key: String): List[Double] = {
    getStrings(key).map(_.toDouble)
  }

  final def getInteger(key: String): Option[BigInt] = {
    get(key) collect {
      case string: String => BigInt(string)
      case value: BigInt => value
    }
  }

  final def getIntegers(key: String): List[BigInt] = {
    getStrings(key).map(BigInt(_))
  }

  final def getDecimal(key: String): Option[BigDecimal] = {
    get(key) collect {
      case string: String => BigDecimal(string)
      case value: BigDecimal => value
    }
  }

  final def getDecimals(key: String): List[BigDecimal] = {
    getStrings(key).map(BigDecimal(_))
  }

  final def getDate(key: String): Option[Date] = {
    get(key) collect {
      case string: String => sys.error("???")
      case value: Date => value
    }
  }

  final def getDates(key: String): List[Date] = {
    // getStrings(key).map(_.toDate)
    sys.error("???")
  }


  final def getTime(key: String): Option[Date] = {
    get(key) collect {
      case string: String => sys.error("???")

      case value: Date => value
    }
  }

  final def getTimes(key: String): List[Date] = {
//    getStrings(key).map(_.toDate)
    sys.error("???")
  }

  final def getDateTime(key: String): Option[Date] = {
    get(key) collect {
      case string: String => sys.error("???")
      case value: Date => value
    }
  }

  final def getDateTimes(key: String): List[Date] = {
//    getStrings(key).map(_.toDate)
    sys.error("???")
  }

  final def getUrls(aKey: String): Seq[URL] = {
    def string2urls(string: String) = { // XXX use -input option
      UString.getTokens(string, ";, ").map(UURL.getURLFromFileOrURLName)
    }

    get(aKey) match {
      case None => Nil
      case Some(null) => Nil
      case Some("") => Nil
      case Some(string: String) => string2urls(string)
      case Some(url: URL) => url :: Nil
      case Some(urls: Seq[_]) if urls.forall(_.isInstanceOf[URL]) => urls.asInstanceOf[Seq[URL]]
      case Some(value) => string2urls(value.toString)
    }
  }
}

class GParameterEntry(val key: String, aValue: Any, val category: GParameterCategory) {
  var value: Any = aValue
  var substituteKey: String = null
}

class GParameterValue(val key: String) {
  private val _entries = new ArrayBuffer[GParameterEntry]

  var value: Any = _
  final def entries: Seq[GParameterEntry] = _entries

  final def addEntry(aEntry: GParameterEntry) {
    _entries += aEntry
  }
}

class GParameterCategory

object Parameter_System extends GParameterCategory
object Parameter_Container extends GParameterCategory
object Parameter_Application extends GParameterCategory
object Parameter_User extends GParameterCategory
object Parameter_Startup extends GParameterCategory
object Parameter_Command extends GParameterCategory

object NullParameterRepository extends GParameterRepository
