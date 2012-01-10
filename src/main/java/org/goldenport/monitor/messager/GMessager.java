package org.goldenport.monitor.messager;

/**
 * derived from IRFrameworkMessager.java and AbstractRMessager.java
 * since Aug. 27, 2005
 *
 * @since   Aug. 27, 2005
 * @version Oct. 31, 2008
 * @version Oct. 30, 2011
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public abstract class GMessager {
    public abstract void message(String message);
    public abstract void messageln(String message);
    public abstract void messageln();
    public abstract void message(char[] cbuf, int off, int len);
    public abstract void error(String errorMessage);
    public abstract void errorln(String errorMessage);
    public abstract void errorln();
    public abstract void error(char[] cbuf, int off, int len);
    public abstract void warning(String warningMessage);
    public abstract void warningln(String warningMessage);
    public abstract void warningln();
    public abstract void warning(char[] cbuf, int off, int len);

    /*
    public abstract void fatal();
    public abstract void error();
    public abstract void warning();
    public abstract void info();
    public abstract void config();
    public abstract void debug();
    */
}
