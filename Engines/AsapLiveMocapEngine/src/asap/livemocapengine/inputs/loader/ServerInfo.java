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
package asap.livemocapengine.inputs.loader;

import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

import lombok.Getter;

/**
 * Parses generic serverinfo XML element<br> 
 * <serverinfo host=".." port=".."/>
 * @author welberge
 *
 */
class ServerInfo extends XMLStructureAdapter
{
    @Getter
    private String hostName;
    @Getter
    private int port;

    private static final String XMLTAG = "serverinfo";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        hostName = getRequiredAttribute("host", attrMap, tokenizer);
        port = getRequiredIntAttribute("port", attrMap, tokenizer);
        super.decodeAttributes(attrMap, tokenizer);
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
