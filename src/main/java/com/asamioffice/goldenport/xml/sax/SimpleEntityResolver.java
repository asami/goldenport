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

package com.asamioffice.goldenport.xml.sax;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import com.asamioffice.goldenport.io.UFile;

/**
 * SimpleEntityResolver
 *
 * @since   Aug. 12, 2000
 * @version Nov. 25, 2006
 * @author  ASAMI, Tomoharu (asami@AsamiOffice.com)
 */

public class SimpleEntityResolver implements EntityResolver {
    private Map publicIds_ = new HashMap();
    private Map systemIds_ = new HashMap();
    private List relativeSystemIds_ = new ArrayList();

    public SimpleEntityResolver() {
    }

    public SimpleEntityResolver(String name, String uri) {
        _init(new String[][] { { name, uri } }, null);
    }

    public SimpleEntityResolver(String[][] systemIds) {
        _init(systemIds, null);
    }

    public SimpleEntityResolver(String[][] systemIds, String[][] publicIds) {
        _init(systemIds, publicIds);
    }

    private void _init(String[][] systemIds, String[][] publicIds) {
        if (systemIds != null) {
            List list = new ArrayList();
            for (int i = 0; i < systemIds.length; i++) {
                String systemId = systemIds[i][0];
                addSystemId(systemId, systemIds[i][1]);
            }
        }
        if (publicIds != null) {
            for (int i = 0; i < publicIds.length; i++) {
                addPublicId(publicIds[i][0], publicIds[i][1]);
            }
        }
    }

    public void addSystemId(String systemId, String uri) {
        systemIds_.put(systemId, uri);
        relativeSystemIds_.add(systemId);
    }

    public void addPublicId(String publicId, String uri) {
        publicIds_.put(publicId, uri);
    }

    public InputSource resolveEntity(String publicId, String systemId) {
        if (systemId != null) {
            if (_isExist(systemId)) {
                return (new InputSource(systemId));
            }
        }
        if (publicId != null) {
            String uri = (String)publicIds_.get(publicId);
            if (uri != null) {
                return (new InputSource(uri));
            } else {
                return (null);
            }
        }
        if (systemId != null) {
            String uri = _getURIBySystemId(systemId);
            if (uri != null) {
                return (new InputSource(uri));
            } else {
                return (new InputSource(systemId));
            }
        } else {
            return (null);
        }
    }

    private boolean _isExist(String uri) {
        try {
            URL url = new URL(uri);
            if ("file".equals(url.getProtocol())) {
                String fileName = url.getFile();
                return (UFile.isExist(fileName));
            }
            return (false); // XXX : http
        } catch (IOException e) {
            return (false);
        }
    }

    private String _getURIBySystemId(String systemId) {
        String uri = (String)systemIds_.get(systemId);
        if (uri != null) {
            return (uri);
        }
        int size = relativeSystemIds_.size();
        for (int i = 0; i < size; i++) {
            String relativeId = (String)relativeSystemIds_.get(i);
            if (systemId.endsWith(relativeId)) {
                return ((String)systemIds_.get(relativeId));
            }
        }
        return (null);
    }
}
