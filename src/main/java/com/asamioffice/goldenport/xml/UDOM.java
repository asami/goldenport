package com.asamioffice.goldenport.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * UDOM
 *
 * @since   Jul.  1, 1998
 * @version Jul. 21, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */
public class UDOM {
    public static boolean isParsedEntity(EntityReference entityRef) {
        String name = entityRef.getNodeName();
        Document doc = entityRef.getOwnerDocument();
        DocumentType doctype = doc.getDoctype();
        if (doctype == null) {
            return (false);
        }
        NamedNodeMap entities = doctype.getEntities();
        Entity entity = (Entity)entities.getNamedItem(name);
        if (entity == null) {
            return (false);
        }
        return (entity.getNotationName() == null);
    }

    public static Element getFirstElement(Element element, String ns, String localName) {
        NodeList children = element.getChildNodes();
        int size = children.getLength();
        for (int i = 0;i < size;i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                if (isMatch(ns, localName, (Element)child)) {
                    return (Element)child;
                }
            }
        }
        return null;
    }

    private static boolean isMatch(String ns, String localName, Element element) {
        return ns.equals(element.getNamespaceURI()) &&
               localName.equals(getLocalName(element));
    }

    public static String getLocalName(Element element) {
        String localName = element.getLocalName();
        if (localName != null) {
            return (localName);
        } else {
            return (element.getTagName()); // XXX
        }
    }

    public static Element[] getElements(Node element, String ns, String localName) {
        NodeList children = element.getChildNodes();
        List<Element> list = new ArrayList<Element>();
        int size = children.getLength();
//      System.out.println("getElements - '" + localName + "' : " + size);
        for (int i = 0;i < size;i++) {
            Node child = children.item(i);
            if (child instanceof Element) {
                if (localName.equals(getLocalName((Element)child)) &&
                        ns.equals(child.getNamespaceURI())) {
                    list.add((Element)child);
                }
            }
        }
        Element[] array = new Element[list.size()];
        return ((Element[])list.toArray(array));
    }
}
