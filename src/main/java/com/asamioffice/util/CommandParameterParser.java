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

import java.util.Map;

/**
 * CommandParameterParser
 *
 * @since   May.  3, 2003
 * @version Aug. 29, 2008
 * @version Nov. 10, 2011
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class CommandParameterParser extends AbstractParameterParser {
    public CommandParameterParser(String[] args) {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("---")) {
                // XXX
            } else if (arg.startsWith("--")) {
                KeyValue pair = _getKeyValue(arg);
                _setFrameworkOption(pair.key, pair.value);
            } else if (arg.startsWith("-")) {
                KeyValue pair = _getKeyValue(arg);
                String key = pair.key;
                String value = pair.value;
                _setOption(key, value);
                if (getService() == null && value == null) {
                    _setService(key);
                }
            } else {
                _addArgument(arg);
            }
        }
    }

    public CommandParameterParser(Map<String, String> frameworkOptions) {
        _setFrameworkOptions(frameworkOptions);
    }
    
    public CommandParameterParser(Map<String, String> frameworkOptions, Map<String, String> options) {
        if (frameworkOptions != null) {
            _setFrameworkOptions(frameworkOptions);
        }
        _setOptions(options);
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

    public Object getArgument(int index) {
        throw new UnsupportedOperationException("CommandParameterParser.getArgument");
    }
}
