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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.hamcrest.number.OrderingComparison.lessThan;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Floats;
/**
 * Unit test cases for LinearFloatInterpolator
 * @author hvanwelbergen
 *
 */
public class CubicSplineFloatInterpolatorTest
{
    private CubicSplineFloatInterpolator interp = new CubicSplineFloatInterpolator();
    private static final double TIME_PRECISION = 0.0001;
    @Before
    public void setup()
    {
        KeyFrame kf0 = new KeyFrame(0,new float[]{1,2,3});
        KeyFrame kf1 = new KeyFrame(0.2,new float[]{2,3,4});
        KeyFrame kf2 = new KeyFrame(0.8,new float[]{3,4,5});
        KeyFrame kf3 = new KeyFrame(1,new float[]{4,5,6});
        interp.setKeyFrames(ImmutableList.of(kf0,kf1,kf2,kf3), 3);        
    }
    
    @Test
    public void testInterpolateToStart()
    {
        KeyFrame kf = interp.interpolate(0);
        assertEquals(0, kf.getFrameTime(),TIME_PRECISION);
        assertThat(Floats.asList(kf.getDofs()), contains(1f,2f,3f));
    }
    
    @Test
    public void testInterpolateToEnd()
    {
        KeyFrame kf = interp.interpolate(1);
        assertEquals(1, kf.getFrameTime(),TIME_PRECISION);
        assertThat(Floats.asList(kf.getDofs()), contains(4f,5f,6f));
    }
    
    @Test
    public void testInterpolate02()
    {
        KeyFrame kf = interp.interpolate(0.2);
        assertEquals(0.2, kf.getFrameTime(),TIME_PRECISION);
        assertThat(Floats.asList(kf.getDofs()), contains(2f,3f,4f));
    }
    
    @Test
    public void testInterpolate01()
    {
        KeyFrame kf = interp.interpolate(0.1);
        assertEquals(0.1, kf.getFrameTime(),TIME_PRECISION);
        assertThat(kf.getDofs()[0],greaterThan(1f));
        assertThat(kf.getDofs()[0],lessThan(2f));
        assertThat(kf.getDofs()[1],greaterThan(2f));
        assertThat(kf.getDofs()[1],lessThan(3f));
        assertThat(kf.getDofs()[2],greaterThan(3f));
        assertThat(kf.getDofs()[2],lessThan(4f));        
    }
}
