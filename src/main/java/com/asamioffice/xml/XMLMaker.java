/*
 * The JabaJaba class library
 *  Copyright (C) 1997-2006  ASAMI, Tomoharu (asami@AsamiOffice.com)
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

package com.asamioffice.xml;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import com.asamioffice.text.UString;
import com.asamioffice.xml.visitor.DOMVisitorBase;
import com.asamioffice.xml.visitor.DOMVisitorException;
import com.asamioffice.xml.visitor.UDOMVisitor;

/**
 * XMLMaker
 *
 * @since   Oct. 27, 2000
 * @version Mar. 25, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class XMLMaker extends DOMVisitorBase {
    private Writer buffer_;
    private StringWriter stringWriter_;
    private String encoding_ = "UTF-8";
    private boolean simpleXmlDeclaration_ = true;
    private String headComment_ = null;
    private String publicId_ = null;
    private String systemId_ = null;
    private boolean dom2_ = false;
    private boolean expandEntityReference_ = false;
    private boolean emptyElementTag_ = true;
    private boolean visual_ = false;
    private boolean autoMixed_ = true;
    private boolean isEscapeXmlChars_ = true;
    private int indent_ = 2;
    private int indentCount_ = 0;
    private int indentMixedMode_ = 0;
    private String lineSeparator_ = "\n";
//    private String lineSeparator_ = System.getProperty("line.separator");
    private boolean escapeCr_ = true;
    private boolean javaString_ = false;
    private Map<String, List<String>> attributeOrder_ = new HashMap<String, List<String>>();
    private Set<String> mixedElements_ = new HashSet<String>();

    public static void traverse(Node node, XMLMaker maker) {
        UDOMVisitor.traverse(node, maker);
    }

    public XMLMaker() {
        buffer_ = stringWriter_ = new StringWriter();
    }

    public XMLMaker(Writer writer) {
        buffer_ = writer;
        stringWriter_ = null;
    }

    public void setEncoding(String encoding) {
        encoding_ = encoding;
    }
    
    public void setSimpleXmlDeclaration(boolean value) {
        simpleXmlDeclaration_ = value;
    }

    public void setPublicId(String id) {
        publicId_ = id;
    }

    public void setSystemId(String id) {
        systemId_ = id;
    }

    public void setDOM2(boolean dom2) {
        dom2_ = dom2;
    }

    public void setExpandEntityReference(boolean expand) {
        expandEntityReference_ = expand;
    }

    public void setEmptyElementTag(boolean empty) {
        emptyElementTag_ = empty;
    }

    public void setVisual(boolean visual) {
        visual_ = visual;
    }
    
    public void setAutoMixed(boolean autoMixed) {
        autoMixed_ = autoMixed;
    }

    public void setLineSeparator(String separator) {
        lineSeparator_ = separator;
    }
    
    public void setIndent(int indent) {
        indent_ = indent;
    }
    
    public void setJavaString(boolean javaString) {
        javaString_ = javaString;
        if (javaString) {
            lineSeparator_ = "\\n";
        }
    }

    public void make(Node node) {
        try {
            traverse(node, this);
            buffer_.flush();
        } catch (IOException e) {
            throw new DOMVisitorException(e);
        }
    }

    public String getText() {
        return (stringWriter_.toString());
    }

    public boolean enter(Element element) {
        try {
            if (visual_ && indentMixedMode_ == 0) {
                _writeIndent();
            }
            String tag = element.getTagName();
            buffer_.write("<");
            buffer_.write(tag);
            int attrIndent = indentCount_ + 1 + tag.length() + 1;
            NamedNodeMap attrs = element.getAttributes();
            _makeAttributes(attrs, attrIndent, tag);
            if (element.getFirstChild() == null && emptyElementTag_) {
                buffer_.write("/>");
                if (visual_) {
                    buffer_.write(lineSeparator_);
                }
                return (false);
            } else {
                buffer_.write(">");
                if (isMixedElement_(element)) {
                    indentMixedMode_++;
                }
                if (_isIndent(element)) {
                    buffer_.write(lineSeparator_);
                    _indentUp();
                }
                return (true);
            }
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    private void _makeAttributes(
        NamedNodeMap attrMap,
        int attrIndent,
        String elementName
    ) throws IOException {
        Attr[] attrs = sortAttrs_(elementName, attrMap);
        if (visual_) {
            _makeAttributesVisual(attrs, attrIndent);
        } else {
            _makeAttributesPlain(attrs);
        }
    }

    private Attr[] sortAttrs_(String elementName, NamedNodeMap attrMap) {
        if (visual_) {
            return (sortAttrsVisual_(elementName, attrMap));
        } else {
            return (sortAttrsAlphabet_(attrMap));
        }
    }

    private Attr[] sortAttrsAlphabet_(NamedNodeMap attrMap) {
        int nAttrs = attrMap.getLength();
        Attr[] attrs = new Attr[nAttrs];
        for (int i = 0;i < nAttrs;i++) {
            Attr attr = (Attr)attrMap.item(i);
            attrs[i] = attr;
        }
        return (attrs);
    }

    private Attr[] sortAttrsVisual_(String elementName, NamedNodeMap attrMap) {
        List<String> order = attributeOrder_.get(elementName);
//System.out.println("sortAttrs_" + elementName + "/" + order);        
        if (order == null) {
            return (sortAttrsAlphabet_(attrMap));
        } else {
            List<Attr> list = new ArrayList<Attr>();
            Object[] names = order.toArray();
            int nAttrs = attrMap.getLength();
            for (int i = 0; i < names.length; i++) {
                String name = (String)names[i];
                for (int j = 0;j < nAttrs;j++) {
                    Attr attr = (Attr)attrMap.item(j);
                    if (name.equals(attr.getName())) {
                        list.add(attr);
                        break;
                    }
                }            
            }
            for (int i = 0;i < nAttrs;i++) {
                Attr attr = (Attr)attrMap.item(i);
                if (!order.contains(attr.getName())) {
                    list.add(attr);
                }
            }            
            Attr[] result = new Attr[list.size()];
            return ((Attr[])list.toArray(result));
        }
    }

    private void _makeAttributesVisual(
        Attr[] attrs,
        int attrIndent
    ) throws IOException {
        Attr nsDef = null;
        List<Attr> nsDefs = new ArrayList<Attr>();
        List<Attr> attrDefs = new ArrayList<Attr>();
        for (int i = 0;i < attrs.length;i++) {
            Attr attr = attrs[i];
            if (attr.getSpecified()) {
                if ("xmlns".equals(attr.getName())) {
                    nsDef = attr;
                } else if (attr.getName().startsWith("xmlns:")) {
                    nsDefs.add(attr);
                } else {
                    attrDefs.add(attr);
                }
            }
        }
        if (nsDef != null) {
            nsDefs.add(0, nsDef);
        }
        int attrDefsSize = attrDefs.size();
        int nsDefsSize = nsDefs.size();
        if (nsDefsSize > 0) {
//            Collections.sort(nsDefs);
            Attr attr = (Attr)nsDefs.get(0);
            buffer_.write(' ');
            enter(attr);
            leave(attr);
            for (int i = 1;i < nsDefsSize;i++) {
                buffer_.write(lineSeparator_);
                _writeIndent(attrIndent);
                attr = (Attr)nsDefs.get(i);
                enter(attr);
                leave(attr);
            }
            if (attrDefsSize > 0) {
                buffer_.write(lineSeparator_);
                _writeIndent(attrIndent);
            }
        } else {
            if (attrDefsSize > 0) {
                buffer_.write(' ');
            }
        }
        { // TODO
            String status = "init";
            for (int i = 0;i < attrDefsSize;i++) {
                Attr attr = (Attr)attrDefs.get(i);
                if ("init".equals(status)) {
                    enter(attr);
                    leave(attr);
                    if (_isLongString(attr)) {
//                        buffer_.write(lineSeparator_);
//                        _writeIndent(attrIndent);
                        status = "cont-long";
                    } else {
                        status = "cont";
                    }
                } else if ("cont".equals(status)) {
                    if (_isLongString(attr)) {
                        buffer_.write(lineSeparator_);
                        _writeIndent(attrIndent);
                        enter(attr);
                        leave(attr);
                        status = "cont-long";
                    } else {
                        buffer_.write(' ');
                        enter(attr);
                        leave(attr);
                        status = "cont";
                    }
                } else if ("cont-long".equals(status)) {
                    buffer_.write(lineSeparator_);
                    _writeIndent(attrIndent);
                    if (_isLongString(attr)) {
                        enter(attr);
                        leave(attr);
                        status = "cont-long";
                    } else {
                        enter(attr);
                        leave(attr);
                        status = "cont";
                    }
                } else {
                    throw (new InternalError());
                }
            }
        }
/*
        } else {
            for (int i = 0;i < attrs.length;i++) {
                Attr attr = attrs[i];
                if (attr.getSpecified()) {
                    buffer_.write(' ');
                    enter(attr);
                    leave(attr);
                }
            }
        }
*/
    }

    private boolean _isLongString(Attr attr) {
        String name = attr.getName();
        String value = attr.getValue();
        if (value != null) {
            return (name.length() + value.length() > 20);
        } else {
            return (name.length() > 20);
        }
    }

    private void _makeAttributesPlain(Attr[] attrs)
        throws IOException {
        for (int i = 0;i < attrs.length;i++) {
            Attr attr = attrs[i];
            if (attr.getSpecified()) {
                buffer_.write(' ');
                enter(attr);
                leave(attr);
            }
        }
    }

    private boolean _isIndent(Element element) {
        if (!visual_) {
            return (false);
        }
        if (indentMixedMode_ > 0) {
            return (false);
        }
        if (hasElement_(element)) {
            return (true);
        } else {
            return (false);
        }
    }

    private boolean hasElement_(Element element) {
        NodeList children = element.getChildNodes();
        int size = children.getLength();
        for (int i = 0; i < size; i++) {
            if (children.item(i) instanceof Element) {
                return (true);
            }
        }
        return (false);
    }

    private boolean isMixedElement_(Element element) {
        if (wouldBeMixedElement_(element)) {
            return (true);
        }
        String localName = element.getLocalName();
        if (localName == null) {
            localName = element.getTagName();
        }
        return (mixedElements_.contains(localName));
    }

    private boolean wouldBeMixedElement_(Element element) {
        if (!autoMixed_) {
            return (false);
        }
        NodeList children = element.getChildNodes();
        int size = children.getLength();
        for (int i = 0;i < size;i++) {
            Node child = children.item(i);
            if (child instanceof Text) {
                return (true);
            }
        }
        return (false);
    }

    public void leave(Element element) {
        String tag = element.getTagName();
        try {
            if (isMixedElement_(element)) { 
                indentMixedMode_--;
                if (indentMixedMode_ > 0) {
                    buffer_.write("</" + tag + ">");
                } else {
                    buffer_.write("</" + tag + ">");
                    buffer_.write(lineSeparator_);
                }
            } else if (indentMixedMode_ > 0) {
                buffer_.write("</" + tag + ">");
            } else if (_isIndent(element)) {
                _indentDown();
                _writeIndent();
                buffer_.write("</" + tag + ">");
                buffer_.write(lineSeparator_);
            } else if (visual_) {
                buffer_.write("</" + tag + ">");
                buffer_.write(lineSeparator_);
            } else {
                buffer_.write("</" + tag + ">");
            }
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public boolean enter(Attr attr) {
        try {
            buffer_.write(attr.getName());
            if (javaString_) {
                buffer_.write("='");
                buffer_.write(UXML.escapeAttrQuot(attr.getValue()));
                buffer_.write('\'');
            } else {
                buffer_.write("=\"");
                buffer_.write(UXML.escapeAttrQuot(attr.getValue()));
                buffer_.write('\"');
            }
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
        return (true);
    }

    public void leave(Attr attr) {
    }

    public boolean enter(Text text) {
        try {
            String string;
            if (!isEscapeXmlChars_) {
                string = text.getData();
            } else if (escapeCr_) {
                string = UXML.escapeCharDataCr(text.getData());
            } else {
                string = UXML.escapeCharData(text.getData());
            }
            if (javaString_) {
                appendJavaString_(string);
            } else {
                buffer_.write(string);
            }
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
        return (true);
    }

    private void appendJavaString_(String string) throws IOException {
        char[] chars = string.toCharArray();
        for (int i = 0;i < chars.length;i++) {
            char c = chars[i];
            if (c == '\n') {
                buffer_.write("\\n");
            } else if (c == '"') {
                buffer_.write("\\\"");
            } else {
                buffer_.write(c);
            }
        }
    }

    public void leave(Text text) {
    }

    public boolean enter(CDATASection cdata) {
        try {
            buffer_.write("<![CDATA[");
            buffer_.write(cdata.getData());
            buffer_.write("]]>");
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
        return (true);
    }

    public void leave(CDATASection cdata) {
    }

    public boolean enter(EntityReference entityRef) {
        try {
            if (expandEntityReference_ && UDOM.isParsedEntity(entityRef)) {
                return (true);
            } else {
                buffer_.write("&");
                buffer_.write(entityRef.getNodeName());
                buffer_.write(";");
                return (false);
            }
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public void leave(EntityReference entityRef) {
    }

    public boolean enter(Entity entity) {
        try {
            String name = entity.getNodeName();
            String pid = entity.getPublicId();
            String sid = entity.getSystemId();
            String notation = entity.getNotationName();
            if (sid != null) {
                UXMLMaker.makeUnparsedEntity(name, pid, sid, notation, buffer_);
            } else {
                buffer_.write("<!ENTITY ");
                buffer_.write(name);
                buffer_.write(" \"");
                XMLMaker entityMaker = new XMLMaker();
                UDOMVisitor.traverseChildren(entity, entityMaker);
                buffer_.write(UXML.escapeEntityQuot(entityMaker.getText()));
                buffer_.write("\"");
                buffer_.write(">");
            }
            return (false);
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public void leave(Entity entity) {
    }

    public boolean enter(ProcessingInstruction pi) {
        try {
            UXMLMaker.makeProcessingInstruction(
                pi.getTarget(),
                pi.getData(),
                buffer_);
            return (true);
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public void leave(ProcessingInstruction pi) {
    }

    public boolean enter(Comment comment) {
        try {
            buffer_.write("<!--");
            buffer_.write(comment.getData());
            buffer_.write("-->");
            return (true);
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public void leave(Comment comment) {
    }

    public boolean enter(Document doc) {
        if (javaString_) {
            return (true);
        }
        String name = doc.getDocumentElement().getTagName();
        try {
            if (!(simpleXmlDeclaration_ && encoding_.toUpperCase().equals("UTF-8"))) {
                buffer_.write("<?xml version=\"1.0\" encoding=\"");
                buffer_.write(encoding_);
                buffer_.write("\" ?>");
                buffer_.write(lineSeparator_);
            }
            makeHeadComment_();
            if (systemId_ != null) {
                buffer_.write("<!DOCTYPE ");
                buffer_.write(name);
                if (publicId_ != null) {
                    buffer_.write(" PUBLIC \"");
                    buffer_.write(publicId_);
                    buffer_.write("\"");
                    buffer_.write(" \"");
                    buffer_.write(systemId_);
                    buffer_.write("\"");
                } else {
                    buffer_.write(" SYSTEM \"");
                    buffer_.write(systemId_);
                    buffer_.write("\"");
                }
                buffer_.write(">");
                buffer_.write(lineSeparator_);
            }
            return (true);
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    private void makeHeadComment_() throws IOException {
        if (headComment_ == null) {
            return;
        }
        buffer_.write("<!--");
        buffer_.write(lineSeparator_);
        int size = headComment_.length();
        for (int i = 0;i < size;i++) {
            int c = headComment_.charAt(i);
            if (c == '\n') {
                buffer_.write(lineSeparator_);
            } else {
                buffer_.write((char)c);
            }
        }
        buffer_.write("-->");
        buffer_.write(lineSeparator_);
    }

    public void leave(Document doc) {
    }

    public boolean enter(DocumentType doctype) {
        if (systemId_ != null) {
            return (true);
        }
        try {
            if (dom2_) {
                String name = doctype.getName();
                String publicId = doctype.getPublicId();
                String systemId = doctype.getSystemId();
                String internalSubset = doctype.getInternalSubset();
                buffer_.write("<!DOCTYPE ");
                buffer_.write(name);
                if (publicId != null) {
                    buffer_.write(" PUBLIC \"");
                    buffer_.write(publicId);
                    buffer_.write("\"");
                }
                if (systemId != null) {
                    buffer_.write(" SYSTEM \"");
                    buffer_.write(systemId);
                    buffer_.write("\"");
                }
                if (internalSubset != null) {
                    buffer_.write(" [");
                    buffer_.write(internalSubset);
                    buffer_.write("]");
                }
                buffer_.write(">");
                buffer_.write(lineSeparator_);
                return (true);
            } else {
                String name = doctype.getName();
                NamedNodeMap entities = doctype.getEntities();
                NamedNodeMap notations = doctype.getNotations();
                buffer_.write("<!DOCTYPE ");
                buffer_.write(name);
                if (entities != null
                    && entities.getLength() > 0
                    || notations != null
                    && notations.getLength() > 0) {

                    buffer_.write(" [");
                    int nEntities = entities.getLength();
                    for (int i = 0; i < nEntities; i++) {
                        XMLMaker entityMaker = new XMLMaker();
                        UDOMVisitor.traverse(entities.item(i), entityMaker);
                        buffer_.write(entityMaker.getText());
                    }
                    int nNotations = notations.getLength();
                    for (int i = 0; i < nNotations; i++) {
                        enter((Notation)notations.item(i));
                        leave((Notation)notations.item(i));
                    }
                    buffer_.write("]");
                }
                buffer_.write(">");
                buffer_.write(lineSeparator_);
            }
            return (true);
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public void leave(DocumentType doctype) {
    }

    public boolean enter(DocumentFragment docfrag) {
        return (true);
    }

    public void leave(DocumentFragment docfrag) {
    }

    public boolean enter(Notation notation) {
        try {
            String name = notation.getNodeName();
            String pid = notation.getPublicId();
            String sid = notation.getSystemId();
            UXMLMaker.makeNotation(name, pid, sid, buffer_);
            return (true);
        } catch (IOException e) {
            throw (new DOMVisitorException(e));
        }
    }

    public void leave(Notation notation) {
    }

    public boolean enter(Node node) {
        throw (new InternalError());
    }

    public void leave(Node node) {
        throw (new InternalError());
    }

    private void _indentUp() {
        indentCount_ += indent_;
    }

    private void _indentDown() {
        indentCount_ -= indent_;
    }

    private void _writeIndent() throws IOException {
        for (int i = 0; i < indentCount_; i++) {
            buffer_.write(" ");
        }
    }

    private void _writeIndent(int count) throws IOException {
        for (int i = 0;i < count;i++) {
            buffer_.write(" ");
        }
    }

    public void setHeadComment(String headComment) {
        headComment_ = headComment;
    }

    public void setEscapeXmlChars(boolean escape) {
        isEscapeXmlChars_ = escape;
    }

    public void addAttributeOrder(String localName, String values) {
        attributeOrder_.put(localName, Arrays.asList(UString.getTokens(values)));
    }

    public void addMixedElement(String localName) {
        mixedElements_.add(localName);
    }
}
