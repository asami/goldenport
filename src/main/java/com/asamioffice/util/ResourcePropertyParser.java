package com.asamioffice.util;

import java.net.URL;

/**
 * ResourcePropertyParser
 *
 * @since   May.  3, 2003
 * @version Oct. 11, 2010
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class ResourcePropertyParser extends AbstractPropertyParser {
    public ResourcePropertyParser(String name, Object object) {
        URL url = object.getClass().getResource(name);
        if (url != null) {
            load_properties(url);
        }
    }
}
