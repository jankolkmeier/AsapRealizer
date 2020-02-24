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
package asap.bml.ext.murml;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;
import saiba.bml.core.Behaviour;
import asap.bml.ext.bmla.BMLAInfo;
import asap.murml.MURMLDescription;

/**
 * Generic class for MURML behaviours
 * @author hvanwelbergen
 *
 */
public abstract class MURMLBehaviour extends Behaviour
{
    @Getter
    private MURMLDescription murmlDescription;
    
    public MURMLBehaviour(String bmlId)
    {
        super(bmlId);        
    }
    
    public MURMLBehaviour(String bmlId, String id)
    {
        super(bmlId, id);        
    }
    
    static final String MURMLNAMESPACE = "http://www.techfak.uni-bielefeld.de/ags/soa/murml";

    @Override
    public String getNamespace()
    {
        return MURMLNAMESPACE;
    }

    @Override
    public boolean hasContent()
    {
        return true;
    }
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        return murmlDescription.appendXML(buf,fmt);
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        if(tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(MURMLDescription.xmlTag()))
            {
                murmlDescription = new MURMLDescription();
                murmlDescription.readXML(tokenizer);
                if(specifiesParameter(BMLAInfo.BMLA_NAMESPACE+":priority"))
                {
                   murmlDescription.setPriority((int)getFloatParameterValue(BMLAInfo.BMLA_NAMESPACE+":priority")); 
                }
            }
        }
        ensureDecodeProgress(tokenizer);
    }
}
