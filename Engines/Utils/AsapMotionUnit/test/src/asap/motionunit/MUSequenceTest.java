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
package asap.motionunit;

import static org.mockito.Matchers.doubleThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.number.IsCloseTo;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the MUSequence
 * @author hvanwelbergen
 * 
 */
public class MUSequenceTest
{
    private MotionUnit mockMU1 = mock(MotionUnit.class);
    private MotionUnit mockMU2 = mock(MotionUnit.class);
    private MotionUnit mockMU3 = mock(MotionUnit.class);
    private MUSequence muSequence;
    private static final double MU1_DURATION = 1;
    private static final double MU2_DURATION = 2;
    private static final double MU3_DURATION = 3;
    private static final double EPSILON = 0.001;
    @Before
    public void setup()
    {
        when(mockMU1.getPreferedDuration()).thenReturn(MU1_DURATION);
        when(mockMU2.getPreferedDuration()).thenReturn(MU2_DURATION);
        when(mockMU3.getPreferedDuration()).thenReturn(MU3_DURATION);
        muSequence = new MUSequence(ImmutableList.of(mockMU1, mockMU2, mockMU3));
    }

    @Test
    public void testPlay0() throws MUPlayException
    {
        muSequence.play(0);
        verify(mockMU1).play(0);
    }
    
    @Test
    public void testPlay1() throws MUPlayException
    {
        muSequence.play(0.9999);
        verify(mockMU3).play(doubleThat(IsCloseTo.closeTo(1,EPSILON)));
    }
    
    @Test
    public void testPlay0_5() throws MUPlayException
    {
        muSequence.play(0.5);
        verify(mockMU3).play(0);
    }
}
