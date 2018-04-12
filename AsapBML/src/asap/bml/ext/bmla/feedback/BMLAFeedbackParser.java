/*******************************************************************************
 *******************************************************************************/
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
