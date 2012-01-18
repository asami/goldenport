package com.asamioffice.goldenport.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * Base class of LexicalHandler
 *
 * @since   Feb. 18, 2001
 * @version Jan. 16, 2006
 * @author  ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class LexicalHandlerBase implements LexicalHandler {
    public void startDTD(String name, String publidId, String systemID)
    throws SAXException {
    }

    public void endDTD() throws SAXException {
    }

    public void startEntity(String name) throws SAXException {
    }

    public void endEntity(String name) throws SAXException {
    }

    public void startCDATA() throws SAXException {
    }

    public void endCDATA() throws SAXException {
    }

    public void comment(char ch[], int start, int length) {
    }
}
