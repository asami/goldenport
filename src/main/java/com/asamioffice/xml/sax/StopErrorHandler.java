package com.asamioffice.xml.sax;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * StopErrorHandler
 *
 * @since   Sep. 15, 2005
 * @version Jan. 16, 2006
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class StopErrorHandler implements ErrorHandler {
    public void warning(SAXParseException exception) throws SAXException {
        // do nothing
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
        // do nothing
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
        // do nothing
    }
}
