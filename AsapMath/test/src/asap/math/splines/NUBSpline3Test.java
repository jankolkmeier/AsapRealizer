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
package asap.math.splines;

import hmi.math.Vec3f;
import hmi.testutil.math.Vec3fTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for the NUBSpline
 * @author hvanwelbergen
 */
public class NUBSpline3Test
{
    private static final float PRECISION = 0.001f;
    @Test
    public void test()
    {
        NUBSpline3 spline = new NUBSpline3(3);
        List<float[]>data = new ArrayList<>();
        List<float[]>vel = new ArrayList<>();
        List<Double>times = new ArrayList<>();
        int points = 4;
        
        for(int i=0;i<points;i++)
        {
            data.add(Vec3f.getVec3f(i,i,i));
            vel.add(Vec3f.getVec3f(0,0,0));
            times.add((double)i);
        }        
        spline.interpolate(data,times, vel);
        
        //some test values from C++ version
        Vec3fTestUtil.assertVec3fEquals(0.51f,0.51f,0.51f,spline.getPosition(0.3),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(1.04444f,1.04444f,1.04444f,spline.getPosition(1.3),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(2f,2f,2f,spline.getPosition(2.2),PRECISION);
    }
}
