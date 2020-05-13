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
package asap.realizer.parametervaluechange;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test cases for the LinearTrajectory trajectory
 * @author hvanwelbergen
 */
public class LinearTrajectoryTest
{
    private LinearTrajectory trajectory = new LinearTrajectory();
    private static final float START_VALUE = 20;
    private static final float END_VALUE = 100;
    private static final float INTERPOLATION_PRECISION = 0.0001f;
    
    @Test
    public void testStart()
    {
        assertEquals(START_VALUE, trajectory.getValue(START_VALUE, END_VALUE, 0), INTERPOLATION_PRECISION);
    }

    @Test
    public void testEnd()
    {
        assertEquals(END_VALUE, trajectory.getValue(START_VALUE, END_VALUE, 1), INTERPOLATION_PRECISION);
    }

    @Test
    public void testMiddle()
    {
        assertEquals(START_VALUE+(END_VALUE-START_VALUE)/2, trajectory.getValue(START_VALUE, END_VALUE, 0.5f), INTERPOLATION_PRECISION);        
    }
}
