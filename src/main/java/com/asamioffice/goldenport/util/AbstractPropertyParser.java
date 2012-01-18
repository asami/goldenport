package com.asamioffice.goldenport.util;

import java.net.URL;
import java.util.*;
import java.io.*;

/**
 * AbstractPropertyParser
 *
 * @since   May.  3, 2003
 * @version Oct. 11, 2010
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class AbstractPropertyParser extends AbstractParameterParser {
    protected final List log_ = new ArrayList();
    protected IOException checked_exception = null;

    protected AbstractPropertyParser() {
    }

    protected final void load_properties(File file) {
        log_.add("Load properties from '" + file + "'");
        try {
            load_properties(new FileInputStream(file));
        } catch (IOException e) {
            checked_exception = e;
        }
    }

    protected final void load_properties(URL url) {
        log_.add("Load properties from '" + url + "'");
        try {
            load_properties(url.openStream());
        } catch (IOException e) {
            checked_exception = e;
        }
    }

    protected final void load_properties(InputStream is) {
        BufferedReader in = null;
        try {
            //System.out.println("File '" + file + "'");
            in = new BufferedReader(new InputStreamReader(is, "utf-8"));
            //System.out.println("Load properties from '" + file + "'");
            String line;
            while ((line = in.readLine()) != null) {
                if (line.length() == 0) {
                    continue;
                }
                if (line.charAt(0) == '#') {
                    continue;
                }
                int index = line.indexOf("=");
                if (index >= 0) {
                    String key = line.substring(0, index).trim();
                    String value = line.substring(index + 1).trim();
                    //System.out.println("AbstractPropertyParser: " + key + "," + value);
                    _setOption(key, value);
                }
            }
        } catch (IOException e) {
            //System.out.println(e.toString() + "/" + e.getMessage());
            checked_exception = e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ee) {
                }
            }
        }
    }
}
