/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2003  ASAMI, Tomoharu (asami@AsamiOffice.com)
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

package com.asamioffice.xml.visitor;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/**
 * IDOMVisitor
 *
 * @since   Feb.  4, 2000
 * @version Sep. 23, 2005
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public interface IDOMVisitor {
    boolean enter(Element element) throws DOMVisitorException;
    boolean enter(Attr attr) throws DOMVisitorException;
    boolean enter(Text text) throws DOMVisitorException;
    boolean enter(CDATASection cdata) throws DOMVisitorException;
    boolean enter(EntityReference entityRef) throws DOMVisitorException;
    boolean enter(Entity entity) throws DOMVisitorException;
    boolean enter(ProcessingInstruction pi) throws DOMVisitorException;
    boolean enter(Comment comment) throws DOMVisitorException;
    boolean enter(Document doc) throws DOMVisitorException;
    boolean enter(DocumentType doctype) throws DOMVisitorException;
    boolean enter(DocumentFragment docfrag) throws DOMVisitorException;
    boolean enter(Notation notation) throws DOMVisitorException;
    boolean enter(Node node) throws DOMVisitorException;
    void stay(Element node, Node prev, Node next) throws DOMVisitorException;
    void stay(Node node, Node prev, Node next) throws DOMVisitorException;
    void leave(Element element) throws DOMVisitorException;
    void leave(Attr attr) throws DOMVisitorException;
    void leave(Text text) throws DOMVisitorException;
    void leave(CDATASection cdata) throws DOMVisitorException;
    void leave(EntityReference entityRef) throws DOMVisitorException;
    void leave(Entity entity) throws DOMVisitorException;
    void leave(ProcessingInstruction pi) throws DOMVisitorException;
    void leave(Comment comment) throws DOMVisitorException;
    void leave(Document doc) throws DOMVisitorException;
    void leave(DocumentType doctype) throws DOMVisitorException;
    void leave(DocumentFragment docfrag) throws DOMVisitorException;
    void leave(Notation notation) throws DOMVisitorException;
    void leave(Node node) throws DOMVisitorException;
}
