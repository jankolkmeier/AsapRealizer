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
import saiba.bml.core.BehaviourBlock;
import saiba.utils.TestUtil;
import asap.bml.ext.bmlt.BMLTInfo;

/**
 * Unit tests for the activate behavior
 * @author welberge
 *
 */
public class BMLAActivateBehaviourTest extends AbstractBehaviourTest
{
    static
    {
        BMLTInfo.init();
    }
    
    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<bmla:activate xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" "+TestUtil.getDefNS()+"id=\"a1\" target=\"bmltarget\""+ 
                extraAttributeString+"/>";        
                return new BMLAActivateBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new BMLAActivateBehaviour(bmlId,new XMLTokenizer(bmlString));
    }
    
    @Test
    public void testReadXML() throws IOException
    {
        String bmlString = "<bmla:activate xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" id=\"a1\" start=\"nod1:end\" target=\"bml1\"/>";
        BMLAActivateBehaviour beh = new BMLAActivateBehaviour("bmla", new XMLTokenizer(bmlString));
        assertEquals("bmla",beh.getBmlId());
        assertEquals("a1",beh.id);
        assertEquals("bml1",beh.getTarget());
        assertEquals("bml1",beh.getStringParameterValue("target"));
        assertEquals("nod1",beh.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end",beh.getSyncPoints().get(0).getRef().syncId);
    }
    
    @Test
    public void testActivateInBML() throws IOException
    {
        String bmlString = "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml1\">"+
                            "<bmla:activate xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" id=\"a1\" start=\"nod1:end\" target=\"bml1\"/>"+
                           "</bml>";
        BehaviourBlock bb = new BehaviourBlock(new XMLTokenizer(bmlString));
        assertEquals(1,bb.behaviours.size());
        BMLAActivateBehaviour beh = (BMLAActivateBehaviour)bb.behaviours.get(0);
        assertEquals("bml1",beh.getBmlId());
        assertEquals("bml1",beh.getTarget());        
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String bmlString = "<bmla:activate xmlns:bmla=\""+BMLAInfo.BMLA_NAMESPACE+"\" id=\"a1\" start=\"nod1:end\" target=\"bml1\"/>";
        BMLAActivateBehaviour behIn = new BMLAActivateBehaviour("bmla", new XMLTokenizer(bmlString));
        StringBuilder buf = new StringBuilder();        
        behIn.appendXML(buf, new XMLFormatting(), "bmla", BMLAInfo.BMLA_NAMESPACE);
        BMLAActivateBehaviour behOut = new BMLAActivateBehaviour("bmla", new XMLTokenizer(buf.toString()));
        
        assertEquals("bmla",behOut.getBmlId());
        assertEquals("a1",behOut.id);
        assertEquals("bml1",behOut.getTarget());
        assertEquals("bml1",behOut.getStringParameterValue("target"));
        assertEquals("nod1",behOut.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end",behOut.getSyncPoints().get(0).getRef().syncId);
    }

    
}
