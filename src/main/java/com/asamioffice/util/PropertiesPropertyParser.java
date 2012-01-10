/*
 * The RelaxerOrg class library
 *  Copyright (C) 1997-2003  ASAMI, Tomoharu (asami@relaxer.org)
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

import java.io.File;

/**
 * PropertiesParameterParser
 *
 * @since   Dec. 18, 2003
 * @version Aug. 29, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class PropertiesPropertyParser extends AbstractParameterParser {
    public PropertiesPropertyParser(File file) {
    }

    private KeyValue _getKeyValue(String arg) {
        KeyValue pair = new KeyValue();
        int start = _getStart(arg);
        int index = arg.indexOf(":", start);
        if (index == -1) {
            index = arg.indexOf("=", start);
        }
        if (index == -1) {
            pair.key = arg.substring(start);
            pair.value = null;
        } else {
            pair.key = arg.substring(start, index);
            pair.value = arg.substring(index + 1);
        }
        return (pair);
    }

    private int _getStart(String arg) {
        int i = 0;
        for (;;) {
            if (arg.charAt(i) != '-') {
                return (i);
            }
            i++;
        }
    }

    private static class KeyValue {
        public String key;
        public String value;
    }
}
