package com.asamioffice.text;

/**
 * String utility for java meta model
 *
 * @since   Mar. 25, 2006
 * @version Mar. 24, 2009
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UJavaString {
    public static String makeClassName(String name) {
        return (UString.capitalize(makeJavaName(name)));
    }

    public static String makeVariableName(String name) {
        char[] data = name.toCharArray();
        if (data.length == 0) {
            return (name);
        }
        boolean allUpperCase = true;
        for (int i = 0; i < data.length; i++) {
            char c = data[i];
            if (c == '-' || c == ':' || c == '.') {
                data[i] = '_';
            } else if (c == '_') {
                // do nothing
            } else if (!Character.isUpperCase(c)) {
                allUpperCase = false;
            }
        }
        if (allUpperCase) {
            return (new String(data));
        }
        data[0] = Character.toLowerCase(data[0]);
        return (new String(data));
    }

    public static String makeGetterName(String name) {
        return "get" + UString.capitalize(name);
    }

    public static String makeIserName(String name) {
        return "is" + UString.capitalize(name);
    }

    public static String makeSetterName(String name) {
        return "set" + UString.capitalize(name);
    }

    public static String makeJavaName(String name) {
        char[] data = name.toCharArray();
        if (data.length == 0) {
            return (name);
        }
        for (int i = 0; i < data.length; i++) {
            char c = data[i];
            if (c == '-' || c == ':' || c == '.') {
                data[i] = '_';
            }
        }
        return (new String(data));
    }

    public static String escapeJavaText(String text) {
        if (text.indexOf('\n') == -1
            && text.indexOf('\r') == -1
            && text.indexOf('\t') == -1
            && text.indexOf('"') == -1) {

            return (text);
        }
        StringBuffer buffer = new StringBuffer();
        int size = text.length();
        for (int i = 0; i < size; i++) {
            char c = text.charAt(i);
            switch (c) {

                case '\n' :
                    buffer.append("\\n");
                    break;
                case '\r' :
                    buffer.append("\\r");
                    break;
                case '\t' :
                    buffer.append("\\t");
                    break;
                case '"' :
                    buffer.append("\\\"");
                    break;
                default :
                    buffer.append(c);
            }
        }
        return (new String(buffer));
    }

    public static String getPropertyName(String name) {
        if (name.startsWith("get") || name.startsWith("set")) {
            return UString.uncapitalize(name.substring(3));
        } else if (name.startsWith("is")) {
            return UString.uncapitalize(name.substring(2));
        } else {
            throw new IllegalArgumentException(name);
        }
    }

    public static String className2pathname(String className) {
        return "/" + className.replace('.', '/');
    }

    public static String packageName2pathname(String packageName) {
        return "/" + packageName.replace('.', '/');
    }

    public static String pathname2className(String pathname) {
        return pathname2classifierName(pathname);
    }

    public static String pathname2classifierName(String pathname) {
        if (pathname.startsWith("/")) {
            pathname = pathname.substring(1);
        }
        return pathname.replace('/', '.');
    }

    public static String pathname2packageName(String pathname) {
        if (pathname.startsWith("/")) {
            pathname = pathname.substring(1);
        }
        int index = pathname.lastIndexOf('/');
        if (index == -1) {
            return null;
        } else {
            return pathname2className(pathname.substring(0, index));
        }
    }

    public static String getComponentClassName(String name) throws IllegalArgumentException {
        int index = name.indexOf('<');
        if (index == -1) {
            return name;
        }
        int end = name.lastIndexOf('>');
        if (end == -1) {
            throw new IllegalArgumentException(name);
        }
        return name.substring(index + 1, end);
    }

    public static String getContainerClassName(String name) {
        int index = name.indexOf('<');
        if (index == -1) {
            return name;
        }
        return name.substring(0, index);
    }

    public static String qname2packageName(String name) {
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return "";
        } else {
            return name.substring(0, index);
        }
    }

    public static String qname2simpleName(String name) {
        int index = name.lastIndexOf(".");
        if (index == -1) {
            return name;
        } else {
            return name.substring(index + 1);
        }
    }
}
