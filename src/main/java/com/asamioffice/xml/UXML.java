package com.asamioffice.xml;

/**
 * UXML
 *
 * @since   Jul.  1, 1998
 * @version Jan. 16, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UXML {
    public static String escape(String string) {
        if (string.indexOf('<') == -1 &&
            string.indexOf('>') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('"') == -1 &&
            string.indexOf('\'') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '<') {
                buffer.append("&lt;");
            } else if (c == '>') {
                buffer.append("&gt;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '"') {
                buffer.append("&quot;");
            } else if (c == '\'') {
                buffer.append("&apos;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeEntityQuot(String string) {
        if (string.indexOf('%') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('"') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '%') {
                buffer.append("&---;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '"') {
                buffer.append("&quot;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeEntityApos(String string) {
        if (string.indexOf('%') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('\'') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '%') {
                buffer.append("&#x25;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '\'') {
                buffer.append("&apos;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeAttrQuot(String string) {
        if (string.indexOf('<') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('"') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '<') {
                buffer.append("&lt;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '"') {
                buffer.append("&quot;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeAttrApos(String string) {
        if (string.indexOf('<') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('\'') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '<') {
                buffer.append("&lt;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '\'') {
                buffer.append("&apos;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeSystemQuot(String string) {
        if (string.indexOf('"') == -1) {
            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '"') {
                buffer.append("&quot;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeSystemApos(String string) {
        if (string.indexOf('\'') == -1) {
            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '\'') {
                buffer.append("&apos;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeCharData(String string) {
        if (string.indexOf('<') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('>') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '<') {
                buffer.append("&lt;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '>') {
                buffer.append("&gt;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String escapeCharDataCr(String string) {
        if (string.indexOf('<') == -1 &&
            string.indexOf('&') == -1 &&
            string.indexOf('>') == -1 &&
            string.indexOf('\r') == -1) {

            return (string);
        }
        StringBuffer buffer = new StringBuffer();
        int size = string.length();
        for (int i = 0;i < size;i++) {
            char c = string.charAt(i);
            if (c == '<') {
                buffer.append("&lt;");
            } else if (c == '&') {
                buffer.append("&amp;");
            } else if (c == '>') {
                buffer.append("&gt;");
            } else if (c == '\r') {
                buffer.append("&#xD;");
            } else {
                buffer.append(c);
            }
        }
        return (new String(buffer));
    }
}
