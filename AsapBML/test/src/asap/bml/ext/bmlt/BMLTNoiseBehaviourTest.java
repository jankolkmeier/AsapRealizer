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
package asap.bml.ext.bmlt;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;
import saiba.utils.TestUtil;

/**
 * Unit tests for the BMLTNoiseBehaviour
 * @author Herwin
 * 
 */
public class BMLTNoiseBehaviourTest extends AbstractBehaviourTest
{
    private static final float PARAMETER_PRECISION = 0.0001f;

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<bmlt:noise xmlns:bmlt=\"http://hmi.ewi.utwente.nl/bmlt\" type=\"perlin\" joint=\"j1\"" + TestUtil.getDefNS()
                + " id=\"beh1\"" + extraAttributeString + "/>";
        return new BMLTNoiseBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new BMLTNoiseBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    @Test
    public void testReadXML() throws IOException
    {
        String str = "<bmlt:noise xmlns:bmlt=\"http://hmi.ewi.utwente.nl/bmlt\" "
                + "id=\"noise1\" type=\"perlin\" joint=\"vl5\" start=\"0\" end=\"100\">"
                + "<bmlt:parameter name=\"basefreqx\" value=\"0.5\"/>" + "<bmlt:parameter name=\"baseamplitudex\" value=\"0.05\"/>"
                + "</bmlt:noise>";
        BMLTNoiseBehaviour noiseBeh = new BMLTNoiseBehaviour("bml1", new XMLTokenizer(str));
        assertEquals(0.5, noiseBeh.getFloatParameterValue("basefreqx"), PARAMETER_PRECISION);
        assertEquals(0.05, noiseBeh.getFloatParameterValue("baseamplitudex"), PARAMETER_PRECISION);
        assertEquals("vl5", noiseBeh.getStringParameterValue("joint"));
        assertEquals("perlin", noiseBeh.getStringParameterValue("type"));
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<bmlt:noise xmlns:bmlt=\"http://hmi.ewi.utwente.nl/bmlt\" "
                + "id=\"noise1\" type=\"perlin\" joint=\"vl5\" start=\"0\" end=\"100\">"
                + "<bmlt:parameter name=\"basefreqx\" value=\"0.5\"/>" + "<bmlt:parameter name=\"baseamplitudex\" value=\"0.05\"/>"
                + "</bmlt:noise>";
        BMLTNoiseBehaviour noiseIn = new BMLTNoiseBehaviour("bml1", new XMLTokenizer(str));
        
        StringBuilder buf = new StringBuilder();
        noiseIn.appendXML(buf);
        BMLTNoiseBehaviour noiseOut = new BMLTNoiseBehaviour("bml1",new XMLTokenizer(buf.toString()));
        assertEquals(0.5, noiseOut.getFloatParameterValue("basefreqx"), PARAMETER_PRECISION);
        assertEquals(0.05, noiseOut.getFloatParameterValue("baseamplitudex"), PARAMETER_PRECISION);
        assertEquals("vl5", noiseOut.getStringParameterValue("joint"));
        assertEquals("perlin", noiseOut.getStringParameterValue("type"));
    }
}
