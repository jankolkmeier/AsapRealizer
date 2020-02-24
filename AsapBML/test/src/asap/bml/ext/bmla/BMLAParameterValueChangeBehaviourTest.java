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
package asap.bml.ext.bmla;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;
import saiba.utils.TestUtil;

/**
 * Unit tests for the parametervaluechange behavior
 * @author welberge
 * 
 */
public class BMLAParameterValueChangeBehaviourTest extends AbstractBehaviourTest
{
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<bmla:parametervaluechange xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" id=\"a1\" "+TestUtil.getDefNS()
                +"paramId=\"volume\" target=\"bmlx1:behx\" "
                + extraAttributeString+">"
                + "<bmla:trajectory type=\"linear\" targetValue=\"100\" initialValue=\"0\"/>" + "</bmlt:parametervaluechange>";        
                return new BMLAParameterValueChangeBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new BMLAParameterValueChangeBehaviour(bmlId, new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String bmlString = "<bmla:parametervaluechange xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" id=\"a1\" start=\"nod1:end\" "
                + "paramId=\"volume\" target=\"bml1:speech1\">"
                + "<bmla:trajectory type=\"linear\" targetValue=\"100\" initialValue=\"0\"/>" + "</bmlt:parametervaluechange>";
        BMLAParameterValueChangeBehaviour beh = new BMLAParameterValueChangeBehaviour("bmla", new XMLTokenizer(bmlString));
        assertEquals("bmla", beh.getBmlId());
        assertEquals("a1", beh.id);

        assertEquals("linear", beh.getStringParameterValue("type"));
        assertEquals("0", beh.getStringParameterValue("initialValue"));
        assertEquals("100", beh.getStringParameterValue("targetValue"));
        assertEquals("bml1:speech1", beh.getStringParameterValue("target"));
        assertEquals("nod1", beh.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end", beh.getSyncPoints().get(0).getRef().syncId);
    }

    @Test
    public void testWriteXML() throws IOException
    {
        String bmlString = "<bmla:parametervaluechange xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" id=\"a1\" start=\"nod1:end\" "
                + "paramId=\"volume\" target=\"bml1:speech1\">"
                + "<bmla:trajectory type=\"linear\" targetValue=\"100\" initialValue=\"0\"/>" + "</bmlt:parametervaluechange>";
        BMLAParameterValueChangeBehaviour behIn = new BMLAParameterValueChangeBehaviour("bmla", new XMLTokenizer(bmlString));

        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf, new XMLFormatting(), "bmlt", "http://hmi.ewi.utwente.nl/bmlt");
        BMLAParameterValueChangeBehaviour behOut = new BMLAParameterValueChangeBehaviour("bmla", new XMLTokenizer(buf.toString()));

        assertEquals("bmla", behOut.getBmlId());
        assertEquals("a1", behOut.id);
        assertEquals("linear", behOut.getStringParameterValue("type"));
        assertEquals("0", behOut.getStringParameterValue("initialValue"));
        assertEquals("100", behOut.getStringParameterValue("targetValue"));
        assertEquals("bml1:speech1", behOut.getStringParameterValue("target"));
        assertEquals("nod1", behOut.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end", behOut.getSyncPoints().get(0).getRef().syncId);
    }    
}
