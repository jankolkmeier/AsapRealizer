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
package asap.animationengine.gesturebinding;

import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

import lombok.Getter;
import asap.animationengine.gaze.RestGaze;

/**
 * Creates a RestGaze from an XML description
 * @author welberge
 */
public class RestGazeAssembler extends XMLStructureAdapter
{
    @Getter
    private RestGaze restGaze;
    
    private static final String XMLTAG = "RestGaze";

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String type = getRequiredAttribute("type", attrMap, tokenizer);
        String className = getOptionalAttribute("class", attrMap, null);
        if(type.equals("class"))
        {
            try
            {
                Class<?> c = Class.forName(className);
                restGaze = c.asSubclass(RestGaze.class).newInstance();                
            }
            catch (ClassNotFoundException e)
            {
                throw new XMLScanException("RestGaze "+className+" not found.", e);
            }
            catch (InstantiationException e)
            {
                throw new XMLScanException("RestGaze "+className+" not instantiated.", e);
            }
            catch (IllegalAccessException e)
            {
                throw new XMLScanException("RestGaze "+className+" illegal access.", e);
            }
        }
    }
    
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
