/*
 * The RelaxerOrg class library
 *  Copyright (C) 1997-2001  ASAMI, Tomoharu (asami@zeomtech.com)
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

import java.io.*;
import org.xml.sax.*;

/**
 * DTDSkipper
 * 
 * @since May. 28, 2001
 * @version Nov. 25, 2006
 * @author ASAMI, Tomoharu (asami@asamioffice.com)
 */
public class DTDSkipper implements EntityResolver {
    public InputSource resolveEntity(String publicId, String systemId) {
        if (!systemId.endsWith(".dtd")) {
            return (null);
        }
        StringReader reader = new StringReader("");
        InputSource is = new InputSource(reader);
        return (is);
    }
}
