/*******************************************************************************
 * Copyright (C) 2009-2020 Human Media Interaction, University of Twente, the Netherlands
 *
 * This file is part of the Articulated Social Agents Platform BML realizer (ASAPRealizer).
 *
 * ASAPRealizer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License (LGPL) as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ASAPRealizer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with ASAPRealizer.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package asap.murml;

import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

import lombok.Getter;

/**
 * Parses the MURML value element
 * @author hvanwelbergen
 */
public class Value extends MURMLElement
{
    @Getter
    private String type;

    @Getter
    private String name;
    
    @Getter
    private String id;

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        type = getOptionalAttribute("type", attrMap, "");
        id = getOptionalAttribute("id", attrMap, "");
        if(id.isEmpty() && type.isEmpty())
        {
            throw new XMLScanException("Value should have either and id or a type");
        }
        name = getRequiredAttribute("name", attrMap, tokenizer);        
    }

    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if(type!=null && !type.isEmpty())
        {
            appendAttribute(buf, "type", type.toString());
        }
        if(id!=null && !id.isEmpty())
        {
            appendAttribute(buf, "id", id);
        }
        appendAttribute(buf, "name", name);
        return buf;
    }
    
    private static final String XMLTAG = "value";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
