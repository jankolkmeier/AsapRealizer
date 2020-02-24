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
package asap.bml.msapi;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import asap.bml.ext.msapi.MSApiBehaviour;

/**
 * Unit test cases for the MSApiBehaviour
 * @author welberge
 */
public class MSApiBehaviourTest
{
    @Test
    public void testReadXML() throws IOException
    {
        String str = "<sapi>Hello world!</sapi>";
        MSApiBehaviour beh = new MSApiBehaviour("bml1",new XMLTokenizer(str));
        assertEquals("Hello world!",beh.getContent().trim());
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String str = "<sapi>Hello world!</sapi>";
        MSApiBehaviour behIn = new MSApiBehaviour("bml1",new XMLTokenizer(str));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf);
        MSApiBehaviour behOut = new MSApiBehaviour("bml1",new XMLTokenizer(buf.toString()));
        assertEquals("Hello world!",behOut.getContent().trim());        
    }
}
