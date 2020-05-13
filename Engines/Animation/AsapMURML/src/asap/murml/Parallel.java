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
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Parser for the MURML parallel element
 * @author hvanwelbergen
 * 
 */
public class Parallel extends MURMLElement implements MovementConstraint
{
    @Getter
    private List<Dynamic> dynamics = new ArrayList<>();

    @Getter
    private List<Sequence> sequences = new ArrayList<>();

    @Getter
    private List<Static> statics = new ArrayList<>();
    
    @Getter
    private List<Symmetrical> symmetricals = new ArrayList<>();

    public void add(Sequence seq)
    {
        sequences.add(seq);
    }
    
    public void add(Dynamic d)
    {
        dynamics.add(d);
    }
    
    public void add(Static s)
    {
        statics.add(s);
    }
    
    public void normalizeInnerSymmetricals()
    {
        for(Symmetrical sym: symmetricals)
        {
            Parallel p = sym.normalize();
            dynamics.addAll(p.getDynamics());
            statics.addAll(p.getStatics());            
        }
        for(Sequence seq: sequences)
        {
            seq.normalizeInnerSymmetricals();
        }
    }
    
    public void makeSymmetric(Dominant dominantHand, Symmetry sym)
    {
        List<Static> newStatics = new ArrayList<Static>();
        for(Static s:statics)
        {
            newStatics.addAll(Static.constructMirror(s, dominantHand, sym).getStatics());
        }
        statics = newStatics;
        
        List<Dynamic> newDynamics = new ArrayList<Dynamic>();
        for(Dynamic d:dynamics)
        {
            newDynamics.addAll(Dynamic.constructMirror(d, dominantHand,sym).getDynamics());
        }
        dynamics = newDynamics;
        
        for(Sequence seq:sequences)
        {
            seq.makeSymmetric(dominantHand, sym);
        }
        
        if(symmetricals.size()>0)
        {
            throw new XMLScanException("Cannot have inner <symmetric> inside another symmetric block.");
        }
    }
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, dynamics);
        appendXMLStructureList(buf, fmt, sequences);
        appendXMLStructureList(buf, fmt, statics);
        appendXMLStructureList(buf, fmt, symmetricals);
        return buf;
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(Dynamic.xmlTag()))
            {
                Dynamic dynamic = new Dynamic();
                dynamic.readXML(tokenizer);
                dynamics.add(dynamic);
            }
            else if (tag.equals(Static.xmlTag()))
            {
                Static s = new Static();
                s.readXML(tokenizer);
                statics.add(s);
            }
            else if (tag.equals(Sequence.xmlTag()))
            {
                Sequence s = new Sequence();
                s.readXML(tokenizer);
                sequences.add(s);
            }
            else if (tag.equals(Symmetrical.xmlTag()))
            {
                Symmetrical s = new Symmetrical();
                s.readXML(tokenizer);
                symmetricals.add(s);
            }
            else
            {
                throw new XMLScanException("Unknown element " + tag + " in parallel");
            }
        }
    }

    static final String XMLTAG = "parallel";

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
