package org.goldenport.monitor.logger;

import java.io.IOException;

/**
 * PlainLogFormatter
 *
 * @since   Jan. 19, 2003
 * @version Oct. 28, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class PlainLogFormatter extends GLogFormatter {
    public void fatal(Appendable out, String message, Object... args) throws IOException {
        _printTime(out);
        out.append("FATAL: ");
        _format(out, message, args);
        _printNewline(out);
    }

    public void fatal(Appendable out, Throwable e, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.fatal");
    }

    public void error(Appendable out, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.error");
    }

    public void error(Appendable out, Throwable e, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.error");
    }

    public void warning(Appendable out, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.warning");
    }

    public void warning(Appendable out, Throwable e, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.warning");
    }

    public void info(Appendable out, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.info");
    }

    public void info(Appendable out, Throwable e, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.info");
    }

    public void config(Appendable out, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.config");
    }

    public void config(Appendable out, Throwable e, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.config");
    }

    public void entering(Appendable out, Object instance, String method, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.entering");
    }

    public void exiting(Appendable out, Object instance, String method) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.exiting");
    }

    public void exiting(Appendable out, Object instance, String method, Object result) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.exiting");
    }

    public void exiting(Appendable out, Object instance, String method, Throwable e) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.exiting");
    }

    public void debug(Appendable out, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.debug");
    }

    public void debug(Appendable out, Throwable e, String message, Object... args) throws IOException {
        throw new UnsupportedOperationException("PlainLogFormatter.debug");
    }

    /*
    public void log(String message, Writer writer) throws IOException {
        _printTime(writer);
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    // fatal
    public void fatal(String message, Writer writer) throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void fatal(String message, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void fatal(String message, Object arg, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, arg, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void fatal(String message, Object arg, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, arg, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void fatal(String message, Object arg1, Object arg2, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, arg1, arg2, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void fatal(
        String message,
        Object arg1,
        Object arg2,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, arg1, arg2, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void fatal(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, arg1, arg2, arg3, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void fatal(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, arg1, arg2, arg3, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void fatal(String message, String[] args, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, args, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void fatal(
        String message,
        String[] args,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("FATAL: ");
        _format(message, args, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    // error
    public void error(String message, Writer writer) throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void error(String message, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void error(String message, Object arg, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, arg, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void error(String message, Object arg, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, arg, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void error(String message, Object arg1, Object arg2, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, arg1, arg2, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void error(
        String message,
        Object arg1,
        Object arg2,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, arg1, arg2, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void error(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, arg1, arg2, arg3, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void error(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, arg1, arg2, arg3, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void error(String message, String[] args, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, args, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void error(
        String message,
        String[] args,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ERROR: ");
        _format(message, args, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    // warning
    public void warning(String message, Writer writer) throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void warning(String message, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void warning(String message, Object arg, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, arg, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void warning(String message, Object arg, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, arg, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void warning(String message, Object arg1, Object arg2, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, arg1, arg2, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void warning(
        String message,
        Object arg1,
        Object arg2,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, arg1, arg2, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void warning(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, arg1, arg2, arg3, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void warning(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, arg1, arg2, arg3, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void warning(String message, String[] args, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, args, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void warning(
        String message,
        String[] args,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("WARNING: ");
        _format(message, args, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    // info
    public void info(String message, Writer writer) throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void info(String message, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void info(String message, Object arg, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, arg, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void info(String message, Object arg, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, arg, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void info(String message, Object arg1, Object arg2, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, arg1, arg2, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void info(
        String message,
        Object arg1,
        Object arg2,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, arg1, arg2, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void info(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, arg1, arg2, arg3, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void info(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, arg1, arg2, arg3, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void info(String message, String[] args, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, args, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void info(
        String message,
        String[] args,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("INFO: ");
        _format(message, args, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    // config
    public void config(String message, Writer writer) throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void config(String message, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void config(String message, Object arg, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, arg, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void config(String message, Object arg, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, arg, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void config(String message, Object arg1, Object arg2, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, arg1, arg2, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void config(
        String message,
        Object arg1,
        Object arg2,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, arg1, arg2, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void config(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, arg1, arg2, arg3, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void config(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, arg1, arg2, arg3, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void config(String message, String[] args, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, args, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void config(
        String message,
        String[] args,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("CONFIG: ");
        _format(message, args, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    // entering
    public void entering(Object object, String method, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("ENTER: ");
        _printClassMethod(object, method, writer);
        writer.write("()");
        _printNewline(writer);
        writer.flush();
    }

    public void entering(
        Object object,
        String method,
        Object arg,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ENTER: ");
        _printClassMethod(object, method, writer);
        writer.write("(");
        writer.write(arg.toString());
        writer.write(")");
        _printNewline(writer);
        writer.flush();
    }

    public void entering(
        Object object,
        String method,
        Object arg1,
        Object arg2,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ENTER: ");
        _printClassMethod(object, method, writer);
        writer.write("(");
        writer.write(arg1.toString());
        writer.write(", ");
        writer.write(arg2.toString());
        writer.write(")");
        _printNewline(writer);
        writer.flush();
    }

    public void entering(
        Object object,
        String method,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ENTER: ");
        _printClassMethod(object, method, writer);
        writer.write("(");
        writer.write(arg1.toString());
        writer.write(", ");
        writer.write(arg2.toString());
        writer.write(", ");
        writer.write(arg3.toString());
        writer.write(")");
        _printNewline(writer);
        writer.flush();
    }

    public void entering(
        Object object,
        String method,
        Object[] args,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("ENTER: ");
        _printClassMethod(object, method, writer);
        writer.write("(");
        if (args.length > 0) {
            writer.write(args[0].toString());
            for (int i = 1; i < args.length; i++) {
                writer.write(", ");
                writer.write(args[i].toString());
            }
        }
        writer.write(")");
        _printNewline(writer);
        writer.flush();
    }

    // exiting
    public void exiting(Object object, String method, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("EXIT: ");
        _printClassMethod(object, method, writer);
        writer.write("()");
        _printNewline(writer);
        writer.flush();
    }

    public void exiting(
        Object object,
        String method,
        Object result,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("EXIT: ");
        _printClassMethod(object, method, writer);
        writer.write("(");
        if (result != null) {
            writer.write(result.toString());
        }
        writer.write(")");
        _printNewline(writer);
        writer.flush();
    }

    public void exiting(
        Object object,
        String method,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("EXIT: ");
        _printClassMethod(object, method, writer);
        _formatExceptionSummary(e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    // debug
    public void debug(String message, Writer writer) throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void debug(String message, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void debug(String message, Object arg, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, arg, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void debug(String message, Object arg, Throwable e, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, arg, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void debug(String message, Object arg1, Object arg2, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, arg1, arg2, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void debug(
        String message,
        Object arg1,
        Object arg2,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, arg1, arg2, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void debug(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, arg1, arg2, arg3, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void debug(
        String message,
        Object arg1,
        Object arg2,
        Object arg3,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, arg1, arg2, arg3, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }

    public void debug(String message, String[] args, Writer writer)
        throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, args, writer);
        _printNewline(writer);
        writer.flush();
    }

    public void debug(
        String message,
        String[] args,
        Throwable e,
        Writer writer
    ) throws IOException {
        _printTime(writer);
        writer.write("DEBUG: ");
        _format(message, args, e, writer);
        _printNewline(writer);
        _formatExceptionDetail(e, writer);
        writer.flush();
    }
*/
}
