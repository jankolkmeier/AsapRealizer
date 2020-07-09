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

import hmi.util.Resources;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.animationengine.motionunit.AnimationUnit;
import asap.binding.SpecParameterDefault;
import asap.binding.SpecParameterDefaults;

/**
 * Specifies the connection between visimes and motionunits
 * @author hvanwelbergen
 * 
 */
public class VisimeSpec extends XMLStructureAdapter
{
    private AnimationUnit motionUnit;
    private final Resources resources;
    private int visime;
    private SpecParameterDefaults parameterdefaults = new SpecParameterDefaults();
    private final static Logger logger = LoggerFactory.getLogger(VisimeSpec.class.getName());

    public int getVisime()
    {
        return visime;
    }

    public VisimeSpec(Resources r)
    {
        resources = r;
    }

    public AnimationUnit getMotionUnit()
    {
        return motionUnit;
    }

    /**
     * Get motion unit parameter for BML parameter src
     */
    public Collection<SpecParameterDefault> getParameterDefaults()
    {
        return parameterdefaults.getParameterDefaults();
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        visime = getRequiredIntAttribute("visime", attrMap, tokenizer);
        // specnamespace = getOptionalAttribute("namespace", attrMap, null);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        String tag = "";
        try
        {
            while (tokenizer.atSTag())
            {
                tag = tokenizer.getTagName();
                if (tag.equals(SpecParameterDefaults.xmlTag()))
                {
                    parameterdefaults.readXML(tokenizer);
                }
                else if (tag.equals(MotionUnitAssembler.xmlTag()))
                {
                    MotionUnitAssembler mua = new MotionUnitAssembler(resources);
                    mua.readXML(tokenizer);
                    motionUnit = mua.getMotionUnit();
                }
            }
        }
        catch (RuntimeException ex)
        {
            logger.warn("Cannot read visime spec, dropping element from face binding. Tag: {} ", tag);
            motionUnit = null;
        }
    }

    private static final String XMLTAG = "VisimeSpec";

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
