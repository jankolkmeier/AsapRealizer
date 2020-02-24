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

import org.junit.Test;

/**
 * Unit tests for the IpaacaMessage
 * @author hvanwelbergen
 *
 */
public class IpaacaMessageTest
{
    @Test
    public void test()
    {
        String xml="<message category=\"cat1\">"
                + "<payload>"
                + "<item key=\"key1\" value=\"val1\"/>"
                + "<item key=\"key2\" value=\"val2\"/>"
                + "</payload>"
                + "</message>";
        IpaacaMessage message = new IpaacaMessage();
        message.readXML(xml);
        assertEquals("cat1", message.getCategory());
        assertEquals("val1", message.getPayload().get("key1"));
        assertEquals("val2", message.getPayload().get("key2"));
        assertEquals("default", message.getChannel());
    }
    
    @Test
    public void testChannel()
    {
        String xml="<message channel=\"ch1\" category=\"cat1\">"
                + "<payload>"
                + "<item key=\"key1\" value=\"val1\"/>"
                + "<item key=\"key2\" value=\"val2\"/>"
                + "</payload>"
                + "</message>";
        IpaacaMessage message = new IpaacaMessage();
        message.readXML(xml);
        assertEquals("cat1", message.getCategory());
        assertEquals("val1", message.getPayload().get("key1"));
        assertEquals("val2", message.getPayload().get("key2"));
        assertEquals("ch1", message.getChannel());
    }
}
