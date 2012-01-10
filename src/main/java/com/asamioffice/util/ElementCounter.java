/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2003  ASAMI, Tomoharu (asami@AsamiOffice.com)
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

/**
 * ElementCounter
 *
 * @since   Feb.  4, 2000
 * @version Oct. 15, 2003
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class ElementCounter {
    private Map elements_ = new HashMap();

    public void add(Object object) {
        Integer counter = (Integer)elements_.get(object);
        if (counter != null) {
            Integer updated = new Integer(counter.intValue() + 1);
            elements_.put(object, updated);
        } else {
            elements_.put(object, new Integer(1));
        }
    }

    public int getCount(Object object) {
        Integer counter = (Integer)elements_.get(object);
        if (counter == null) {
            return (0);
        } else {
            return (counter.intValue());
        }
    }

    public Object[] getMax() {
        List list = new ArrayList();
        int number = 0;
        Object[] keys = elements_.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            Integer counter = (Integer)elements_.get(key);
            int count = counter.intValue();
            if (count > number) {
                list.clear();
                list.add(key);
                number = count;
            } else if (count == number) {
                list.add(key);
            }
        }
        return ((Object[])list.toArray());
    }

    public Object getMaxOne() {
        Object[] result = getMax();
        if (result.length == 0) {
            return (null);
        } else {
            return (result[0]);
        }
    }
}
