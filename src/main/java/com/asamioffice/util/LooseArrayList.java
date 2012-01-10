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

package com.asamioffice.util;

import java.util.*;

/**
 * LooseList
 *
 * @since   Jan. 21, 2002
 * @version Jul. 20, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class LooseArrayList<E> extends ArrayList<E> {
    private static final long serialVersionUID = 843380636432331474L;

    public E put(int index, E element) {
        int size = size();
        if (size <= index) {
            for (int i = size; i <= index; i++) {
                add(null);
            }
        }
        return (set(index, element));
    }
}
