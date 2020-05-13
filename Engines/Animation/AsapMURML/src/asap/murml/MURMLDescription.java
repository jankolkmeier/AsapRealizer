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
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * Parser for the MURML BML description
 * @author hvanwelbergen
 * 
 */
public class MURMLDescription extends MURMLElement
{
    @Getter
    private String id;

    @Getter
    private String scope;

    @Getter
    private boolean pointStroke;

    @Getter
    @Setter
    private int priority = 100;

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        id = getOptionalAttribute("id", attrMap);
        scope = getOptionalAttribute("scope", attrMap);
        pointStroke = getOptionalBooleanAttribute("pointStroke", attrMap, false);
    }

    @Getter
    private Dynamic dynamic;

    @Getter
    private Static staticElement;

    @Getter
    private Parallel parallel;

    @Getter
    private Symmetrical symmetrical;

    @Getter
    private Sequence sequence;

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (dynamic != null)
        {
            buf = dynamic.appendXML(buf, fmt);
        }
        if (parallel != null)
        {
            buf = parallel.appendXML(buf, fmt);
        }
        if (staticElement != null)
        {
            buf = staticElement.appendXML(buf, fmt);
        }
        if (sequence != null)
        {
            buf = sequence.appendXML(buf, fmt);
        }
        return buf;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        String tag = tokenizer.getTagName();
        switch (tag)
        {
        case Dynamic.XMLTAG:
            dynamic = new Dynamic();
            dynamic.readXML(tokenizer);
            break;
        case Parallel.XMLTAG:
            parallel = new Parallel();
            parallel.readXML(tokenizer);
            break;
        case Symmetrical.XMLTAG:
            symmetrical = new Symmetrical();
            symmetrical.readXML(tokenizer);
            break;
        case Static.XMLTAG:
            staticElement = new Static();
            staticElement.readXML(tokenizer);
            break;
        case Sequence.XMLTAG:
            sequence = new Sequence();
            sequence.readXML(tokenizer);
            break;
        default:
            throw new XMLScanException("Invalid tag " + tag + " in <murml-description>");
        }
        normalizeSymmetricals();
    }

    private void normalizeSymmetricals()
    {
        if (symmetrical != null)
        {
            parallel = symmetrical.normalize();
        }
        else if (parallel != null)
        {
            parallel.normalizeInnerSymmetricals();
        }
        else if (sequence != null)
        {
            sequence.normalizeInnerSymmetricals();
        }
    }

    private static final String XMLTAG = "murml-description";

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
