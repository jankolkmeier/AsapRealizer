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
package asap.binding;

import static org.junit.Assert.assertEquals;
import hmi.xml.XMLScanException;

import org.junit.Test;

/**
 * Unit test cases for the SpecParameterDefaults
 * @author Herwin
 * 
 */
public class SpecParameterDefaultsTest
{
    private static final int READ_TIMEOUT = 200;

    @Test(timeout = READ_TIMEOUT, expected = XMLScanException.class)
    public void testInvalidContent()
    {
        String str = "<parameterdefaults><invalid/></parameterdefaults>";
        SpecParameterDefaults fb = new SpecParameterDefaults();
        fb.readXML(str);
    }

    @Test
    public void test()
    {
        String str = "<parameterdefaults><parameterdefault name=\"namex\" value=\"valx\"/>" +
        		"<parameterdefault name=\"namey\" value=\"valy\"/></parameterdefaults>";
        SpecParameterDefaults fb = new SpecParameterDefaults();
        fb.readXML(str);
        assertEquals(2,fb.getParameterDefaults().size());
    }
}
