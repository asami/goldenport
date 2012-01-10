package org.goldenport.monitor.messager;

/**
 * NullMessager
 *
 * @since   Aug. 27, 2005
 * @version Oct. 31, 2008
 * @version Oct. 30, 2011
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class NullMessager extends GMessager {
    public void message(String message) {
        throw new UnsupportedOperationException("NullMessager.message");
    }

    public void message(char[] cbuf, int off, int len) {
        throw new UnsupportedOperationException("NullMessager.message");
    }

    public void error(String errorMessage) {
        throw new UnsupportedOperationException("NullMessager.error");
    }

    public void error(char[] cbuf, int off, int len) {
        throw new UnsupportedOperationException("NullMessager.error");
    }

    public void messageln(String message) {
        throw new UnsupportedOperationException("NullMessager.message");
    }

    public void messageln(char[] cbuf, int off, int len) {
        throw new UnsupportedOperationException("NullMessager.message");
    }

    public void messageln() {
        throw new UnsupportedOperationException("NullMessager.message");
    }

    public void errorln(String errorMessage) {
        throw new UnsupportedOperationException("NullMessager.error");
    }

    public void errorln(char[] cbuf, int off, int len) {
        throw new UnsupportedOperationException("NullMessager.error");
    }

    public void errorln() {
      throw new UnsupportedOperationException("NullMessager.error");
    }

    public void warning(String warningMessage) {
      throw new UnsupportedOperationException("NullMessager.warning");
    }

    public void warningln(String warningMessage) {
      throw new UnsupportedOperationException("NullMessager.warning");
    }

    public void warningln() {
      throw new UnsupportedOperationException("NullMessager.warning");
    }

    public void warning(char[] cbuf, int off, int len) {
      throw new UnsupportedOperationException("NullMessager.warning");
    }
}
