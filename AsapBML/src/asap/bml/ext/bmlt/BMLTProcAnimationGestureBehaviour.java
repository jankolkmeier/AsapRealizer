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
package asap.bml.ext.bmlt;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import saiba.bml.parser.SyncPoint;

import com.google.common.collect.ImmutableList;

/**
 * Uses procanimation as a gesture, that is, with automatic preparation/retraction
 * @author hvanwelbergen
 *
 */
public class BMLTProcAnimationGestureBehaviour extends BMLTBehaviour
{
    @Getter
    private String content = null;

    @Getter
    private String fileName = null;
    
    public BMLTProcAnimationGestureBehaviour(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    public BMLTProcAnimationGestureBehaviour(String bmlId, String id, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId, id);
        readXML(tokenizer);
    }

    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start", "ready", "strokeStart", "stroke", "strokeEnd", "relax",
            "end");

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    @Override
    public void addDefaultSyncPoints()
    {
        for (String s : getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }
    }

    @Override
    public boolean hasContent()
    {
        return true;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        fileName = getOptionalAttribute("fileName", attrMap, null);
        super.decodeAttributes(attrMap, tokenizer);        
    }

    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        if(fileName!=null)
        {
            appendAttribute(buf,"fileName",fileName);
        }
        return super.appendAttributeString(buf, fmt);
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(BMLTParameter.xmlTag()))
            {
                decodeBMLTParameter(tokenizer);
            }
            else if (tag.equals("ProcAnimation"))
            {
                content = tokenizer.getXMLSection();
            }
            else
            {
                throw new XMLScanException("Invalid content " + tag + " in BMLTBehavior " + id);
            }
        }
        super.decodeContent(tokenizer);
    }

    private static final String XMLTAG = "procanimationgesture";

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
