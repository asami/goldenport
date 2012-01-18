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

package com.asamioffice.goldenport.util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * UArray
 *
 * @since   Feb.  8, 1998
 * @version Nov.  3. 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UArray {
    public static boolean contains(Object[] objects, Object value) {
        for (Object object : objects) {
            if (value.equals(object)) {
                return true;
            }
        }
        return false;
    }

    public static void addAll(Collection collection, Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            collection.add(objects[i]);
        }
    }

    public static Object[] cloneArrayFast(Object[] array) {
        Object[] newArray =
            (Object[])Array.newInstance(
                array.getClass().getComponentType(),
                array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return (newArray);
    }

    public static Object[] cloneArray(Object[] array) {
        Object[] cloned =
            (Object[])Array.newInstance(
                array.getClass().getComponentType(),
                array.length);
        // array.clone(); // XXX : clone?
        int size = array.length;
        for (int i = 0; i < size; i++) {
            if (array[i] instanceof Object[]) {
                cloned[i] = cloneArray((Object[])array[i]);
            } else {
                cloned[i] = array[i];
            }
        }
        return (cloned);
    }

    public static void printArray(Object[] array) {
        printArray(array, System.out);
    }

    public static void printArray(Object[] array, OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        _printArray(array, writer);
        writer.flush();
    }

    public static void printlnArray(Object[] array) {
        printlnArray(array, System.out);
    }

    public static void printlnArray(Object[] array, OutputStream out) {
        PrintWriter writer = new PrintWriter(out, true);
        _printArray(array, writer);
        writer.println();
    }

    public static String[] collection2StringArray(Collection collection) {
        int size = collection.size();
        String[] array = new String[size];
        Iterator iter = collection.iterator();
        int i = 0;
        while (iter.hasNext()) {
            array[i++] = (String)iter.next();
        }
        return (array);
    }

    public static String array2String(Object[] array) {
        StringBuffer buffer = new StringBuffer();
        _array2String(array, buffer);
        return (new String(buffer));
    }

    private static void _printArray(Object[] array, PrintWriter writer) {
        writer.print("{");
        for (int i = 0; i < array.length; i++) {
            Object element = array[i];
            if (element instanceof Object[]) {
                _printArray((Object[])array[i], writer);
            } else {
                writer.print(element);
            }
            if (i < array.length - 1) {
                writer.print(", ");
            }
        }
        writer.print("}");
    }

    private static void _array2String(Object[] array, StringBuffer buffer) {
        buffer.append("{");
        for (int i = 0; i < array.length; i++) {
            Object element = array[i];
            if (element instanceof Object[]) {
                _array2String((Object[])array[i], buffer);
            } else {
                buffer.append(element.toString());
            }
            if (i < array.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append("}");
    }

    // test driver
    public static void main(String[] args) {
        // test for cloneArray, printArray
        Object[][][] strings = {
            {
                { "one", "1", "ichi" }
            }, {
                { "two", "2", "ni" },
                { "three", "3", "san" }
            }
        };
        printlnArray(strings);
        Object[] cloned = cloneArray(strings);
        printlnArray(cloned);
        strings[0] = new String[][] {
            { "number 1" }
        };
        strings[1][0] = new String[] { "number 2" };
        printlnArray(strings);
        printlnArray(cloned);
    }
}
