/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2005  ASAMI, Tomoharu (asami@asamiOffice.com)
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

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ArrayMap
 *
 * @since   Dec. 28, 1998
 * @version May. 14, 2011
 * @author  ASAMI, Tomoharu (asami@asamioffice.com)
 * @param <K>
 */
public class ArrayMap<K,V> extends AbstractMap<K,V> {
    protected List<Map.Entry<K,V>> list_ = new ArrayList<Map.Entry<K,V>>();
    protected Set<Map.Entry<K,V>> entrySet_;

    public ArrayMap() {
        entrySet_ = new AbstractSet<Map.Entry<K,V>>() {
            public int size() {
                return (list_.size());
            }

            public Iterator<Map.Entry<K,V>> iterator() {
                return (list_.iterator());
            }
        };
    }

    @Override
    public V put(K key, V value) {
        Map.Entry<K,V> oldEntry = _getEntry(key);
        V oldValue;
        if (oldEntry == null) {
            oldValue = null;
            list_.add(new SimpleMapEntry<K,V>(key, value));
        } else {
            oldValue = oldEntry.getValue();
            oldEntry.setValue(value);
        }
        return (oldValue);
    }

    @Override
    public Set<Map.Entry<K,V>> entrySet() {
        return (entrySet_);
    }

    protected Map.Entry<K,V> _getEntry(K key) {
        int size = list_.size();
        if (key == null) {
            for (int i = 0; i < size; i++) {
                Map.Entry<K,V> entry = list_.get(i);
                if (entry.getKey() == null) {
                    return (entry);
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                Map.Entry<K,V> entry = list_.get(i);
                if (key.equals(entry.getKey())) {
                    return (entry);
                }
            }
        }
        return (null);
    }

    public V getNth(int index) {
        Map.Entry<K,V> entry = list_.get(index);
        return (entry.getValue());
    }

    @SuppressWarnings("unchecked")
    public int getIndex(Object key) {
        Object[] entries = list_.toArray();
        for (int i = 0;i < entries.length;i++) {
            Map.Entry<K, V> entry = (Entry<K, V>)entries[i];
            if (key.equals(entry.getKey())) {
                return i;
            }
        }
        return -1;
    }

    public List<V> getList() {
        List<V> list = new ArrayList<V>();
        for (Map.Entry<K,V> entry : list_) {
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * The Iterator does not support the remove method.
     * 
     * @return
     */
    public Iterator<V> getIterator() {
        return new Iterator<V>() {
            Iterator<Map.Entry<K,V>> iter = list_.iterator();

            public boolean hasNext() {
                return iter.hasNext();
            }

            public V next() {
                return iter.next().getValue();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public V[] toValueArray(V[] result) {
        Object[] entries = list_.toArray();
        for (int i = 0;i < entries.length;i++) {
            Map.Entry<K, V> entry = toMapEntry_(entries[i]);
            result[i] = entry.getValue();
        }
        return (result);
    }

    public Object[] toValueArray() {
        Object[] entries = list_.toArray();
        Object[] result = new Object[entries.length];
        for (int i = 0;i < entries.length;i++) {
            Map.Entry<K,V> entry = toMapEntry_(entries[i]);
            result[i] = entry.getValue();
        }
        return (result);
    }

    @SuppressWarnings("unchecked")
    public Map.Entry<K, V>[] toEntryArray() {
        return list_.toArray(new Map.Entry[list_.size()]);
    }

    @SuppressWarnings("unchecked")
    private Map.Entry<K, V> toMapEntry_(Object entry) {
        return (Map.Entry<K,V>)entry;
    }
}
