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

import hmi.xml.XMLTokenizer;

import java.io.IOException;


import saiba.bml.feedback.*;

/**
 * Parses BMLAFeedback. Modified by Dennis wrt original that used to only wrap the BMLFeedbackParser to provide BMLAFeedback
 * @author hvanwelbergen
 * @author Dennis Reidsma
 */
public class BMLAFeedbackParser
{
    private BMLAFeedbackParser()
    {
    }

    /**
     * @param str the feedback String
     * @return the corresponding instance of feedback, null if none matching 
     * @throws IOException 
     */
    public static final BMLFeedback parseFeedback(String str) throws IOException
    {
        XMLTokenizer tok = new XMLTokenizer(str);
       
        if(tok.atSTag(BMLABlockProgressFeedback.xmlTag()))
        {
            BMLABlockProgressFeedback fb = new BMLABlockProgressFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLAPredictionFeedback.xmlTag()))
        {
            BMLAPredictionFeedback fb = new BMLAPredictionFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLWarningFeedback.xmlTag()))
        {
            BMLWarningFeedback fb = new BMLWarningFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        else if(tok.atSTag(BMLASyncPointProgressFeedback.xmlTag()))
        {
            BMLASyncPointProgressFeedback fb = new BMLASyncPointProgressFeedback();
            fb.readXML(tok);
            return fb;                        
        }
        return null;
    }
}
