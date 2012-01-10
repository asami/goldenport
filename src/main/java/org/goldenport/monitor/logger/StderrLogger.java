package org.goldenport.monitor.logger;

/**
 * StderrLogger
 *
 * @since   Jan. 19, 2003
 * @version Oct. 28, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class StderrLogger extends AppendableGLogger {
    public StderrLogger() {
        super(System.err);
    }
}
