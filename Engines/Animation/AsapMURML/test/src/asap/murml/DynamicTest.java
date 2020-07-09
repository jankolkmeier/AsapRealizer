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
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import asap.murml.Keyframing.Mode;

/**
 * Unit tests for Dynamic
 * @author hvanwelbergen
 *
 */
public class DynamicTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Test
    public void testReadDynamicWithKeyframe()
    {
        Dynamic dynamic = new Dynamic();
        dynamic.readXML("<dynamic xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\"><keyframing mode=\"spline\" priority=\"100\" easescale=\"10\">"
                + "<phase>"
                + "<frame ftime=\"0.1\"><posture>Humanoid (dB_Smile 3 70 0 0) (dB_OpenMouthWOOQ 3 0 0 0) "
                + "(dB_OpenMouthL 3 0 0 0) (dB_OpenMouthE 3 0 0 0)</posture>"
                + "</frame>"
                + "<frame ftime=\"0.2\"><posture>Humanoid (dB_Smile 3 80 0 0) (dB_OpenMouthWOOQ 3 1 0 0) "
                + "(dB_OpenMouthL 3 0 1 0) (dB_OpenMouthE 3 0 0 1)</posture>" + "</frame>" + "</phase></keyframing></dynamic>");
        assertNotNull(dynamic.getKeyframing());
        Keyframing kf = dynamic.getKeyframing();
        assertEquals(Mode.SPLINE, kf.getMode());
        assertEquals(100, kf.getPriority());
        assertEquals(10, kf.getEasescale(), PARAMETER_PRECISION);
    }
    
    @Test
    public void testWriteDynamicWithKeyframe()
    {
        Dynamic dynamicIn = new Dynamic();
        dynamicIn.readXML("<dynamic xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\"><keyframing mode=\"spline\" priority=\"100\" easescale=\"10\">"
                + "<phase>"
                + "<frame ftime=\"0.1\"><posture>Humanoid (dB_Smile 3 70 0 0) (dB_OpenMouthWOOQ 3 0 0 0) "
                + "(dB_OpenMouthL 3 0 0 0) (dB_OpenMouthE 3 0 0 0)</posture>"
                + "</frame>"
                + "<frame ftime=\"0.2\"><posture>Humanoid (dB_Smile 3 80 0 0) (dB_OpenMouthWOOQ 3 1 0 0) "
                + "(dB_OpenMouthL 3 0 1 0) (dB_OpenMouthE 3 0 0 1)</posture>" + "</frame>" + "</phase></keyframing></dynamic>");
        StringBuilder buf = new StringBuilder();
        dynamicIn.appendXML(buf);
        
        Dynamic dynamicOut = new Dynamic();
        dynamicOut.readXML(buf.toString());
        assertNotNull(dynamicOut.getKeyframing());
        Keyframing kf = dynamicOut.getKeyframing();
        assertEquals(Mode.SPLINE, kf.getMode());
        assertEquals(100, kf.getPriority());
        assertEquals(10, kf.getEasescale(), PARAMETER_PRECISION);        
    }
}
