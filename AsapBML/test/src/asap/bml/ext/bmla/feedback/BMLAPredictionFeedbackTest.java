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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import saiba.utils.TestUtil;
import asap.bml.ext.bmla.BMLAInfo;
import asap.bml.ext.bmlt.BMLTInfo;

/**
 * Unit tests for BMLAPredictionFeedback
 * @author hvanwelbergen
 *
 */
public class BMLAPredictionFeedbackTest
{
    @Test
    public void testReadFromXML()
    {
        BMLTInfo.init();
        String feedback = "<predictionFeedback "+" xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" "+TestUtil.getDefNS()+">"
                + "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " +
                        "id=\"bml1\" bmla:posixStartTime=\"1\" bmla:posixEndTime=\"2\"  globalStart=\"1\" globalEnd=\"7\"/>"
                + "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " +
                "id=\"bml2\" bmla:posixStartTime=\"3\" bmla:posixEndTime=\"4\" globalStart=\"1\" globalEnd=\"7\"/>"
                + "</predictionFeedback>";
        BMLAPredictionFeedback fb = new  BMLAPredictionFeedback();
        fb.readXML(feedback);
        assertEquals(2, fb.getBMLABlockPredictions().size());
        assertEquals(1, fb.getBMLABlockPredictions().get(0).getPosixStartTime());
        assertEquals(2, fb.getBMLABlockPredictions().get(0).getPosixEndTime());
        assertEquals(3, fb.getBMLABlockPredictions().get(1).getPosixStartTime());
        assertEquals(4, fb.getBMLABlockPredictions().get(1).getPosixEndTime());        
    }
    
    @Test
    public void testWriteBMLAPrefix()
    {
        BMLAPredictionFeedback fb = new  BMLAPredictionFeedback();
        assertThat(fb.toBMLFeedbackString(),containsString("xmlns:bmla=\"http://www.asap-project.org/bmla\""));        
    }
}
