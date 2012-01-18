/*
 * The RelaxerOrg class library
 *  Copyright (C) 1997-2004  ASAMI, Tomoharu (asami@relaxer.org)
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

package com.asamioffice.goldenport.util;

/**
 * IOptionParser
 *
 * @since   May.  3, 2003
 * @version Aug. 29, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public interface IParameterParser {
    boolean isFrameworkOption(String key);
    String getFrameworkOption(String key);
    boolean isOption(String key);
    String getOption(String key);
    OptionEntry[] getOptions();
    String getService();
    String[] getArguments();

    public static class OptionEntry extends KeyValueEntry<String, String> {
        public OptionEntry(String key) {
            super(key);
        }

        public OptionEntry(String key, String value) {
            super(key, value);
        }
    }
}
