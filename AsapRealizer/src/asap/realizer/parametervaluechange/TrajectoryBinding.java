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

import com.google.common.collect.ImmutableMap;

/**
 * Binds a trajectory type name to a new trajectory instance
 * @author Herwin
 */
public class TrajectoryBinding
{
    private static final ImmutableMap<String, Class<? extends ParameterValueTrajectory>> TRAJECTORY_BINDING =
        new ImmutableMap.Builder<String, Class<? extends ParameterValueTrajectory>>()
        .put("linear", LinearTrajectory.class)
        .put("instant",InstantChangeTrajectory.class)
        .build();
    
    public ParameterValueTrajectory getTrajectory(String type) throws TrajectoryBindingException
    {
        if(TRAJECTORY_BINDING.get(type)!=null) try
        {
            return TRAJECTORY_BINDING.get(type).newInstance();
        }
        catch (InstantiationException e)
        {
            TrajectoryBindingException ex = new TrajectoryBindingException(type);
            ex.initCause(e);
            throw ex;
        }
        catch (IllegalAccessException e)
        {
            TrajectoryBindingException ex = new TrajectoryBindingException(type);
            ex.initCause(e);
            throw ex;
        }
        throw new TrajectoryBindingException(type);
    }
}
