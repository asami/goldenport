package com.asamioffice.util;

import java.util.GregorianCalendar;

/**
 * UDateTime
 *
 * @since   Apr.  3, 2009
 * @version Apr.  3, 2009
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UDateTime {
    public static String getCurrentDateTimeMillString() {
        return String.format("%1$tY%1$tm%1$td%1$tH%1$tM%1$tS%1$tL",
                             new GregorianCalendar());
    }

    public static String getCurrentDateTimeMillXmlString() {
        return String.format("%1$tY-%1$tm-%1$tdT%1$tH:%1$tM:%1$tS.%1$tL",
                             new GregorianCalendar());
    }
}
