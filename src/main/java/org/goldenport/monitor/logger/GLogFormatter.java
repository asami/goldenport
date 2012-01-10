package org.goldenport.monitor.logger;

import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * derived from IRLogFormatter.java and AbstractLogFormatter.java
 * since Jan. 19, 2003
 *
 * @since   Jan. 19, 2003
 * @version Oct. 28, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class GLogFormatter {
    private ResourceBundle localizer_;

    final public void setup(Properties properties) {
    }

    public abstract void fatal(Appendable out, String message, Object... args) throws IOException;
    public abstract void fatal(Appendable out, Throwable e, String message, Object... args) throws IOException;
    public abstract void error(Appendable out, String message, Object... args) throws IOException;
    public abstract void error(Appendable out, Throwable e, String message, Object... args) throws IOException;
    public abstract void warning(Appendable out, String message, Object... args) throws IOException;
    public abstract void warning(Appendable out, Throwable e, String message, Object... args) throws IOException;
    public abstract void info(Appendable out, String message, Object... args) throws IOException;
    public abstract void info(Appendable out, Throwable e, String message, Object... args) throws IOException;
    public abstract void config(Appendable out, String message, Object... args) throws IOException;
    public abstract void config(Appendable out, Throwable e, String message, Object... args) throws IOException;
    public abstract void entering(Appendable out, Object instance, String method, Object... args) throws IOException;
    public abstract void exiting(Appendable out, Object instance, String method) throws IOException;
    public abstract void exiting(Appendable out, Object instance, String method, Object result) throws IOException;
    public abstract void exiting(Appendable out, Object instance, String method, Throwable e) throws IOException;
    public abstract void debug(Appendable out, String message, Object... args) throws IOException;
    public abstract void debug(Appendable out, Throwable e, String message, Object... args) throws IOException;

    protected void _format(Appendable out, String message, Object[] args) throws IOException {
        out.append(getMessage_(message, args));
    }

    protected void _format(Appendable out, Throwable e, String message, Object[] args) throws IOException {
        out.append(getMessage_(message, args));
        _formatExceptionSummary(out, e);
    }
    
    protected void _formatExceptionSummary(Appendable out, Throwable e) throws IOException {
        out.append(" Exception is [" + e.getClass() + "(" + e.getMessage() + ")]");
    }

    protected void _formatExceptionDetail(Appendable out, Throwable e) throws IOException {
        if (out instanceof PrintWriter) {
            PrintWriter output = (PrintWriter)out;
            e.printStackTrace(output);
            output.flush();
        } else if (out instanceof Writer) {
            PrintWriter output = new PrintWriter((Writer)out);
            e.printStackTrace(output);
            output.flush();
        } else {
            StringWriter buffer = new StringWriter();
            PrintWriter output = new PrintWriter(buffer);
            e.printStackTrace(output);
            output.flush();
            out.append(buffer.toString());
        }
    }

    private String getMessage_(String message, Object... args) {
        return (getMessageFormat_(message).format(args));
    }

    private MessageFormat getMessageFormat_(String message) {
        message = getLocalizedFormatPattern_(message);
        return (new MessageFormat(message));
//        Locale locale = Locale.getDefault(); // TODO
//        return (new MessageFormat(message, locale)); 1.4
    }
    
    private String getLocalizedFormatPattern_(String message) {
        if (localizer_ != null) {
            try {
                message = localizer_.getString(message);
            } catch (MissingResourceException e) {
                // TODO warning
            }
        }
        return (message);
    }

    protected void _printClassMethod(Appendable out, Object instance, String method) throws IOException {
        out.append(instance.getClass().getName());
        out.append(".");
        out.append(method);
    }

    protected void _printTime(Appendable out) throws IOException {
        out.append(getCurrentDateTimeString());
        out.append(' ');
    }

    protected void _printNewline(Appendable out) throws IOException {
        out.append('\n');  // TODO
        if (out instanceof Flushable) {
            ((Flushable)out).flush();
        }
    }

    // TODO
    public static String getCurrentDateTimeString() {
        Calendar calendar = Calendar.getInstance();
        StringBuffer buffer = new StringBuffer();
        buffer.append(calendar.get(Calendar.YEAR));
        buffer.append("-");
        buffer.append(calendar.get(Calendar.MONTH) + 1);
        buffer.append("-");
        buffer.append(calendar.get(Calendar.DATE));
        buffer.append("T");
        buffer.append(calendar.get(Calendar.HOUR_OF_DAY));
        buffer.append(":");
        buffer.append(calendar.get(Calendar.MINUTE));
        buffer.append(":");
        buffer.append(calendar.get(Calendar.SECOND));
        TimeZone zone = calendar.getTimeZone();
        buffer.append(zone.getDisplayName(false, TimeZone.SHORT));
        return (new String(buffer));
    }
}
