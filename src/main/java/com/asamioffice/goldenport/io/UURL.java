package com.asamioffice.goldenport.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * UURL is a utility for URL.
 *
 * @since   Mar. 23, 1998
 * @version Jan. 18, 2009
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public final class UURL {
    /**
     * Convert file name or URL name to URL. Name's kind is detected
     * automatically.
     *
     * @param name  file name or URL name.
     * @return URL
     */
    public static URL getURLFromFileOrURLName(String name)
        throws MalformedURLException {

        int index = name.indexOf(':');
        if (index != -1) {
            String protocol = name.substring(0, index);
            if (protocol.length() == 1) {
                return getURLFromFileName(name);
            }
        }
        try {
            //      if (name.startsWith("jdbc:")) {
            //          return (new URL(null, name, new JDBCStreamHandler()));
            //      }
            return (new URL(name));
        } catch (MalformedURLException e) {
        }
        return (getURLFromFileName(name));
    }

    /**
     * Convert file name to URL.
     *
     * @param filename  file name to convert
     * @return URL
     * @exception MalformedURLException filename is not valid file name form.
     */
    public static URL getURLFromFileName(String filename)
        throws MalformedURLException {

        return (getURLFromFile(new File(filename)));
    }

    /**
     * Convert file name to URL.
     *
     * @param file  file to convert
     * @return URL
     */
    public static URL getURLFromFile(File file) throws MalformedURLException {
        URL url = file.toURL();
        String name = url.toExternalForm();
        if (name.indexOf('%') == -1) {
            return (url);
        } else { // avoids File's bug
            StringBuffer sb = new StringBuffer();
            char[] chars = name.toCharArray();
            for (int i = 0;i < chars.length;i++) {
                char c = chars[i];
                if (c == '%') {
                    sb.append("%25");
                } else {
                    sb.append(c);
                }
            }
            return (new URL(sb.toString()));
        }
    }


    /**
     * Convert resource name to URL.
     * <br>
     * This method uses a ClassLoader specified by the parameter clazz
     * to access the resource.
     *
     * @param resourceName  resource name to convert
     * @param clazz  class related resource.
     * @return URL
     */
    public static URL getURLFromResourceName(
        String resourceName,
        ClassLoader loader
    ) {
        URL url = loader.getResource(resourceName);
        if (url != null) {
            return (url);
        }
	if (resourceName.startsWith("/")) {
	    resourceName = resourceName.substring(1);
	} else {
            resourceName = "/" + resourceName;
        }
        return (loader.getResource(resourceName));
    }

    /**
     * Convert resource name to URL.
     * <br>
     * This method uses a ClassLoader specified by the parameter clazz
     * to access the resource.
     *
     * @param resourceName  resource name to convert
     * @param clazz  class related resource.
     * @return URL
     */
    public static URL getURLFromResourceName(
        String resourceName,
        Class clazz) {
        return (getURLFromResourceName(resourceName, clazz.getClassLoader()));
    }

    /**
     * Convert resource name to URL.
     * <br>
     * This method uses a ClassLoader specified by the parameter object
     * to access the resource.
     *
     * @param resourceName  resource name to convert
     * @param object  object related resource.
     * @return URL
     */
    public static URL getURLFromResourceName(
        String resourceName,
        Object object) {
        return (getURLFromResourceName(resourceName, object.getClass()));
    }

    public static File getFileFromFileNameOrURLName(String name) {
        File file = getActiveFile(name);
        if (file != null) {
            return (file);
        }
        return (new File(name));
    }

    public static boolean isURL(String src) {
        try {
            URL url = new URL(src);
            return (true);
        } catch (MalformedURLException e) {
            return (false);
        }
    }

    public static File getActiveFile(String uri) {
        try {
            return (getActiveFile(new URL(uri)));
        } catch (MalformedURLException e) {
            return (null);
        }
    }

    public static File getActiveFile(URI uri) {
        try {
            return getActiveFile(uri.toURL());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public static File getActiveFile(URL url) {
        String protocol = url.getProtocol();
        if (protocol.length() == 1) { // windows drive letter
            return new File(url.toExternalForm());
        } else if ("file".equals(protocol)) {
            return (new File(url.getFile()));
        } else {
            return (null);
        }
    }
}
