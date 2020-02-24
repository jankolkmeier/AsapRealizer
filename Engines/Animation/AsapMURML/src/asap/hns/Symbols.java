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
package asap.hns;

import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

/**
 * Parses hns symbols
 * @author hvanwelbergen
 *
 */
public class Symbols extends XMLStructureAdapter
{
    @Getter
    private Map<String, Map<String, Double>> symbols = new HashMap<>(); // className->(name->value)
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if(tag.equals(Symbol.xmlTag()))
            {
                Symbol s = new Symbol();
                s.readXML(tokenizer);   
                Map<String, Double> map = symbols.get(s.getClassName());
                if(map==null)
                {
                    map = new HashMap<>();
                    symbols.put(s.getClassName(),map);
                }
                map.put(s.getName(), s.getValue());
            }            
            else
            {
                throw new XMLScanException("Invalid tag "+tag+" in <symbols>");
            }
        }
    }    
    
    static final String XMLTAG = "symbols";

    public final static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
