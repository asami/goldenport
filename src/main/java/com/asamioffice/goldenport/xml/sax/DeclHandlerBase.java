package com.asamioffice.goldenport.xml.sax;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;

/**
 * Base class of DeclHandler
 * 
 * @since Feb. 18, 2001
 * @version Jan. 16, 2006
 * @author ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class DeclHandlerBase implements DeclHandler {
    public void elementDecl(String name, String model) throws SAXException {
    }

    public void attributeDecl(String eName, String aName, String type,
            String valueDefault, String value) throws SAXException {
    }

    public void internalEntityDecl(String name, String value)
            throws SAXException {
    }

    public void externalEntityDecl(String name, String publicId, String systemId)
            throws SAXException {
    }
}
