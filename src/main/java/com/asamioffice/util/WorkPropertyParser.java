package com.asamioffice.util;

import java.io.File;

/**
 * WorkPropertyParser
 *
 * @since   May.  3, 2003
 * @version Jun. 17, 2010
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class WorkPropertyParser extends AbstractPropertyParser {
    public WorkPropertyParser(String filename) {
        File file = new File(filename);
        load_properties(file);
    }
}
