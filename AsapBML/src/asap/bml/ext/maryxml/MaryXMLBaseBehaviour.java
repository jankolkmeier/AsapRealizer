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
package asap.bml.ext.maryxml;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import saiba.bml.core.SpeechBehaviour;

/**
 * Base class for all MaryTTSBehaviours
 * @author Herwin
 *
 */
class MaryXMLBaseBehaviour extends SpeechBehaviour
{
    public MaryXMLBaseBehaviour(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId, tokenizer);        
    }
    
    public MaryXMLBaseBehaviour(String bmlId, String id, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId, id, tokenizer);        
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        content = tokenizer.getXMLSectionContent();        
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "maryxml";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (content != null) buf.append(content);
        return buf;
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
    
    static final String NAMESPACE = "http://mary.dfki.de/2002/MaryXML";
    
    @Override
    public  String getNamespace() { return NAMESPACE; }
}
