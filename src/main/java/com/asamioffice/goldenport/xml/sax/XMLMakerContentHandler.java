/*
 * The RelaxerOrg class library
 *  Copyright (C) 1997-2004  ASAMI, Tomoharu (asami@relaxer.org)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package com.asamioffice.goldenport.xml.sax;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;

import com.asamioffice.goldenport.xml.UXMLMaker;

/**
 * XMLMakerContentHandler
 *
 * @since   Jan. 16, 2001
 * @version Nov. 25, 2006
 * @author  ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class XMLMakerContentHandler implements ContentHandler, DTDHandler {
    private Writer buffer_;
    private StringWriter stringWriter_;
    //
    private StringBuffer xmlnsDecl_;
    private StringBuffer internalSubset_;
    //
    private String encoding_ = "UTF-8";
    private boolean expandEntityReference_ = false;
    private boolean emptyElementTag_ = false;

    public XMLMakerContentHandler() {
	buffer_ = stringWriter_ = new StringWriter();
    }

    public XMLMakerContentHandler(Writer writer) {
	buffer_ = writer;
    }

    public String getText() {
	return (stringWriter_.toString());
    }

    // ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() {
	xmlnsDecl_ = null;
	internalSubset_ = null;
    }

    public void endDocument() {
	try {
	    buffer_.flush();
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }

    public void startPrefixMapping(String prefix, String uri) {
	if (xmlnsDecl_ == null) {
	    xmlnsDecl_ = new StringBuffer();
	}
	if ("".equals(prefix)) {
	    xmlnsDecl_.append(" xmlns=\"");
	    xmlnsDecl_.append(uri);
	    xmlnsDecl_.append("\"");
	} else {
	    xmlnsDecl_.append(" xmlns:");
	    xmlnsDecl_.append(prefix);
	    xmlnsDecl_.append("=\"");
	    xmlnsDecl_.append(uri);
	    xmlnsDecl_.append("\"");
	}
    }

    public void endPrefixMapping(String prefix) {
    }

    public void startElement(
	String namespaceURI,
	String localName,
	String qName,
	Attributes atts
    ) {
	try {
	    buffer_.write("<");
	    buffer_.write(qName);
	    if (xmlnsDecl_ != null) {
		buffer_.write(new String(xmlnsDecl_));
		xmlnsDecl_ = null;
	    }
	    int size = atts.getLength();
	    for (int i = 0;i < size;i++) {
		buffer_.write(" ");
		buffer_.write(atts.getQName(i));
		buffer_.write("=\"");
		buffer_.write(atts.getValue(i));
		buffer_.write("\"");
	    }
	    buffer_.write(">");
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }

    public void endElement(
	String namespaceURI,
	String localName,
	String qName
    ) {
	try {
	    buffer_.write("</");
	    buffer_.write(qName);
	    buffer_.write(">");
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }

    public void characters(char ch[], int start, int length) {
	try {
	    buffer_.write(ch, start, length);
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }

    public void ignorableWhitespace(char ch[], int start, int length) {
    }

    public void processingInstruction(String target, String data) {
	try {
	    UXMLMaker.makeProcessingInstruction(target, data, buffer_);
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }

    public void skippedEntity(String name) {
    }

    // DTDHandler
    public void notationDecl(String name, String publicId, String systemId) {
	try {
	    UXMLMaker.makeNotation(name, publicId, systemId, buffer_);
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }

    public void unparsedEntityDecl(
	String name,
	String publicId,
	String systemId,
	String notationName
    ) {
	try {
	    UXMLMaker.makeUnparsedEntity(
		name,
		publicId,
		systemId,
		notationName,
		buffer_
	    );
	} catch (IOException e) {
	    throw (new UnsupportedOperationException());
	}
    }
}
