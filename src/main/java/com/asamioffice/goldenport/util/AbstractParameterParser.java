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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AbstractOptionParser
 *
 * @since   May.  3, 2003
 * @version Apr.  3, 2009
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class AbstractParameterParser implements IParameterParser {
    private String service_;
    private List<String> args_ = new ArrayList<String>();
    private Map<String, String> options_ = new HashMap<String, String>();
    private List<String> optionKeys_ = new ArrayList<String>();
    private Set<String> consumedKeys_ = new HashSet<String>();
    private Map<String, String> frameworkOptions_ = new HashMap<String, String>();
    private List<String> frameworkOptionKeys_ = new ArrayList<String>();
    private Set<String> consumedFrameworkKeys_ = new HashSet<String>();

    public String getService() {
        return (service_);
    }

    public int getArgumentCount() {
        return args_.size();
    }

    public Object getArgument(int index) {
        return args_.get(index);
    }

    public String[] getArguments() {
        String[] args = new String[args_.size()];
        return args_.toArray(args);
    }

    public String[] getOptionKeys() {
        String[] keys = new String[optionKeys_.size()];
        return optionKeys_.toArray(keys);
    }

    public boolean isOption(String key) {
        return (options_.containsKey(key) &&
                !"false".equals(options_.get(key)));
    }

    public String getOption(String key) {
        consumedKeys_.add(key);
        return options_.get(key);
    }
    
    public OptionEntry[] getOptions() {
        return getOptions_(options_);
    }

    public String[] getFrameworkOptionKeys() {
        String[] keys = new String[optionKeys_.size()];
        return frameworkOptionKeys_.toArray(keys);
    }

    public boolean isFrameworkOption(String key) {
        return (frameworkOptions_.containsKey(key) &&
                !"false".equals(frameworkOptions_.get(key)));
    }

    public String getFrameworkOption(String key) {
        consumedFrameworkKeys_.add(key);
        return frameworkOptions_.get(key);
    }

    protected KeyValueEntry<String, String>[] getFrameworkOptions() {
        return getOptions_(frameworkOptions_);
    }

    private OptionEntry[] getOptions_(Map options) {
        Object[] mapEntries = options.entrySet().toArray();
        OptionEntry[] entries = new OptionEntry[mapEntries.length];
        for (int i = 0;i < mapEntries.length;i++) {
            Map.Entry entry = (Map.Entry)mapEntries[i];
            entries[i] = new OptionEntry((String)entry.getKey(), (String)entry.getValue());
        }
        return entries;
    }

    protected final void _setService(String service) {
        service_ = service;
    }

    protected final void _addArgument(String arg) {
        args_.add(arg);
    }

    protected final void _setOption(String key, String value) {
        if (value == null) value = "";
        if (optionKeys_.contains(key)) {
            throw (new IllegalArgumentException());
        }
        options_.put(key, value);
        optionKeys_.add(key);
    }
    
    protected final void _setOptions(Map<String, String> options) {
        options_.putAll(options);
    }

    protected final void _setFrameworkOption(String key, String value) {
        if (frameworkOptionKeys_.contains(key)) {
            throw (new IllegalArgumentException());
        }
        frameworkOptions_.put(key, value);
        frameworkOptionKeys_.add(key);
    }
    
    protected final void _setFrameworkOptions(Map<String, String> options) {
        frameworkOptions_.putAll(options);
    }
}
