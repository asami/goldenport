package org.goldenport.monitor;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.SAXParser;
import org.xml.sax.XMLReader;
import org.goldenport.monitor.logger.GLogger;
import org.goldenport.monitor.messager.GMessager;
import org.goldenport.monitor.notifier.GNotifier;

/**
 * derived from IRMonitor.java and AbstractRMonitor.java since Feb. 5, 2006
 *
 * GMonitor
 *
 * @since   Oct. 28, 2008
 * @version Jan. 26, 2009
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class GMonitor {
    private File tmpDirectory_ = null;

    public abstract GLogger getLogger();
    public abstract GMessager getMessager();
    public abstract GNotifier getNotifier();
    public abstract String getTextEncoding();
    public abstract String getNewLine();
    public abstract boolean isPlatformWindows();
    public abstract ClassLoader getClassLoader();
    public abstract DocumentBuilder getDocumentBuilder();
    public abstract SAXParser getSAXParser();
    public abstract XMLReader getXMLReader();
    public abstract boolean isProtectedFile(File file);

    public final File getTmpDirectory() {
        if (tmpDirectory_ != null) {
            return tmpDirectory_;
        }
        tmpDirectory_ = _getTmpDirectory();
        if (tmpDirectory_ != null) {
            return tmpDirectory_;
        }
        tmpDirectory_ = new File(System.getProperty("java.io.tmpdir"));
        return tmpDirectory_;
    }

    protected File _getTmpDirectory() {
        return null;
    }
}
