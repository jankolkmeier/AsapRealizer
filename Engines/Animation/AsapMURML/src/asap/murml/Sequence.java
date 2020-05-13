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
 * Parser for the MURML sequence element
 * @author hvanwelbergen
 * 
 */
public class Sequence extends MURMLElement implements MovementConstraint
{
    @Getter
    private List<MovementConstraint> sequence = new ArrayList<>();

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, sequence);
        return buf;
    }
    
    static final String XMLTAG = "sequence";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    public void normalizeInnerSymmetricals()
    {
        List<MovementConstraint> newSequence = new ArrayList<MovementConstraint>();
        for(MovementConstraint mc:sequence)
        {
            if (mc instanceof Symmetrical)
            {
                newSequence.add(((Symmetrical)mc).normalize());
            }
            else
            {
                newSequence.add(mc);
                if(mc instanceof Parallel)
                {
                    ((Parallel)mc).normalizeInnerSymmetricals();
                }
            }
        }
        sequence = newSequence;
    }
    
    public void makeSymmetric(Dominant dominantHand, Symmetry sym)
    {
        List<MovementConstraint> newSequence = new ArrayList<MovementConstraint>();

        for (MovementConstraint mc : sequence)
        {
            if (mc instanceof Dynamic)
            {
                newSequence.add(Dynamic.constructMirror((Dynamic) mc, dominantHand, sym));
            }
            else if (mc instanceof Static)
            {
                newSequence.add(Static.constructMirror((Static) mc, dominantHand, sym));
            }
            else if (mc instanceof Parallel)
            {
                ((Parallel)mc).makeSymmetric(dominantHand, sym);
                newSequence.add(mc);
            }
            else if (mc instanceof Symmetrical)
            {
                throw new XMLScanException("Cannot have inner <symmetric> inside another symmetric block.");
            }
        }
        sequence = newSequence;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            switch (tag)
            {
            case Dynamic.XMLTAG:
                Dynamic dynamic = new Dynamic();
                dynamic.readXML(tokenizer);
                sequence.add(dynamic);
                break;
            case Static.XMLTAG:
                Static s = new Static();
                s.readXML(tokenizer);
                sequence.add(s);
                break;
            case Parallel.XMLTAG:
                Parallel par = new Parallel();
                par.readXML(tokenizer);
                sequence.add(par);
                break;
            case Symmetrical.XMLTAG:
                Symmetrical sym = new Symmetrical();
                sym.readXML(tokenizer);
                sequence.add(sym);
                break;
            default:
                throw new XMLScanException("Unknown element " + tag + " in sequence");
            }
        }
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
