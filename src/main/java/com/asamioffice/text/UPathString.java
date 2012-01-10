package com.asamioffice.text;

import java.util.*;
import com.asamioffice.io.UFile;
import com.asamioffice.io.UURL;

/**
 * String utility
 *
 * @since   Nov. 25, 2006
 * @version Dec. 12, 2010
 * @author  ASAMI, Tomoharu
 */
public class UPathString {
    public static String getLastComponent(String path) {
        return (getLastComponent(path, "/"));
    }

    public static String getLastComponent(String path, String delimiter) {
        int delimSize = delimiter.length();
        if (path.endsWith(delimiter)) {
            path = path.substring(0, path.length() - delimSize);
        }
        int index = path.lastIndexOf(delimiter);
        if (index == -1) {
            return (path);
        } else {
            return (path.substring(index + delimSize));
        }
    }

    public static String getLastComponentBody(String path) {
        return (getLastComponentBody(path, "/"));
    }

    public static String getLastComponentBody(String path, String delimiter) {
        int delimSize = delimiter.length();
        if (path.endsWith(delimiter)) {
            path = path.substring(0, path.length() - delimSize);
        }
        int first;
        int last;
        first = path.lastIndexOf(delimiter);
        if (first == -1) {
            first = 0;
        } else {
            first++;
        }
        last = path.lastIndexOf(".");
        if (last == -1 || last <= first) {
            return (path.substring(first));
        } else {
            return (path.substring(first, last));
        }
    }

    public static String getContainerPathname(String path) {
        return (getContainerPathname(path, "/"));
    }

    public static String getContainerPathname(String path, String delimiter) {
        int delimSize = delimiter.length();
        if (path.endsWith(delimiter)) {
            path = path.substring(0, path.length() - delimSize);
        }
        int index = path.lastIndexOf(delimiter);
        if (index == -1) {
            return (null);
        } else {
            return (path.substring(0, index));
        }
    }

    public static String concatPathname(String parent, String child) {
        if (parent == null) {
            return (child);
        }
        if (UFile.isAbsolutePath(child)) {
            return (child);
        }
        if (UURL.isURL(child)) {
            return (child);
        }
        String root;
        if (parent.startsWith("/")) {
            root = "/";
        } else {
            root = "";
        }
        List comps = new ArrayList(Arrays.asList(UString.getTokens(parent, "/")));
        String[] childComps = UString.getTokens(child, "/");
        for (int i = 0; i < childComps.length; i++) {
            String comp = childComps[i];
            if (".".equals(comp)) {
                // do nothing
            } else if ("..".equals(comp)) {
                if (comps.size() > 0) {
                    comps.remove(comps.size() - 1);
                }
            } else {
                comps.add(comp);
            }
        }
        Object[] names = comps.toArray();
        if (names.length == 0) {
            return ("");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(root);
        sb.append(names[0]);
        for (int i = 1; i < names.length; i++) {
            sb.append("/");
            sb.append(names[i]);
        }
        return (new String(sb));
    }

    public static boolean isSuffix(String file, String suffix) {
        return (suffix.equals(getSuffix(file)));
    }

    public static boolean isSuffix(String file, String[] suffixes) {
        for (int i = 0; i < suffixes.length; i++) {
            if (isSuffix(file, suffixes[i])) {
                return (true);
            }
        }
        return (false);
    }

    public static String getRelativePathname(String uri) {
        String dir = System.getProperty("user.dir");
        if (dir == null) {
            throw (new InternalError());
        }
        dir = dir.replace('\\', '/'); // XXX
        if (!dir.endsWith("/")) {
            dir = dir + "/";
        }
        int index = uri.indexOf(dir);
        if (index == -1) {
            return (getLastComponent(uri)); // XXX
        } else {
            return (uri.substring(index + dir.length()));
        }
    }

    public static String getPathnameBody(String pathname) {
        char[] chars = pathname.toCharArray();
        for (int index = chars.length - 1;index > 0;index--) { // excluding 0 means that excluding first character is period
            switch (chars[index]) {
            case '/':
                return pathname;
            case '\\':
                return pathname;
            case '.':
                switch (chars[index - 1]) {
                case '/':
                case '\\':
                    return pathname;
                default:
                    return pathname.substring(0, index);
                }
            }
        }
        return pathname;
    }

    public static String getSuffix(String name) {
        if (name == null) {
            return null;
        }
        char[] chars = name.toCharArray();
        for (int index = chars.length - 1;index > 0;index--) { // excluding 0 means that excluding first character is period
            switch (chars[index]) {
            case '/':
                return null;
            case '\\':
                return null;
            case '.':
                switch (chars[index - 1]) {
                case '/':
                case '\\':
                    return null;
                default:
                    return name.substring(index + 1);
                }
            }
        }
        return null;
    }

    public static String changeSuffix(String name, String suffix) {
        String oldSuffix = getSuffix(name);
        if (oldSuffix == null) {
            return name + "." + suffix;
        }
        StringBuilder buffer = new StringBuilder(name); 
        buffer.delete(name.length() - oldSuffix.length(), name.length());
        buffer.append(suffix);
        return (buffer.toString());
    }
}
