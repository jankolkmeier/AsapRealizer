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
 * Unit test cases for the NUSSpline3
 * @author hvanwelbergen
 *
 */
public class NUSSpline3Test
{
    private static final float PRECISION = 0.001f;
    
    @Test
    public void test()
    {
        NUSSpline3 spline = new NUSSpline3(3);
        List<float[]>data = new ArrayList<>();
        List<Double>times = new ArrayList<>();
        int points = 4;
        
        for(int i=0;i<points;i++)
        {
            data.add(Vec3f.getVec3f(i,i,i));
            times.add((double)i);
        }
        
        List<SparseVelocityDef> vel = new ArrayList<>();
        vel.add(new SparseVelocityDef(0, Vec3f.getVec3f(0,0,0)));
        spline.interpolate3(data,times, vel);
        
        //some test values obtained from C++ version
        Vec3fTestUtil.assertVec3fEquals(0.369998f,0.369998f,0.369998f,spline.getPosition(0.3),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(1.13333f,1.13333f,1.13333f,spline.getFirstDerivative(0.3),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(-0.666649f,-0.666649f,-0.666649f,spline.getSecondDerivative(0.3),PRECISION);
        
        Vec3fTestUtil.assertVec3fEquals(1.23f,1.23f,1.23f,spline.getPosition(1.3),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(0.86667f,0.86667f,0.86667f,spline.getFirstDerivative(1.3),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(0.666649f,0.666649f,0.666649f,spline.getSecondDerivative(1.3),PRECISION);
        
        Vec3fTestUtil.assertVec3fEquals(2.24f,2.24f,2.24f,spline.getPosition(2.2),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(1.19999f,1.19999f,1.199997f,spline.getFirstDerivative(2.2),PRECISION);
        Vec3fTestUtil.assertVec3fEquals(-0.666662f,-0.666662f,-0.666662f,spline.getSecondDerivative(2.2),PRECISION);
    }
}
