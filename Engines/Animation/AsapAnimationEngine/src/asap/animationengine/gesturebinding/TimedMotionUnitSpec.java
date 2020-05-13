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

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import lombok.Getter;
import saiba.bml.core.Behaviour;
import asap.binding.SpecConstraints;
import asap.binding.SpecParameterDefault;
import asap.binding.SpecParameterDefaults;
import asap.binding.SpecParameterMap;

/**
 * XML parser for the TimedMotionUnitSpec in a gesturebinding
 * @author hvanwelbergen
 * 
 */
public class TimedMotionUnitSpec extends XMLStructureAdapter
{
    @Getter
    private String type;

    @Getter
    private String specnamespace;

    @Getter
    private SpecConstraints constraints = new SpecConstraints();
    private SpecParameterMap parameterMap = new SpecParameterMap();
    private SpecParameterDefaults parameterdefaults = new SpecParameterDefaults();
    
    @Getter
    private TimedMotionUnitConstructionInfo timedMotionUnitConstructionInfo = new TimedMotionUnitConstructionInfo();
    
    public boolean satisfiesConstraints(Behaviour b)
    {
        return constraints.satisfiesConstraints(b);
    }

    public Set<String> getParameters()
    {
        return parameterMap.getParameters();
    }

    /**
     * Get motion unit parameter for BML parameter src
     */
    public String getParameter(String src)
    {
        return parameterMap.getParameter(src);
    }

    public Collection<SpecParameterDefault> getParameterDefaults()
    {
        return parameterdefaults.getParameterDefaults();
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        type = getRequiredAttribute("type", attrMap, tokenizer);
        specnamespace = getOptionalAttribute("namespace", attrMap, null);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        String tag = "";
        while (tokenizer.atSTag())
        {
            tag = tokenizer.getTagName();
            switch (tag)
            {
            case SpecConstraints.XMLTAG:
                constraints.readXML(tokenizer);
                break;
            case SpecParameterMap.XMLTAG:
                parameterMap.readXML(tokenizer);
                break;
            case SpecParameterDefaults.XMLTAG:
                parameterdefaults.readXML(tokenizer);
                break;
            case TimedMotionUnitConstructionInfo.XMLTAG:
                timedMotionUnitConstructionInfo.readXML(tokenizer);                
                break;
            default:
                throw new XMLScanException("Invalid tag "+tag+" in TimedMotionUnitSpec");
            }
        }

    }

    private static final String XMLTAG = "TimedMotionUnitSpec";

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
