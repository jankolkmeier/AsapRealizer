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
package asap.visualprosody;

import hmi.math.Vec3f;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;

public class VisualProsodyLoader extends XMLStructureAdapter
{
    private static final String XMLTAG = "visualprosodyprovider";
    
    @Getter
    private float offset[] = Vec3f.getVec3f(0,0,0);
    
    @Getter
    private GaussianMixtureModel roll;
    @Getter
    private GaussianMixtureModel pitch;
    @Getter
    private GaussianMixtureModel yaw;
    @Getter
    private GaussianMixtureModel v;
    @Getter
    private GaussianMixtureModel a;
    
    private GaussianMixtureModel getInnerMixtureModel(XMLTokenizer tokenizer) throws IOException
    {
        tokenizer.takeSTag();
        GMMParser gpp = new GMMParser();
        gpp.readXML(tokenizer);
        tokenizer.takeETag();
        return gpp.constructMixtureModel();
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        String off = getOptionalAttribute("offset", attrMap, "0 0 0");
        offset = decodeFloatArray(off);
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            switch (tag)
            {
            case "roll":
                roll = getInnerMixtureModel(tokenizer);
                break;
            case "pitch":
                pitch = getInnerMixtureModel(tokenizer);
                break;
            case "yaw":
                yaw = getInnerMixtureModel(tokenizer);
                break;
            case "v":
                v = getInnerMixtureModel(tokenizer);
                break;
            case "a":
                a = getInnerMixtureModel(tokenizer);
                break;
            default:
                throw new XMLScanException("Unknown tag " + tag + " in <visualprosodyprovider>");
            }
        }
    }
    
    public VisualProsody constructProsodyProvider()
    {
        return new VisualProsody(roll, pitch, yaw, v, a, offset);
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
