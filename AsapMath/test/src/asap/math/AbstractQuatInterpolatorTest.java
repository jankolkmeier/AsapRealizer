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
package asap.math;

import hmi.math.Quat4f;
import hmi.testutil.math.Quat4fTestUtil;

import org.junit.Test;

public abstract class AbstractQuatInterpolatorTest
{
    protected abstract QuatInterpolator getInterpolator(double p[][]);
    
    protected static final float INTERPOLATION_PRECISION = 0.001f;
    protected float q[] = Quat4f.getQuat4f();
    protected double pval[][]={{0,1,0,0,0},{1,0,0,0,1},{2,0,1,0,0}};
    
    @Test
    public void testInterpolateStart()
    {
        QuatInterpolator in = getInterpolator(pval);        
        in.interpolate(0,q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(Quat4f.getQuat4f(1, 0, 0, 0), q, INTERPOLATION_PRECISION);
    }
    
    @Test
    public void testInterpolateEnd()
    {
        QuatInterpolator in = getInterpolator(pval);
        in.interpolate(2,q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(Quat4f.getQuat4f(0, 1, 0, 0), q, INTERPOLATION_PRECISION);
    }
    
    @Test
    public void testInterpolateMiddle()
    {
        QuatInterpolator in = getInterpolator(pval);
        in.interpolate(1,q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(Quat4f.getQuat4f(0, 0, 0, 1), q, INTERPOLATION_PRECISION);
    }
    
    
    
    @Test
    public void testInterpolateOneValue()
    {
        double pval[][]={{0,1,0,0,0}};
        QuatInterpolator in = getInterpolator(pval);
        in.interpolate(0.5,q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(Quat4f.getQuat4f(1,0,0,0), q, INTERPOLATION_PRECISION);
    }
}
