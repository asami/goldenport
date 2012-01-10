package com.asamioffice.xml.sax;

import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

import com.asamioffice.xml.visitor.DOMVisitorException;
import com.asamioffice.xml.visitor.UDOMVisitor;

/**
 * SAX event producer from DOM tree
 *
 * @since   Feb. 18, 2001
 * @version Jan. 16, 2006
 * @author  ASAMI, Tomoharu (asami@zeomtech.com)
 */
public class DOMSAXProducer {
    private boolean needDocumentEmulation_ = false;
    private Node root_;
    private String systemID_;
    private String publicID_;
    private DTDHandler dtd_;
    private ContentHandler content_;
    private DeclHandler decl_;
    private LexicalHandler lexical_;
    private EntityResolver resolver_;
    private ErrorHandler error_;

    public DOMSAXProducer(Node node) {
        root_ = node;
    }

    public void setDocumentEmulation(boolean emulate) {
        needDocumentEmulation_ = emulate;
    }

    public void setDTDHandler(DTDHandler dtd) {
        dtd_ = dtd;
    }

    public void setContentHandler(ContentHandler content) {
        content_ = content;
    }

    public void setLexicalHandler(LexicalHandler lexical) {
        lexical_ = lexical;
    }

    public void setDeclHandler(DeclHandler decl) {
        decl_ = decl;
    }

    public void setEntityResolver(EntityResolver resolver) {
        resolver_ = resolver;
    }        

    public void setErrorHandler(ErrorHandler error) {
        error_ = error;
    }

    public void makeEvent() throws SAXException {
        try {
            DOMSAXProducerVisitor visitor = new DOMSAXProducerVisitor();
            visitor.setSystemID(systemID_);
            visitor.setPublicID(publicID_);
            visitor.setDTDHandler(dtd_);
            visitor.setContentHandler(content_);
            visitor.setLexicalHandler(lexical_);
            visitor.setDeclHandler(decl_);
            visitor.setEntityResolver(resolver_);
            visitor.setErrorHandler(error_);
            if (needDocumentEmulation_) {
                visitor.emulateStartDocument();
                UDOMVisitor.traverse(root_, visitor);
                visitor.emulateEndDocument();
            } else {
                UDOMVisitor.traverse(root_, visitor);
            }
        } catch (DOMVisitorException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                throw (new SAXException(e.getMessage()));
            } else if (cause instanceof SAXException) {
                throw ((SAXException)cause);
            } else if (cause instanceof Exception) {
                throw (new SAXException(e.getMessage(), (Exception)cause));
            } else {
                throw (new SAXException(e.getMessage()));
            }
        }
    }

    public void makeEvent(ContentHandler handler) throws SAXException {
        setContentHandler(handler);
        makeEvent();
    }
}
