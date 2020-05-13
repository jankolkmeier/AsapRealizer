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
package asap.bml.ext.bmla.feedback;

import hmi.xml.XMLNameSpace;

import java.util.ArrayList;
import java.util.List;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;
import saiba.bml.BMLInfo;
import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourParser;


import saiba.bml.feedback.BMLBlockPredictionFeedback;
import saiba.bml.feedback.BMLPredictionFeedback;
import asap.bml.ext.bmla.BMLAPrefix;

import com.google.common.collect.ImmutableList;

/**
 * BMLPredictionFeedback with BMLABlockPredictions
 * @author hvanwelbergen
 */
@Slf4j
public class BMLAPredictionFeedback extends BMLPredictionFeedback
{
    public static BMLAPredictionFeedback build(BMLPredictionFeedback bpf)
    {
        BMLAPredictionFeedback fb = new BMLAPredictionFeedback();        
        fb.readXML(bpf.toXMLString());
        return fb;
    }
    private List<BMLABlockPredictionFeedback> bmlBlockPredictions = new ArrayList<BMLABlockPredictionFeedback>();
    private List<Behaviour> bmlBehaviorPredictions = new ArrayList<Behaviour>();

    public void addBMLBlockPrediction(BMLBlockPredictionFeedback bfp)
    {
        if (bfp instanceof BMLABlockPredictionFeedback) addBMLABlockPrediction((BMLABlockPredictionFeedback)bfp);
        else addBMLABlockPrediction(BMLABlockPredictionFeedback.build(bfp));
    }
    public void addBMLABlockPrediction(BMLABlockPredictionFeedback bfp)
    {
        bmlBlockPredictions.add(bfp);
    }
    public void addBehaviorPrediction(Behaviour b)
    {
        bmlBehaviorPredictions.add(b);
    }
        
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        appendXMLStructureList(buf, fmt, bmlBlockPredictions);
        appendXMLStructureList(buf, fmt, bmlBehaviorPredictions);
        //appendXMLStructureList(buf, fmt, new ImmutableList.Builder<XMLStructure>().addAll(bmlBlockPredictions).addAll(bmlBehaviorPredictions).build());
        return buf;
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            if(tokenizer.getTagName().equals(BMLBlockPredictionFeedback.xmlTag()))
            {
                BMLABlockPredictionFeedback pred = new BMLABlockPredictionFeedback();
                pred.readXML(tokenizer);
                bmlBlockPredictions.add(pred);
            }
            else
            {
                Behaviour b = BehaviourParser.parseBehaviour(null, tokenizer);
                if(b!=null)
                {
                    bmlBehaviorPredictions.add(b);
                }
                else
                {
                    log.warn("Skipping in prediction: {}", tokenizer.getTagName());
                    tokenizer.skipTag();
                }
            }            
        }
    }
    
    public List<Behaviour> getBmlBehaviorPredictions()
    {
        return bmlBehaviorPredictions;
    }

    public List<BMLBlockPredictionFeedback> getBmlBlockPredictions()
    {
        List<BMLBlockPredictionFeedback> copy = new ArrayList<BMLBlockPredictionFeedback>();
        for (BMLABlockPredictionFeedback pred:bmlBlockPredictions) copy.add(pred);
        return copy;
    }


    public ImmutableList<BMLABlockPredictionFeedback> getBMLABlockPredictions()
    {
        return ImmutableList.copyOf(bmlBlockPredictions);
    }
    
    @Override
    public String toBMLFeedbackString(List<XMLNameSpace> xmlNamespaceList)
    {
        return super.toBMLFeedbackString(BMLAPrefix.insertBMLANamespacePrefix(xmlNamespaceList));
    }
}
