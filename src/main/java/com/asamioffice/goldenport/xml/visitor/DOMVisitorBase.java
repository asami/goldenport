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

package com.asamioffice.goldenport.xml.visitor;

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
 * DOMVisitorBase
 *
 * @since   Feb.  4, 2000
 * @version Jan. 16, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public abstract class DOMVisitorBase implements IDOMVisitor {
    public boolean enter(Element element) {
        return (_enter(element));
    }

    public boolean enter(Attr attr) {
        return (_enter(attr));
    }

    public boolean enter(Text text) {
        return (_enter(text));
    }

    public boolean enter(CDATASection cdata) {
        return (_enter(cdata));
    }

    public boolean enter(EntityReference entityRef) {
        return (_enter(entityRef));
    }

    public boolean enter(Entity entity) {
        return (_enter(entity));
    }

    public boolean enter(ProcessingInstruction pi) {
        return (_enter(pi));
    }

    public boolean enter(Comment comment) {
        return (_enter(comment));
    }

    public boolean enter(Document doc) {
        return (_enter(doc));
    }

    public boolean enter(DocumentType doctype) {
        return (_enter(doctype));
    }

    public boolean enter(DocumentFragment docfrag) {
        return (_enter(docfrag));
    }

    public boolean enter(Notation notation) {
        return (_enter(notation));
    }

    public boolean enter(Node node) {
        throw (new InternalError());
    }

    protected boolean _enter(Node node) {
        return (true);
    }

    public void stay(Element node, Node prev, Node next) throws DOMVisitorException {
    }

    public void stay(Node node, Node prev, Node next) throws DOMVisitorException {
    }

    public void leave(Element element) {
        _leave(element);
    }

    public void leave(Attr attr) {
        _leave(attr);
    }

    public void leave(Text text) {
        _leave(text);
    }

    public void leave(CDATASection cdata) {
        _leave(cdata);
    }

    public void leave(EntityReference entityRef) {
        _leave(entityRef);
    }

    public void leave(Entity entity) {
        _leave(entity);
    }

    public void leave(ProcessingInstruction pi) {
        _leave(pi);
    }

    public void leave(Comment comment) {
        _leave(comment);
    }

    public void leave(Document doc) {
        _leave(doc);
    }

    public void leave(DocumentType doctype) {
        _leave(doctype);
    }

    public void leave(DocumentFragment docfrag) {
        _leave(docfrag);
    }

    public void leave(Notation notation) {
        _leave(notation);
    }

    public void leave(Node node) {
        throw (new InternalError());
    }

    protected void _leave(Node node) {
    }
}
