/*
 * RelaxerOrg class library
 *  Copyright (C) 2000-2002  ASAMI, Tomoharu (asami@relaxer.org)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Sax2DomMaker
 *
 * @since   Dec. 16, 2002
 * @version Nov. 25, 2006
 * @author  ASAMI, Tomoharu (asami@relaxer.org)
 */
public class Sax2DomMaker extends DefaultHandler {
    private static final String NS_URI = "http://www.w3.org/2000/xmlns/";
    private Document doc_ = null;
    private Stack stack_ = new Stack();
    private Node current_ = null;
    private StringBuffer buffer_ = null;
    private List prefixes_ = null;

    public Sax2DomMaker() {
    }

    public void startDocument() {
        try {
            DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc_ = builder.newDocument();
            current_ = doc_;
        } catch (ParserConfigurationException e) {
            throw (new InternalError());
        }
    }

    public void endDocument() {
    }

    public void startPrefixMapping(String prefix, String uri) {
        if (prefixes_ == null) {
            prefixes_ = new ArrayList();
        }
        prefixes_.add(new String[] { prefix, uri });
    }

    public void endPrefixMapping(String prefix) {
    }

    public void startElement(
        String namespaceUri,
        String localName,
        String qName,
        Attributes atts) {
        Element element = doc_.createElementNS(namespaceUri, qName);
        if (prefixes_ != null) {
            int size = prefixes_.size();
            for (int i = 0; i < size; i++) {
                String[] pair = (String[])prefixes_.get(i);
                String prefix = pair[0];
                String uri = pair[1];
                if (prefix == null || "".equals(prefix)) {
                    element.setAttributeNS(NS_URI, "xmlns", uri);
                } else {
                    element.setAttributeNS(NS_URI, "xmlns:" + prefix, uri);
                }
            }
            prefixes_ = null;
        }
        int nAttrs = atts.getLength();
        for (int i = 0; i < nAttrs; i++) {
            String attrUri = atts.getURI(i);
            String attrName = atts.getQName(i);
            String attrValue = atts.getValue(i);
            element.setAttributeNS(attrUri, attrName, attrValue);
        }
        current_.appendChild(element);
        stack_.push(current_);
        current_ = element;
    }

    public void endElement(
        String namespaceUri,
        String localName,
        String qName) {
        current_ = (Node)stack_.pop();
    }

    public void characters(char[] ch, int start, int length) {
        current_.appendChild(
            doc_.createTextNode(new String(ch, start, length)));
    }

    public Document getDocument() {
        return (doc_);
    }
}
