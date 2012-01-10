package com.asamioffice.util;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * ContextClassLoader
 *
 * @since   Jun. 13, 2006
 * @version Jun. 13, 2006
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class ContextClassLoader extends ClassLoader {
    private final List<ClassLoader> loaders_ = new ArrayList<ClassLoader>();
    public ContextClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addClassLoader(ClassLoader classLoader) {
        loaders_.add(classLoader);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        for (ClassLoader loader : loaders_) {
            Class<?> klass = loader.loadClass(name);
            if (klass != null) {
                return klass;
            }
        }
        return super.findClass(name);
    }

    @Override
    protected URL findResource(String name) {
        for (ClassLoader loader : loaders_) {
            URL url = loader.getResource(name);
            if (url != null) {
                return url;
            }
        }
        return super.findResource(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
        Vector<URL> urls = new Vector<URL>();
        for (ClassLoader loader : loaders_) {
            urls.addAll(Collections.list(loader.getResources(name)));
        }
        return urls.elements();
    }
}
