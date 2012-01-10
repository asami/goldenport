/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2004  ASAMI, Tomoharu (asami@asamiOffice.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.asamioffice.util;

import java.util.*;

import com.asamioffice.text.UString;

/**
 * ULocale
 *
 * @since   Oct. 15, 1998
 * @version Sep. 18, 2008 (Feb.  5, 2004)
 * @author  ASAMI, Tomoharu (asami@asamiOffice)
 */
public class ULocale {
    public static Locale makeLocale(String name) {
        StringTokenizer st = new StringTokenizer(name, "_");
        switch (st.countTokens()) {
            case 1 :
                return (new Locale(st.nextToken(), "", ""));
            case 2 :
                return (new Locale(st.nextToken(), st.nextToken(), ""));
            case 3 :
                return (
                    new Locale(st.nextToken(), st.nextToken(), st.nextToken()));
            default :
                throw (new InternalError());
        }
    }

    public static String makeLabel(String label, Locale locale) {
        if (label.length() == 0) {
            return (label);
        }
        char ch = label.charAt(0);
        if (Character.isUpperCase(ch)) {
            return (label);
        }
        // XXX : more I18N
        char[] chars = label.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]); // XXX
        return (new String(chars));
    }

    public static boolean match(
        Locale master,
        Locale target,
        Locale candidate
    ) {
        if (master.equals(target)) {
            return (true);
        }
        String language = master.getLanguage();
        if (!language.equals(target.getLanguage())) {
            return (false);
        }
        String country = master.getCountry();
        String variant = master.getVariant();
        if (UString.isNull(country)) {
            return (true);
        } else if (UString.isNull(variant)) {
            String targetCountry = target.getCountry();
            if (UString.isNull(targetCountry)) {
                if (candidate != null) {
                    String candidateCountry = candidate.getCountry();
                    return (!country.equals(candidateCountry));
                } else {
                    return (true);
                }
            } else {
                return (country.equals(targetCountry));
            }
        }
        return (false);
    }
}
