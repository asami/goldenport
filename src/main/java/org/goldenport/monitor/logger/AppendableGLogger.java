package org.goldenport.monitor.logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * derived from AbstractAppendableLogger.java(AbstractPrintStreamLogger.java)
 * since Apr. 12, 2002
 *
 * @since   Oct. 28, 2008
 * @version Apr.  3, 2009
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class AppendableGLogger extends GLogger {
    protected Appendable out_;

    protected AppendableGLogger() {
    }

    protected AppendableGLogger(PrintWriter out) {
        _setup(out);
    }

    protected AppendableGLogger(Writer out) {
        _setup(out);
    }

    protected AppendableGLogger(OutputStream out) {
        _setup(out);
    }

    protected void _setup(Writer out) {
        if (out == null) {
            throw (new IllegalArgumentException());
        }
        out_ = out;
    }

    protected void _setup(OutputStream out) {
        out_ = new OutputStreamWriter(out);
    }

    // fatal
    public void fatal(String message, Object... args) {
        if (level_ < LOG_FATAL) {
            return;
        }
        try {
            formatter_.fatal(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void fatal(Throwable e, String message, Object... args) {
        if (level_ < LOG_FATAL) {
            return;
        }
        try {
            formatter_.fatal(out_, e, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void fatal(Throwable e) {
        if (level_ < LOG_FATAL) {
            return;
        }
        try {
            formatter_.fatal(out_, e, e.getMessage());
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    // error
    public void error(String message, Object... args) {
        if (level_ < LOG_ERROR) {
            return;
        }
        try {
            formatter_.error(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void error(Throwable e, String message, Object... args) {
        if (level_ < LOG_ERROR) {
            return;
        }
        try {
            formatter_.error(out_, e, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void error(Throwable e) {
        if (level_ < LOG_ERROR) {
            return;
        }
        try {
            formatter_.error(out_, e, e.getMessage());
        } catch (IOException le) {
            _loggingError(le);
        }
    }        

    // warning
    public void warning(String message, Object... args) {
        if (level_ < LOG_WARNING) {
            return;
        }
        try {
            formatter_.warning(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void warning(Throwable e, String message, Object... args) {
        if (level_ < LOG_WARNING) {
            return;
        }
        try {
            formatter_.warning(out_, e, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    // info
    public void info(String message, Object... args) {
        if (level_ < LOG_INFO) {
            return;
        }
        try {
            formatter_.info(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void info(Throwable e, String message, Object... args) {
        if (level_ < LOG_INFO) {
            return;
        }
        try {
            formatter_.info(out_, e, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }
    
    // config
    public void config(String message, Object... args) {
        if (level_ < LOG_CONFIG) {
            return;
        }
        try {
            formatter_.config(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void config(Throwable e, String message, Object... args) {
        if (level_ < LOG_CONFIG) {
            return;
        }
        try {
            formatter_.config(out_, e, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    // entering
    public void entering(Object instance, String method, Object... args) {
        if (level_ < LOG_SERVICE) {
            return;
        }
        try {
            formatter_.entering(out_, instance, method, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void exiting(Object instance, String method) {
        if (level_ < LOG_SERVICE) {
            return;
        }
        try {
            formatter_.exiting(out_, instance, method);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void exiting(Object instance, String method, Object result) {
        if (level_ < LOG_SERVICE) {
            return;
        }
        try {
            formatter_.exiting(out_, instance, method, result);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void exiting(Object instance, String method, Throwable e) {
        if (level_ < LOG_SERVICE) {
            return;
        }
        try {
            formatter_.exiting(out_, instance, method, e);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    // debug
    public void debug(String message, Object... args) {
        if (level_ < LOG_DEBUG) {
            return;
        }
        try {
            formatter_.debug(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void debug(Throwable e, String message, Object... args) {
        if (level_ < LOG_DEBUG) {
            return;
        }
        try {
            formatter_.debug(out_, e, message, args, e);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    // trace
    public void trace(String message, Object... args) {
        if (level_ < LOG_TRACE) {
            return;
        }
        try {
            formatter_.debug(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void trace(Throwable e, String message, Object... args) {
        if (level_ < LOG_TRACE) {
            return;
        }
        try {
            formatter_.debug(out_, e, message, args, e);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void debug_scala(String message) {
        if (level_ < LOG_DEBUG) {
            return;
        }
        try {
            formatter_.debug(out_, message);
        } catch (IOException le) {
            _loggingError(le);
        }
    }

    public void debug_scala(String message, Object[] args) {
        if (level_ < LOG_DEBUG) {
            return;
        }
        try {
            formatter_.debug(out_, message, args);
        } catch (IOException le) {
            _loggingError(le);
        }
    }
}
