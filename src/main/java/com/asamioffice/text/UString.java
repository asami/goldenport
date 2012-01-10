package com.asamioffice.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * String utility
 *
 * @since   Apr. 18, 1998
 * @version Dec. 20, 2006
 * @version Dec.  5, 2011
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UString {
    public static boolean isNull(CharSequence cs) { // 2011-12-05
        return (cs == null || cs.length() == 0);
    }

    public static boolean isNull(String string) {
        return (string == null || string.equals(""));
    }

    public static boolean notNull(String string) {
        return (string != null && !string.equals(""));
    }

    /**
     * Null string Canonicalization
     */
    public static String checkNull(String string) {
        if (string == null || string.equals("")) {
            return (null);
        } else {
            return (string);
        }
    }

    public static String[] getTokens(String text) {
        return (getTokens(text, " \t\n\r"));
    }

    public static String[] getTokens(String text, String delim) {
        StringTokenizer st = new StringTokenizer(text, delim);
        String[] list = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            list[i++] = st.nextToken();
        }
        return (list);
    }

    public static List<? extends String> getTokenList(String text, String delim) {
        return Arrays.asList(getTokens(text, delim));
    }

    public static String[] getLineList(String string) {
        // 0 : init
        // 1 : after cr
        // 2 : after lf
        // 3 : after crlf
        // 4 : after normal
        int status = 0;
        List<String> list = new ArrayList<String>();
        int size = string.length();
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < size; i++) {
            char c = string.charAt(i);
            switch (status) {

                case 0 :
                    if (c == '\r') {
                        status = 1;
                    } else if (c == '\n') {
                        status = 2;
                    } else {
                        buffer.append(c);
                        status = 4;
                    }
                    break;
                case 1 :
                    if (c == '\r') {
                        list.add(new String(buffer));
                        buffer = new StringBuilder();
                        status = 1;
                    } else if (c == '\n') {
                        status = 3;
                    } else {
                        list.add(new String(buffer));
                        buffer = new StringBuilder();
                        buffer.append(c);
                        status = 4;
                    }
                    break;
                case 2 :
                    if (c == '\r') {
                        // illegal sequence
                        status = 1;
                        throw (new InternalError("debug"));
                    } else if (c == '\n') {
                        list.add(new String(buffer));
                        buffer = new StringBuilder();
                        status = 2;
                    } else {
                        list.add(new String(buffer));
                        buffer = new StringBuilder();
                        buffer.append(c);
                        status = 4;
                    }
                    break;
                case 3 :
                    if (c == '\r') {
                        list.add(new String(buffer));
                        buffer = new StringBuilder();
                        status = 1;
                    } else if (c == '\n') {
                        // illegal sequence
                        status = 2;
                        throw (new InternalError("debug"));
                    } else {
                        list.add(new String(buffer));
                        buffer = new StringBuilder();
                        buffer.append(c);
                        status = 4;
                    }
                    break;
                case 4 :
                    if (c == '\r') {
                        status = 1;
                    } else if (c == '\n') {
                        status = 2;
                    } else {
                        buffer.append(c);
                        status = 4;
                    }
                    break;
                default :
                    throw (new InternalError());
            }
        }
        if (status != 0) {
            list.add(new String(buffer));
        }
        String[] strings = new String[list.size()];
        return ((String[])list.toArray(strings));
    }

    public static String replace(String template, String pattern, String replacer) {
        int start = template.indexOf(pattern);
        if (start == -1) {
            return (template);
        }
        StringBuilder buffer = new StringBuilder(template);
        buffer.replace(start, start + pattern.length(), replacer);
        return (new String(buffer));
    }

    public static String truncate(String template, String pattern) {
        int start = template.indexOf(pattern);
        if (start == -1) {
            return (template);
        }
        StringBuilder buffer = new StringBuilder(template);
        buffer.delete(start, start + pattern.length());
        return (new String(buffer));
    }

    public static String capitalize(String name) {
        if (name.length() == 0) {
            return (name);
        }
        if (Character.isUpperCase(name.charAt(0))) {
            return (name);
        }
        char[] data = name.toCharArray();
        data[0] = Character.toUpperCase(data[0]);
        return (new String(data));
    }

    public static String uncapitalize(String name) {
        if (name.length() == 0) {
            return (name);
        }
        if (Character.isLowerCase(name.charAt(0))) {
            return (name);
        }
        char[] data = name.toCharArray();
        data[0] = Character.toLowerCase(data[0]);
        return (new String(data));
    }

    public static boolean contains(String string, char c) {
        return string.indexOf(c) != -1;
    }

    public static int[] string2CodePoints(String string) {
        char[] chars = string.toCharArray();
        int[] codePoints = new int[string.codePointCount(0, chars.length)];
        int index = 0;
        while (chars.length > index) {
            char c = chars[index];
            if (Character.isHighSurrogate(c)) {
                codePoints[index++] = c;
                codePoints[index] = chars[index]; // LowSurrogate
                index++;
            } else {
                codePoints[index] = chars[index];
                index++;
            }
        }
        return codePoints;
    }
}
