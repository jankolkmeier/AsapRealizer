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
package asap.murml;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import asap.murml.DynamicElement.Type;

/**
 * Unit tests for the dynamicElement
 * @author hvanwelbergen
 * 
 */
public class DynamicElementTest
{
    @Test
    public void testRead()
    {
        // @formatter:off
        String dynElem = 
                "<dynamicElement xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" type=\"via\">"+
                "<value id=\"strokeStart\" name=\"val1\"/>"+
                "<value id=\"stroke1\" name=\"val2\"/>"+
                "<value id=\"stroke2\" name=\"val3\"/>"+
                "<value id=\"strokeEnd\" name=\"val4\"/>"+
                "</dynamicElement>";
        // @formatter:on
        DynamicElement elem = new DynamicElement();
        elem.readXML(dynElem);
        assertEquals(Type.VIA, elem.getType());
        assertEquals("val1", elem.getName("strokeStart"));
        assertEquals("val2", elem.getName("stroke1"));
        assertEquals("val3", elem.getName("stroke2"));
        assertEquals("val4", elem.getName("strokeEnd"));
    }
    
    @Test
    public void testWrite()
    {
     // @formatter:off
        String dynElem = 
                "<dynamicElement xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" type=\"via\">"+
                "<value id=\"strokeStart\" name=\"val1\"/>"+
                "<value id=\"stroke1\" name=\"val2\"/>"+
                "<value id=\"stroke2\" name=\"val3\"/>"+
                "<value id=\"strokeEnd\" name=\"val4\"/>"+
                "</dynamicElement>";
        // @formatter:on
        DynamicElement elemIn = new DynamicElement();
        elemIn.readXML(dynElem);
        StringBuilder buf = new StringBuilder();
        elemIn.appendXML(buf);
        DynamicElement elemOut = new DynamicElement();
        elemOut.readXML(buf.toString());
        
        assertEquals(Type.VIA, elemOut.getType());
        assertEquals("val1", elemOut.getName("strokeStart"));
        assertEquals("val2", elemOut.getName("stroke1"));
        assertEquals("val3", elemOut.getName("stroke2"));
        assertEquals("val4", elemOut.getName("strokeEnd"));
    }
}
