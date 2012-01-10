package com.asamioffice.util;

import java.io.File;

/**
 * UserPropertyParser
 *
 * @since   May.  3, 2003
 * @version Jun.  2, 2010
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class UserPropertyParser extends AbstractPropertyParser {
    public UserPropertyParser(String filename) {
        String dirName = System.getProperty("user.home");
        File dir = new File(dirName);
        File file = new File(dir, filename);
        load_properties(file);
    }
}
