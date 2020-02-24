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

import static asap.murml.testutil.MURMLTestUtil.createJointValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import hmi.xml.XMLScanException;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import asap.murml.Keyframing.Mode;

/**
 * Unit test cases for Keyframing
 * @author hvanwelbergen
 * 
 */
public class KeyframingTest
{
    private static final double PARAMETER_PRECISION = 0.0001;

    @Test
    public void testRead()
    {
        Keyframing kf = new Keyframing();
        kf.readXML("<keyframing xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" mode=\"spline\" priority=\"100\" easescale=\"10\">"
                + "<phase>"
                + "<frame ftime=\"0.1\"><posture>Humanoid (dB_Smile 3 70 0 0) (dB_OpenMouthWOOQ 3 0 0 0) "
                + "(dB_OpenMouthL 3 0 0 0) (dB_OpenMouthE 3 0 0 0)</posture>"
                + "</frame>"
                + "<frame ftime=\"0.2\"><posture>Humanoid (dB_Smile 3 80 0 0) (dB_OpenMouthWOOQ 3 1 0 0) "
                + "(dB_OpenMouthL 3 0 1 0) (dB_OpenMouthE 3 0 0 1)</posture>" + "</frame>" + "</phase></keyframing>");
        assertEquals(Mode.SPLINE, kf.getMode());
        assertEquals(100, kf.getPriority());
        assertEquals(10, kf.getEasescale(), PARAMETER_PRECISION);

        Phase ph = kf.getPhases().get(0);
        Frame f0 = ph.getFrames().get(0);
        assertThat(
                f0.getPosture().getJointValues(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(createJointValue("dB_Smile", 70, 0, 0),
                        createJointValue("dB_OpenMouthWOOQ", 0, 0, 0), createJointValue("dB_OpenMouthL", 0, 0, 0),
                        createJointValue("dB_OpenMouthE", 0, 0, 0)));
        assertEquals(0.1, f0.getFtime(), PARAMETER_PRECISION);

        Frame f1 = ph.getFrames().get(1);
        assertThat(
                f1.getPosture().getJointValues(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(createJointValue("dB_Smile", 80, 0, 0),
                        createJointValue("dB_OpenMouthWOOQ", 1, 0, 0), createJointValue("dB_OpenMouthL", 0, 1, 0),
                        createJointValue("dB_OpenMouthE", 0, 0, 1)));
        assertEquals(0.2, f1.getFtime(), PARAMETER_PRECISION);
    }
    
    @Test(timeout=200,expected=XMLScanException.class)
    public void testUnknown()
    {
        Keyframing kf = new Keyframing();
        kf.readXML("<keyframing xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" mode=\"spline\" priority=\"100\" easescale=\"10\">"
                +"<SkeletonInterpolator/>"
                +"</keyframing>");
        
    }
    
    @Test
    public void testWrite()
    {
        Keyframing kfIn = new Keyframing();
        kfIn.readXML("<keyframing xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" mode=\"spline\" priority=\"100\" easescale=\"10\">"
                + "<phase>"
                + "<frame ftime=\"0.1\"><posture>Humanoid (dB_Smile 3 70 0 0) (dB_OpenMouthWOOQ 3 0 0 0) "
                + "(dB_OpenMouthL 3 0 0 0) (dB_OpenMouthE 3 0 0 0)</posture>"
                + "</frame>"
                + "<frame ftime=\"0.2\"><posture>Humanoid (dB_Smile 3 80 0 0) (dB_OpenMouthWOOQ 3 1 0 0) "
                + "(dB_OpenMouthL 3 0 1 0) (dB_OpenMouthE 3 0 0 1)</posture>" + "</frame>" + "</phase></keyframing>");
        StringBuilder buf = new StringBuilder();
        kfIn.appendXML(buf);
        Keyframing kfOut = new Keyframing();
        kfOut.readXML(buf.toString());
        
        assertEquals(Mode.SPLINE, kfOut.getMode());
        assertEquals(100, kfOut.getPriority());
        assertEquals(10, kfOut.getEasescale(), PARAMETER_PRECISION);

        Phase ph = kfOut.getPhases().get(0);
        Frame f0 = ph.getFrames().get(0);
        assertThat(
                f0.getPosture().getJointValues(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(createJointValue("dB_Smile", 70, 0, 0),
                        createJointValue("dB_OpenMouthWOOQ", 0, 0, 0), createJointValue("dB_OpenMouthL", 0, 0, 0),
                        createJointValue("dB_OpenMouthE", 0, 0, 0)));
        assertEquals(0.1, f0.getFtime(), PARAMETER_PRECISION);

        Frame f1 = ph.getFrames().get(1);
        assertThat(
                f1.getPosture().getJointValues(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(createJointValue("dB_Smile", 80, 0, 0),
                        createJointValue("dB_OpenMouthWOOQ", 1, 0, 0), createJointValue("dB_OpenMouthL", 0, 1, 0),
                        createJointValue("dB_OpenMouthE", 0, 0, 1)));
        assertEquals(0.2, f1.getFtime(), PARAMETER_PRECISION);
    }
}
