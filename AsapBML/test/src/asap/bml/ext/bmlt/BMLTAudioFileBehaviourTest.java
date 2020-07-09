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
import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;
import saiba.utils.TestUtil;

/**
 * Unit testcases for the BMLTAudioFileBehaviour
 * @author hvanwelbergen
 * 
 */
public class BMLTAudioFileBehaviourTest extends AbstractBehaviourTest
{

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<bmlt:audiofile xmlns:bmlt=\"http://hmi.ewi.utwente.nl/bmlt\" " + TestUtil.getDefNS()
                + "id=\"a1\" fileName=\"blah.wav\" " + extraAttributeString + "/>";
        return new BMLTAudioFileBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new BMLTAudioFileBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    @Test
    public void testReadXML() throws IOException
    {
        String bmlString = "<bmlt:audiofile xmlns:bmlt=\"http://hmi.ewi.utwente.nl/bmlt\" id=\"a1\" start=\"nod1:end\" fileName=\"blah.wav\"/>";
        BMLTAudioFileBehaviour beh = new BMLTAudioFileBehaviour("bmla", new XMLTokenizer(bmlString));
        assertEquals("bmla", beh.getBmlId());
        assertEquals("a1", beh.id);
        assertEquals("blah.wav", beh.getStringParameterValue("fileName"));
        assertEquals("nod1", beh.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end", beh.getSyncPoints().get(0).getRef().syncId);
    }

    @Test
    public void writeXML() throws IOException
    {
        String bmlString = "<bmlt:audiofile xmlns:bmlt=\"http://hmi.ewi.utwente.nl/bmlt\" id=\"a1\" start=\"nod1:end\" fileName=\"blah.wav\"/>";
        BMLTAudioFileBehaviour behIn = new BMLTAudioFileBehaviour("bmla", new XMLTokenizer(bmlString));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf, new XMLFormatting(), "bmlt", "http://hmi.ewi.utwente.nl/bmlt");
        BMLTAudioFileBehaviour behOut = new BMLTAudioFileBehaviour("bmla", new XMLTokenizer(buf.toString()));

        assertEquals("bmla", behOut.getBmlId());
        assertEquals("a1", behOut.id);
        assertEquals("blah.wav", behOut.getStringParameterValue("fileName"));
        assertEquals("nod1", behOut.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end", behOut.getSyncPoints().get(0).getRef().syncId);
    }
}
