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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.feedback.BMLBlockProgressFeedback;
import saiba.bml.feedback.BMLFeedback;
import saiba.bml.feedback.BMLWarningFeedback;
import saiba.utils.TestUtil;
import asap.bml.ext.bmlt.BMLTInfo;

/**
 * Unit tests for the BMLAFeedbackParser
 * @author hvanwelbergen
 */
public class BMLAFeedbackParserTest
{
    @Before
    public void setup()
    {
        BMLTInfo.init();
    }
    
    private void assertFeedbackType(Class<?>feedbackType, String str) throws IOException
    {
        BMLFeedback fb = BMLAFeedbackParser.parseFeedback(str);
        assertThat(fb,instanceOf(feedbackType));
    }
    
    @Test
    public void testBMLBlockProgressFeedback() throws IOException
    {
        String str = "<blockProgress "+TestUtil.getDefNS()+
                "id=\"bml1:start\" globalTime=\"10\" characterId=\"doctor\"/>";
        assertFeedbackType(BMLBlockProgressFeedback.class, str);
    }
    
    @Test
    public void testBMLPredictionFeedback() throws IOException
    {
        String feedback = "<predictionFeedback "+TestUtil.getDefNS()+">"
                + "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " +
                        "id=\"bml1\" globalStart=\"1\" globalEnd=\"7\"/>"
                + "<gesture id=\"bml1:gesture1\" lexeme=\"BEAT\" start=\"0\" ready=\"1\" strokeStart=\"3\" " +
                "stroke=\"4\" strokeEnd=\"5\" relax=\"6\" end=\"7\"/>"
                + "<head id=\"bml1:head1\" lexeme=\"NOD\" start=\"0\" ready=\"1\" " +
                "strokeStart=\"3\" stroke=\"4\" strokeEnd=\"5\" relax=\"6\" end=\"7\"/>"
                + "</predictionFeedback>";
        assertFeedbackType(BMLAPredictionFeedback.class, feedback);
    }
    
    @Test
    public void testBMLSyncPointProgressFeedback() throws IOException
    {
        String str = "<syncPointProgress "+TestUtil.getDefNS()+
                " characterId=\"doctor\" id=\"bml1:gesture1:stroke\" time=\"10\" globalTime=\"111\"/>";
        assertFeedbackType(BMLASyncPointProgressFeedback.class, str);
    }
    
    @Test
    public void testBMLWarningFeedback() throws IOException
    {
        String str = "<warningFeedback "+TestUtil.getDefNS()+"id=\"bml1\" characterId=\"doctor\" type=\"PARSING_FAILURE\">content</warningFeedback>";
        assertFeedbackType(BMLWarningFeedback.class, str);
    }
    
    @Test(timeout=200)
    public void testLoop() throws IOException
    {
        String str = "<predictionFeedback xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\">"+
        "<unknown id=\"unknown\"/>"+        
        "</predictionFeedback>";
        assertFeedbackType(BMLAPredictionFeedback.class, str);
    }
}
