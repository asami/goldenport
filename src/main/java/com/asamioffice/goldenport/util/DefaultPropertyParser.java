/*
 * The RelaxerOrg class library
 *  Copyright (C) 1997-2004  ASAMI, Tomoharu (asami@relaxer.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.asamioffice.goldenport.util;

/**
 * DefaultPropertyParser
 *
 * @since   May.  3, 2003
 * @version Aug. 29, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class DefaultPropertyParser extends AbstractParameterParser {
/*
    private Class targetClass_;
    private List log_ = new ArrayList();

    public DefaultParameterParser(String[] args) 
      throws MalformedURLException, IOException {
        this(args, null, null);
    }

    public DefaultParameterParser(
        String[] args,
        URL defaultConfig,
        Class clazz
    ) throws MalformedURLException, IOException {
        targetClass_ = clazz;
        List params = new ArrayList();
        String service;
        Map properties;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("---")) {
                // XXX
            } else if (arg.startsWith("--")) {
                Entry entry = _getEntry(arg);
                _setFrameworkProperty(entry.key, entry.value);
            } else if (arg.startsWith("-")) {
                Entry entry = _getEntry(arg);
                String key = entry.key;
                String value = entry.value;
                if (getService() == null && value == null) {
                    _setService(key);
                } else {
                    _setProperty(key, value);
                }
            } else {
                _addArgument(arg);
            }
        }
        setupEnvironmentParameter_();
        if (getFrameworkProperty("config") == null) {
            _setFrameworkProperty("config", defaultConfig.toExternalForm());
        }
        logProperties_();
    }

    public DefaultParameterParser(Map frameworkProperties) 
      throws MalformedURLException, IOException {
        _setFrameworkProperties(frameworkProperties);
        setupEnvironmentParameter_();
    }

    public DefaultParameterParser(Map frameworkProperties, Map properties)
      throws MalformedURLException, IOException {
        if (frameworkProperties != null) {
            _setFrameworkProperties(frameworkProperties);
        }
        setupEnvironmentParameter_();
        _setProperties(properties);
    }

    private void setupEnvironmentParameter_() 
      throws MalformedURLException, IOException {
        setupEnvironmentParameter_(null);
    }

    private void setupEnvironmentParameter_(String defaultUri) 
      throws MalformedURLException, IOException {
        String uri = getFrameworkProperty("config");
        if (uri == null) {
            uri = defaultUri;
        }
        Properties properties = new Properties();
        loadDefaultParameter_(properties);
        if (!loadParameterInOption_(properties)) {
            if (!loadParameterInDirectory_(properties, uri)) {
                if (!loadParameterInArchive_(properties)) {
                    loadParameterInPackage_(properties);
                }
            }
        }
        _setFrameworkProperties(properties);
    }

    private void loadDefaultParameter_(Properties properties) throws IOException {
        if (targetClass_ == null) {
            return;
        }
        URL meta = targetClass_.getResource(
            "/META-INF/RelaxerOrg.properties"
        );
        if (meta != null) {
            loadProperties_(properties, meta);
        }
        URL web = targetClass_.getResource(
            "/WEB-INF/RelaxerOrg.properties"
        );
        if (web != null) {
            loadProperties_(properties, web);
        }
    }

    private boolean loadParameterInOption_(
        Properties properties 
    ) throws MalformedURLException, IOException {
        String uri = getFrameworkProperty("properties");
        if (uri == null) {
            return (false);
        }
        URL url = UURL.getURLFromFileOrURLName(uri);
        return (loadProperties_(properties, url));
    }

    private boolean loadParameterInDirectory_(
        Properties properties, 
        String uri
    ) throws MalformedURLException, IOException {
        if (uri == null) {
            return (false);
        }
        String envUri = UString.getContainerPathname(uri) + "/" + 
                        "RelaxerOrg.properties";
        return (loadProperties_(properties, new URL(envUri)));
    }

    private boolean loadParameterInArchive_(Properties properties) 
      throws IOException {
        if (targetClass_ == null) {
            return (false);
        }
        String packageName = UClass.getPackageName(targetClass_);
        URL config = targetClass_.getResource(
            "/WEB-INF/" + packageName + "." + "RelaxerOrg.properties"
        );
        if (config == null) {
            config = targetClass_.getResource(
                "/META-INF/" + packageName + "." + "RelaxerOrg.properties"
            );
        }
        if (config == null) {
            return (false);
        }
        return (loadProperties_(properties, config));
    }

    private boolean loadParameterInPackage_(
        Properties properties 
    ) throws MalformedURLException, IOException {
        if (targetClass_ == null) {
            return (false);
        }
        String packageName = UClass.getPackageName(targetClass_);
        String dir = getPackageDir_(packageName);
        URL url = targetClass_.getResource(dir + "RelaxerOrg.properties");
        if (url == null) {
            return (false);
        }
        return (loadProperties_(properties, url));
    }

    private String getPackageDir_(String packageName) {
        if (packageName != null) {
            return ("/" + packageName.replace('.', '/') + "/");
        } else {
            return ("/");
        }
    }

    private boolean loadProperties_(Properties properties, URL url)
      throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            properties.load(in);
            log_.add("Load properties from '" + url + "'");
            return (true);
        } catch (IOException e) {
            return (false);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ee) {
                }
            }
        }
    }

    private Entry _getEntry(String arg) {
        Entry pair = new Entry();
        int start = _getStart(arg);
        int index = arg.indexOf(":", start);
        if (index == -1) {
            index = arg.indexOf("=", start);
        }
        if (index == -1) {
            pair.key = arg.substring(start);
            pair.value = null;
        } else {
            pair.key = arg.substring(start, index);
            pair.value = arg.substring(index + 1);
        }
        return (pair);
    }

    private int _getStart(String arg) {
        int i = 0;
        for (;;) {
            if (arg.charAt(i) != '-') {
                return (i);
            }
            i++;
        }
    }

    private void logProperties_() {
        log_.add("Parameters: " + formatParameters_(getParameters()));
        log_.add("Application properties: " +
                 formatProperties_(getProperties()));
        log_.add("Framework properties: " +
                 formatProperties_(getFrameworkProperties()));
    }

    private String formatParameters_(String[] strings) {
        if (strings.length == 0) {
            return ("");
        }
        StringBuffer sb = new StringBuffer();
        sb.append(strings[0]);        
        for (int i = 1;i < strings.length;i++) {
            sb.append(";");
            sb.append(strings[i]);
        }
        return (new String(sb));
    }

    private String formatProperties_(Entry[] entries) {
        if (entries.length == 0) {
            return ("");
        }
        StringBuffer sb = new StringBuffer();
        formatPropertyEntry_(entries[0], sb);
        for (int i = 1;i < entries.length;i++) {
            sb.append(";");
            formatPropertyEntry_(entries[i], sb);
        }
        return (new String(sb));
    }

    private void formatPropertyEntry_(Entry entry, StringBuffer sb) {
        sb.append(entry.key);
        sb.append(":");
        sb.append(entry.value);
    }

    public void logConfig(IRFrameworkLogger logger) {
        Object[] logs = log_.toArray();
        for (int i = 0;i < logs.length;i++) {
            logger.config((String)logs[i]);
        }
    }
*/
}
