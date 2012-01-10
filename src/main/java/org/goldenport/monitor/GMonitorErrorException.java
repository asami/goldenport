package org.goldenport.monitor;

/**
 * RMonitorErrorException
 *
 * @since   Feb.  4, 2006
 * @version Oct. 28, 2008
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class GMonitorErrorException extends RuntimeException {
    private static final long serialVersionUID = -2643097249179233629L;

    public GMonitorErrorException() {
        super();
    }
    
    public GMonitorErrorException(String message) {
        super(message);
    }

    public GMonitorErrorException(Throwable e) {
        super(e);
    }

    public GMonitorErrorException(String message, Throwable e) {
        super(message, e);
    }
}
