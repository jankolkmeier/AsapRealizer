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
package asap.ipaacaeventengine.bml;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;

import lombok.Getter;

import com.google.common.collect.ImmutableList;

import saiba.bml.core.Behaviour;
import saiba.bml.parser.SyncPoint;

/**
 * Sends an IpaacaEvent (e.g. an IpaacaMessage)
 * @author herwinvw
 *
 */
public class IpaacaEventBehaviour extends Behaviour
{
    public static final String NAMESPACE = "http://asap-project.org/ipaacaevent";
    
    @Getter
    private String event = "";
    
    public IpaacaEventBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }
    
    @Override
    public void addDefaultSyncPoints()
    {
        for(String s:getDefaultSyncPoints())
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
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        buf.append(event);
        return buf;
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        event = tokenizer.getXMLSection();
    }
    
    
    @Override
    public String getNamespace()
    {
        return NAMESPACE;
    }
    
    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","end");
    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }
    
    private static final String XMLTAG = "ipaacaevent";

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
