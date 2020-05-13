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
package asap.middlewareengine.viseme;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

/**
 * given a viseme number, return the appropriate middleware json content. 
 * 
 * The mapping is read from a resource file. Note: meaning of viseme number dependent on chose viseme set, e.g., Disney 13, or IKP. See corresponding faceengine classes for the details of the visemes
 *
 * @author Dennis Reidsma
 */
public class VisemeToJsonMapping extends XMLStructureAdapter
{
    /** key: string representation of the viseme number. Value: the json content */
    private Map<String,String> mapping = new HashMap<String,String>();

    
    /**
     * Get the json content for viseme vis. Returns null if not found.
     */
    public String getJsonForViseme(int vis)
    {
      return mapping.get(String.valueOf(vis));
    }

    /**
     * Get the json content for viseme vis. Returns null if not found.
     */
    public String getJsonForViseme(String vis)
    {
      return mapping.get(vis);
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (!tag.equals("Mapping")) throw new XMLScanException("Unknown element in VisemeToMiddlewarePoseMapping: "+tag);
            HashMap<String, String> attrMap = tokenizer.getAttributes();
            String viseme = getRequiredAttribute("viseme", attrMap, tokenizer);
            tokenizer.takeSTag("Mapping");
            String json = tokenizer.takeCharData();
            mapping.put(viseme,json);
            tokenizer.takeETag("Mapping");
        }
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "VisemeToJsonMapping";
 
    /**
     * The XML Stag for XML encoding -- use this static method when you want to see if a given String equals
     * the xml tag for this class
     */
    public static String xmlTag() { return XMLTAG; }
 
    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time xml tag of an object
     */
    @Override
    public String getXMLTag() {
       return XMLTAG;
    }  
}