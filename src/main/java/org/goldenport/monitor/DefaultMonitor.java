package org.goldenport.monitor;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.goldenport.monitor.logger.GLogger;
import org.goldenport.monitor.logger.StderrLogger;
import org.goldenport.monitor.messager.ConsoleMessager;
import org.goldenport.monitor.messager.GMessager;
import org.goldenport.monitor.notifier.GNotifier;
import org.goldenport.monitor.notifier.NullNotifier;
import org.xml.sax.XMLReader;

/**
 * derived from DefaultMonitor.java since Feb. 3, 2006
 *
 * DefaultMonitor
 *
 * @since   Oct. 28, 2008
 * @version Jan. 26, 2009
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class DefaultMonitor extends GMonitor {
    private final GLogger logger_;
    private final GMessager messager_;
    private final GNotifier notifier_;

    public DefaultMonitor() {
        logger_ = new StderrLogger();
        messager_ = new ConsoleMessager();
        notifier_ = new NullNotifier();
    }

    public GLogger getLogger() {
        return logger_;
    }

    public GMessager getMessager() {
        return messager_;
    }

    public GNotifier getNotifier() {
        return notifier_;
    }

    public String getTextEncoding() {
        return "UTF-8";
    }

    public String getNewLine() {
        return "\n";
    }

    public boolean isPlatformWindows() {
        return System.getProperty("os.name").indexOf("Windows") != -1;
    }

    public ClassLoader getClassLoader() {
        return getClass().getClassLoader();
    }

    public DocumentBuilder getDocumentBuilder() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setExpandEntityReferences(true);
        try {
            return factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new GMonitorErrorException(e);
        }
    }

    public SAXParser getSAXParser() {
        throw new UnsupportedOperationException("RelaxerMonitor.getSAXParser");
    }

    public XMLReader getXMLReader() {
        throw new UnsupportedOperationException("RelaxerMonitor.getXMLReader");
    }

    public boolean isProtectedFile(File file) {
        String pathname = file.getAbsolutePath();
        char[] chars = pathname.toCharArray();
        int nSlashes = 0;
        for (int i = 0;i < chars.length;i++) {
            char c = chars[i];
            if (c == '/' || c =='\\') {
                nSlashes++;
            }
            if (nSlashes >= 3) { // XXX
                return false; 
            }
        }
        return true;
    }

    private static GMonitor __monitor = null;

    public static GMonitor getMonitor() {
        if (__monitor == null) {
            __monitor = new DefaultMonitor();
        }
        return __monitor;
    }
}
