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
package asap.ipaacaeventengine.bml;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;
import saiba.utils.TestUtil;

/**
 * Unit tests for IpaacaEventBehaviour
 * @author herwinvw
 *
 */
public class IpaacaEventBehaviourTest extends AbstractBehaviourTest
{
    @Test
    public void testReadXML() throws IOException
    {
        String bmlString = "<ipaaca:ipaacaevent xmlns:ipaaca=\""+IpaacaEventBehaviour.NAMESPACE+"\" id=\"e1\" start=\"nod1:end\" >"
                + "<message category=\"cat1\">"
                + "</message>"
                + "</ipaaca:ipaacaevent>";
        IpaacaEventBehaviour beh = new IpaacaEventBehaviour("bml2", new XMLTokenizer(bmlString));
        assertEquals("bml2",beh.getBmlId());
        assertEquals("e1",beh.id);
        assertEquals("nod1",beh.getSyncPoints().get(0).getRef().sourceId);
        assertEquals("end",beh.getSyncPoints().get(0).getRef().syncId);
    }

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String str = "<ipaaca:ipaacaevent xmlns:ipaaca=\""+IpaacaEventBehaviour.NAMESPACE+"\" "+TestUtil.getDefNS()+"id=\"a1\" " + extraAttributeString+">"
                + "<message category=\"cat1\">"
                + "</message>"
                + "</ipaaca:ipaacaevent>";       
                return new IpaacaEventBehaviour(bmlId, new XMLTokenizer(str));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new IpaacaEventBehaviour(bmlId,new XMLTokenizer(bmlString));
    }
}
