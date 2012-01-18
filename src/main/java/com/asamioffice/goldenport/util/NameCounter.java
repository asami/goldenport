/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2004  ASAMI, Tomoharu (asami@AsamiOffice.com)
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

import java.util.*;

/**
 * NameCounter
 *
 * @since   Feb.  4, 2000
 * @version Jan. 16, 2004
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class NameCounter {
    private Map elements_ = new HashMap();
    private boolean ignoreCase_ = false;

    public void setIgnoreCase(boolean ignore) {
        ignoreCase_ = ignore;
    }

    public void add(String name) {
        if (ignoreCase_) {
            name = name.toUpperCase();
        }
        Integer counter = (Integer)elements_.get(name);
        if (counter != null) {
            Integer updated = new Integer(counter.intValue() + 1);
            elements_.put(name, updated);
        } else {
            elements_.put(name, new Integer(1));
        }
    }

    public int getCount(String name) {
        if (ignoreCase_) {
            name = name.toUpperCase();
        }
        Integer counter = (Integer)elements_.get(name);
        if (counter == null) {
            return (0);
        } else {
            return (counter.intValue());
        }
    }
}
