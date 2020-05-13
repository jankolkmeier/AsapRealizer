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
package asap.motionunit.keyframe;

import static org.junit.Assert.assertEquals;
import hmi.math.Quat4f;
import hmi.testutil.math.Quat4fTestUtil;

import org.junit.Test;

import asap.math.LinearQuatInterpolator;

/**
 * Unit tests for the LinearQuatFloatInterpolator
 * @author hvanwelbergen
 *
 */
public class LinearQuatFloatInterpolatorTest extends AbstractQuatFloatInterpolatorTest<LinearQuatInterpolator>
{

    @Override
    protected QuatFloatInterpolator<LinearQuatInterpolator> getInterpolator()
    {
        return new LinearQuatFloatInterpolator();
    }
    
    @Test
    public void testInterpolate01()
    {
        KeyFrame kf = interp.interpolate(0.1);
        assertEquals(0.1, kf.getFrameTime(), FRAMETIME_PRECISION);
        float q0[] = Quat4f.getQuat4f();
        float q1[] = Quat4f.getQuat4f();
        float q2[] = Quat4f.getQuat4f();
        Quat4f.interpolate(q0, Quat4f.getQuat4f(0, 1, 0, 0), Quat4f.getQuat4f(1, 0, 0, 0), 0.5f);
        Quat4f.interpolate(q1, Quat4f.getQuat4f(1, 0, 0, 0), Quat4f.getQuat4f(0, 1, 0, 0), 0.5f);
        Quat4f.interpolate(q2, Quat4f.getQuat4f(0, 0, 0, 1), Quat4f.getQuat4f(0, 0, 1, 0), 0.5f);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(q0, 0, kf.getDofs(), 0, INTERPOLATION_PRECISION);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(q1, 0, kf.getDofs(), 4, INTERPOLATION_PRECISION);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(q2, 0, kf.getDofs(), 8, INTERPOLATION_PRECISION);
    }

}
