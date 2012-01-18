/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2004  ASAMI, Tomoharu (tasami@ibm.net)
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * MultiValueMap
 *
 * @since   Dec. 18, 1999
 * @version May. 14, 20111
 * @author  ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class MultiValueMap<K, V> extends AbstractMap<K, List<V>> {
    protected Map<K, List<V>> map_ = new HashMap<K, List<V>>();

    public MultiValueMap() {
    }

    // AbstractMap
    public List<V> put(K key, List<V> value) {
        List<V> oldValue = get(key);
        List<V> list = new ArrayList<V>();
        list.addAll(value);
        map_.put(key, list);
        return (oldValue);
    }

    // AbstractMap
    public Set<Map.Entry<K, List<V>>> entrySet() {
        return (new AbstractSet<Entry<K, List<V>>>() {
            public int size() {
                return (map_.size());
            }

            public Iterator<Entry<K, List<V>>> iterator() {
                return (new Iterator<Entry<K, List<V>>>() {
                    Iterator<Entry<K, List<V>>> iter = map_.entrySet().iterator();

                    public boolean hasNext() {
                        return (iter.hasNext());
                    }

                    public Entry<K, List<V>> next() {
                        return iter.next();
                    }

                    public void remove() {
                        iter.remove();
                    }
                });
            }
        });
    }

    public void add(K key, V value) {
        List<V> list = map_.get(key);
        if (list == null) {
            list = new ArrayList<V>();
        }
        list.add(value);
        map_.put(key, list);
    }
}
