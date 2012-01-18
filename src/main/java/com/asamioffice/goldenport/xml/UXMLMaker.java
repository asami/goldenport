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

package com.asamioffice.goldenport.xml;

import java.io.IOException;
import java.io.Writer;

import org.w3c.dom.Node;

import com.asamioffice.goldenport.xml.visitor.UDOMVisitor;

/**
 * UXMLMaker
 *
 * @since   Jan. 16, 2001
 * @version Jan. 16, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public final class UXMLMaker {
    public static String getXMLText(Node node) {
        return (getXMLText(node, new XMLMaker()));
    }

    public static String getXMLVisualText(Node node) {
        XMLMaker maker = new XMLMaker();
        maker.setVisual(true);
        maker.setEmptyElementTag(true);
        return (getXMLText(node, maker));
    }

    public static String getXMLVisualText(Node node, String encoding) {
        XMLMaker maker = new XMLMaker();
        maker.setVisual(true);
        maker.setEmptyElementTag(true);
        maker.setEncoding(encoding);
        return (getXMLText(node, maker));
    }
    
    public static String getJavaString(Node node) {
        XMLMaker maker = new XMLMaker();
        maker.setJavaString(true);
        return (getXMLText(node, maker));
    }

    public static String getXMLText(Node node, XMLMaker maker) {
        UDOMVisitor.traverse(node, maker);
        return (maker.getText());
    }

    public static void makeProcessingInstruction(
        String target,
        String data,
        Writer buffer)
        throws IOException {
        buffer.write("<?");
        buffer.write(target);
        buffer.write(" ");
        buffer.write(data);
        buffer.write("?>");
    }

    public static void makeUnparsedEntity(
        String name,
        String publicId,
        String systemId,
        String notation,
        Writer buffer)
        throws IOException {
        if (systemId == null) {
            throw (new IllegalArgumentException("null system id"));
        }
        buffer.write("<!ENTITY ");
        buffer.write(name);
        if (publicId != null) {
            buffer.write(" PUBLIC \"");
            buffer.write(publicId);
            buffer.write("\" \"");
            buffer.write(UXML.escapeSystemQuot(systemId));
            buffer.write("\">");
        } else {
            buffer.write(" SYSTEM \"");
            buffer.write(UXML.escapeSystemQuot(systemId));
            buffer.write("\">");
        }
        if (notation != null) {
            buffer.write(" NDATA ");
            buffer.write(notation);
            buffer.write(">");
        }
    }

    public static void makeNotation(
        String name,
        String pid,
        String sid,
        Writer buffer)
        throws IOException {
        buffer.write("<!NOTATION ");
        buffer.write(name);
        if (pid != null) {
            buffer.write(" PUBLIC \"");
            buffer.write(pid);
            buffer.write("\"");
            if (sid != null) {
                buffer.write(" \"");
                buffer.write(UXML.escapeSystemQuot(sid));
                buffer.write("\"");
            }
        } else if (sid != null) {
            buffer.write(" SYSTEM \"");
            buffer.write(UXML.escapeSystemQuot(sid));
            buffer.write("\"");
        }
        buffer.write(">");
    }
}
