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

import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;

/**
 * Sax1DomMaker
 *
 * @since   Dec. 16, 2002
 * @version Nov. 25, 2006
 * @author  ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class Sax1DomMaker extends HandlerBase {
    private Document doc_;
    private Stack stack_ = new Stack();
    private Node current_ = null;
    private StringBuffer buffer_ = null;

    public Sax1DomMaker() {
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

    public void startElement(String name, AttributeList atts) {
        Element element = doc_.createElement(name);
        int nAttrs = atts.getLength();
        for (int i = 0; i < nAttrs; i++) {
            String attrName = atts.getName(i);
            String attrValue = atts.getValue(i);
            element.setAttribute(attrName, attrValue);
        }
        current_.appendChild(element);
        stack_.push(current_);
        current_ = element;
    }

    public void endElement(String name) {
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
