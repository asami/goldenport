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

import java.util.ArrayList;
import java.util.List;

/**
 * MultiUniqueValueMap
 *
 * @since   Dec.  1, 2006
 * @version Dec.  1, 2006
 * @author  ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class MultiUniqueValueMap<K, V> extends MultiValueMap<K, V> {
    public void add(K key, V value) {
        List<V> list = map_.get(key);
        if (list == null) {
            list = new ArrayList<V>();
        }
        if (!list.contains(value)) {
            list.add(value);
            map_.put(key, list);
        }
    }
}
