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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.junit.Test;

/**
 * Test cases for the trajectory binding
 * @author welberge
 */
public class TrajectoryBindingTest
{
    @Test
    public void testGetTrajectory() throws TrajectoryBindingException
    {
        TrajectoryBinding binding = new TrajectoryBinding();
        ParameterValueTrajectory t = binding.getTrajectory("linear");
        assertThat(t, instanceOf(LinearTrajectory.class));
    }
    
    @Test (expected=TrajectoryBindingException.class)
    public void testGetInvalidTrajectory() throws TrajectoryBindingException
    {
        TrajectoryBinding binding = new TrajectoryBinding();
        ParameterValueTrajectory t = binding.getTrajectory("invalid");
        assertThat(t, instanceOf(LinearTrajectory.class));
    }
}
