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
package asap.visualprosody;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hmi.math.Vec3f;
import hmi.testutil.math.Vec3fTestUtil;

import org.junit.Test;

public class TestVisualProsodyLoader
{
    private static final double PRECISION=0.0001;
    
    private String xml="<visualprosodyprovider offset=\"1 2 3\">"+
            "<roll>"+
            "<gmm k=\"2\">\r\n" + 
            "  <lambdas>\r\n" + 
            "    <lambda val=\"0.1\"/>\r\n" + 
            "    <lambda val=\"0.9\"/>\r\n" + 
            "  </lambdas>\r\n" + 
            "  <mus>\r\n" + 
            "    <mu val=\"1 1 1\"/>\r\n" + 
            "    <mu val=\"1 1 1\"/>\r\n" + 
            "  </mus>\r\n" + 
            "  <sigmas>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "  </sigmas>\r\n" + 
            "</gmm>"+
            "</roll>"+
            "<pitch>"+
            "<gmm k=\"2\">\r\n" + 
            "  <lambdas>\r\n" + 
            "    <lambda val=\"0.2\"/>\r\n" + 
            "    <lambda val=\"0.8\"/>\r\n" + 
            "  </lambdas>\r\n" + 
            "  <mus>\r\n" + 
            "    <mu val=\"2 2 2\"/>\r\n" + 
            "    <mu val=\"2 2 2\"/>\r\n" + 
            "  </mus>\r\n" + 
            "  <sigmas>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "  </sigmas>\r\n" + 
            "</gmm>"+
            "</pitch>"+
            "<yaw>"+
            "<gmm k=\"2\">\r\n" + 
            "  <lambdas>\r\n" + 
            "    <lambda val=\"0.3\"/>\r\n" + 
            "    <lambda val=\"0.7\"/>\r\n" + 
            "  </lambdas>\r\n" + 
            "  <mus>\r\n" + 
            "    <mu val=\"3 3 3\"/>\r\n" + 
            "    <mu val=\"3 3 3\"/>\r\n" + 
            "  </mus>\r\n" + 
            "  <sigmas>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "  </sigmas>\r\n" + 
            "</gmm>"+
            "</yaw>"+
            "<v>"+
            "<gmm k=\"2\">\r\n" + 
            "  <lambdas>\r\n" + 
            "    <lambda val=\"0.4\"/>\r\n" + 
            "    <lambda val=\"0.6\"/>\r\n" + 
            "  </lambdas>\r\n" + 
            "  <mus>\r\n" + 
            "    <mu val=\"4 4 4\"/>\r\n" + 
            "    <mu val=\"4 4 4\"/>\r\n" + 
            "  </mus>\r\n" + 
            "  <sigmas>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "  </sigmas>\r\n" + 
            "</gmm>"+
            "</v>"+
            "<a>"+
            "<gmm k=\"2\">\r\n" + 
            "  <lambdas>\r\n" + 
            "    <lambda val=\"0.5\"/>\r\n" + 
            "    <lambda val=\"0.5\"/>\r\n" + 
            "  </lambdas>\r\n" + 
            "  <mus>\r\n" + 
            "    <mu val=\"5 5 5\"/>\r\n" + 
            "    <mu val=\"5 5 5\"/>\r\n" + 
            "  </mus>\r\n" + 
            "  <sigmas>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "    <sigma val=\"1 0 0 0 1 0 0 0 1\"/>\r\n" + 
            "  </sigmas>\r\n" + 
            "</gmm>"+
            "</a>"+
            "</visualprosodyprovider>";
    private VisualProsodyLoader vpl = new VisualProsodyLoader();
    
    @Test
    public void testRead()
    {
        
        vpl.readXML(xml);
        assertEquals(1,vpl.getRoll().getMu()[0],PRECISION);
        assertEquals(2,vpl.getPitch().getMu()[0],PRECISION);
        assertEquals(3,vpl.getYaw().getMu()[0],PRECISION);
        assertEquals(4,vpl.getV().getMu()[0],PRECISION);
        assertEquals(5,vpl.getA().getMu()[0],PRECISION);
        Vec3fTestUtil.assertVec3fEquals(Vec3f.getVec3f(1,2,3),vpl.getOffset(), (float)PRECISION);        
    }
    
    @Test
    public void testConstructVisualProsodyProvider()
    {
        vpl.readXML(xml);
        VisualProsody vpp = vpl.constructProsodyProvider();
        assertNotNull(vpp.headMotion(new double[]{0,0,0}, new AudioFeatures(new double[]{0,0}, new double[]{0,0}, 2)));
    }
}
