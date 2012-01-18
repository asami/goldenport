/*
 * The JabaJaba class library
 *  Copyright (C) 1997-1998  ASAMI, Tomoharu (tasami@ibm.net)
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

import java.util.Map;

/**
 * SimpleArrayEntry
 * 
 * @since   Dec. 28, 1998
 * @version Jan. 14, 2006
 * @author ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class SimpleMapEntry<K,V> implements Map.Entry<K,V> {
    protected int hash_;

    protected K key_;

    protected V value_;

    public SimpleMapEntry(K key) {
        key_ = key;
    }

    public SimpleMapEntry(K key, V value) {
        key_ = key;
        value_ = value;
    }

    public K getKey() {
        return (key_);
    }

    public V getValue() {
        return (value_);
    }

    public V setValue(V value) {
        V oldValue = value_;
        value_ = value;
        return (oldValue);
    }

    public boolean equals(Object o) {
        if (!(o instanceof Map.Entry)) {
            return (false);
        }
        Map.Entry entry = (Map.Entry)o;
        Object key = entry.getKey();
        if (key_ == null) {
            if (key != null) {
                return (false);
            }
        } else {
            if (!key_.equals(key)) {
                return (false);
            }
        }
        Object value = entry.getValue();
        if (value_ == null) {
            if (value != null) {
                return (false);
            }
        } else {
            if (!value_.equals(value)) {
                return (false);
            }
        }
        return (true);
    }

    public int hashCode() {
        if (value_ == null) {
            return (0);
        }
        return (value_.hashCode());
    }

    public String toString() {
        return (key_.toString() + "=" + value_.toString());
    }
}
