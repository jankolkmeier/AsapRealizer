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

import org.junit.Before;
import org.junit.Test;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import saiba.utils.TestUtil;
import asap.bml.ext.bmla.BMLAInfo;
import asap.bml.ext.bmlt.BMLTInfo;


/**
 * Unit tests for BMLASyncPointProgressFeedbackTest
 * @author hvanwelbergen
 *
 */
public class BMLASyncPointProgressFeedbackTest
{
    private static final double PRECISION = 0.0001;
    private BMLASyncPointProgressFeedback fb = new BMLASyncPointProgressFeedback();
        
    @Before
    public void setup()
    {
        BMLTInfo.init();
    }
    
    @Test
    public void testReadXML()
    {
        String str = "<syncPointProgress " + TestUtil.getDefNS() 
                + " characterId=\"doctor\" id=\"bml1:gesture1:stroke\" "+"xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" " 
        +"time=\"10\" globalTime=\"111\" bmla:posixTime=\"100\"/>";
        fb.readXML(str);
        assertEquals("doctor", fb.getCharacterId());
        assertEquals(10, fb.getTime(), PRECISION);
        assertEquals(111, fb.getGlobalTime(), PRECISION);
        assertEquals(100, fb.getPosixTime(), PRECISION);
    }
    
    @Test
    public void testWrite()
    {
        BMLASyncPointProgressFeedback fbIn = new BMLASyncPointProgressFeedback("bml1", "beh", "start", 0, 1, 100);
        StringBuilder buf = new StringBuilder();
        fbIn.appendXML(buf);
        
        BMLASyncPointProgressFeedback fbOut = new BMLASyncPointProgressFeedback();
        fbOut.readXML(buf.toString());
        assertEquals(100, fbOut.getPosixTime(), PRECISION);
    }
    
    @Test
    public void testWriteBMLAPrefix()
    {
        BMLASyncPointProgressFeedback fbIn = new BMLASyncPointProgressFeedback("bml1", "beh", "start", 0, 1, 100);
        assertThat(fbIn.toBMLFeedbackString(),containsString("xmlns:bmla=\"http://www.asap-project.org/bmla\""));        
    }
    
    @Test
    public void testBuild()
    {
        BMLSyncPointProgressFeedback fbBML = new BMLSyncPointProgressFeedback();
        String str = "<syncPointProgress " + TestUtil.getDefNS() 
                + " characterId=\"doctor\" id=\"bml1:gesture1:stroke\" "+"xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" " 
        +"time=\"10\" globalTime=\"111\" bmla:posixTime=\"100\"/>";
        fbBML.readXML(str);
        fb = BMLASyncPointProgressFeedback.build(fbBML);
        assertEquals("doctor", fb.getCharacterId());
        assertEquals(10, fb.getTime(), PRECISION);
        assertEquals(111, fb.getGlobalTime(), PRECISION);
        assertEquals(100, fb.getPosixTime(), PRECISION);
    }
}
