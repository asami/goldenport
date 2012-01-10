package org.goldenport.monitor.logger;

import java.util.Properties;

/**
 * derived from IRLogger.java and AbstractLogger.java since Aug. 11, 2002
 *
 * @since   Apr. 11, 2002
 * @version Oct. 30, 2011
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class GLogger {
    int LOG_OFF = 0;
    int LOG_FATAL = 100;
    int LOG_ERROR = 200;
    int LOG_WARNING = 300;
    int LOG_INFO = 400;
    int LOG_CONFIG = 500;
    int LOG_SERVICE = 600;
    int LOG_DEBUG = 700;
    int LOG_TRACE = 800;

    protected int level_ = LOG_INFO;
    protected GLogFormatter formatter_;

    protected GLogger() {
        formatter_ = new PlainLogFormatter();
    }

    final public void setup(Properties properties) {
    }

    final public void setLevel(int level) {
        level_ = level;
    }
    
    final public int getLevel() {
        return (level_);
    }

    final public void setFormatter(GLogFormatter formatter) {
        formatter_ = formatter;
    }

    protected void _loggingError(Exception e) {
        // XXX
    }

    public void openLogger() {
    }

    public void closeLogger() {
    }

    public abstract void fatal(String message, Object... args);
    public abstract void fatal(Throwable e, String message, Object... args);
    public abstract void fatal(Throwable e);
    public abstract void error(String message, Object... args);
    public abstract void error(Throwable e, String message, Object... args);
    public abstract void error(Throwable e);
    public abstract void warning(String message, Object... args);
    public abstract void warning(Throwable e, String message, Object... args);
    public abstract void info(String message, Object... args);
    public abstract void info(Throwable e, String message, Object... args);
    public abstract void config(String message, Object... args);
    public abstract void config(Throwable e, String message, Object... args);
    public abstract void entering(Object instance, String method, Object... args);
    public abstract void exiting(Object instance, String method);
    public abstract void exiting(Object instance, String method, Object result);
    public abstract void exiting(Object instance, String method, Throwable e);
    public abstract void debug(String message, Object... args);
    public abstract void debug(Throwable e, String message, Object... args);
    public abstract void trace(String message, Object... args);

    // scala
    public abstract void debug_scala(String message);
    public abstract void debug_scala(String message, Object[] args);
}
