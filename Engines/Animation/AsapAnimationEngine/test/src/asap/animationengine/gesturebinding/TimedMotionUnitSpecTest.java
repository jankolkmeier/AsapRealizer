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
package asap.animationengine.gesturebinding;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for the TimedMotionUnitSpec
 * @author hvanwelbergen
 *
 */
public class TimedMotionUnitSpecTest
{
    @Test
    public void test()
    {
        //@formatter:off
        String xmlString=
        "<TimedMotionUnitSpec type=\"gesture\">"+
        "<constraints>"+
        "  <constraint name=\"lexeme\" value=\"wave\"/>"+
        "</constraints>"+
        "<parametermap>"+
        "</parametermap>"+
        "<parameterdefaults>"+
        "</parameterdefaults>"+
        "<TimedMotionUnit type=\"MURML\">"+
        "<test> </test>"+
        "</TimedMotionUnit>"+
        "</TimedMotionUnitSpec>";
        //@formatter:on
        TimedMotionUnitSpec spec = new TimedMotionUnitSpec();
        spec.readXML(xmlString);
        assertEquals("gesture",spec.getType());
        assertEquals("MURML",spec.getTimedMotionUnitConstructionInfo().getType());
        assertEquals("<test> </test>",spec.getTimedMotionUnitConstructionInfo().getContent());
        
    }
}
