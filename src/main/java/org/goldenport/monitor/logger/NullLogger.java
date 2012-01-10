package org.goldenport.monitor.logger;

/**
 * NullLogger
 *
 * @since   Dec. 13, 2003
 * @version Oct. 30, 2011
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class NullLogger extends GLogger {
    public NullLogger() {
    }

    public void fatal(String message, Object... args) {
    }

    public void fatal(Throwable e, String message, Object... args) {
    }

    public void fatal(Throwable e) {
    }

    public void error(String message, Object... args) {
    }

    public void error(Throwable e, String message, Object... args) {
    }

    public void error(Throwable e) {
    }

    public void warning(String message, Object... args) {
    }

    public void warning(Throwable e, String message, Object... args) {
    }

    public void info(String message, Object... args) {
    }

    public void info(Throwable e, String message, Object... args) {
    }

    public void config(String message, Object... args) {
    }

    public void config(Throwable e, String message, Object... args) {
    }

    public void entering(Object instance, String method, Object... args) {
    }

    public void exiting(Object instance, String method) {
    }

    public void exiting(Object instance, String method, Object result) {
    }

    public void exiting(Object instance, String method, Throwable e) {
    }

    public void debug(String message, Object... args) {
    }

    public void debug(Throwable e, String message, Object... args) {
    }

    public void trace(String message, Object... args) {
    }

    public void debug_scala(String message) {
    }

    public void debug_scala(String message, Object[] args) {
    }
}
