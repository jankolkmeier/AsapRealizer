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

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Used to group a bunch of frames into ONE continuous motion. Note: Each phase is timed locally, ie. its timing starts over from 0
 * @author hvanwelbergen
 */
public class Phase extends MURMLElement
{
    @Getter
    private List<Frame> frames = new ArrayList<Frame>();
    
    @Override
    public boolean hasContent()
    {
        return true;
    }
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, frames);
        return buf;
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(Frame.xmlTag()))
            {
                Frame frame = new Frame();
                frame.readXML(tokenizer);
                frames.add(frame);
            }
        }
    }
    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "phase";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time
     * xml tag of an object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
