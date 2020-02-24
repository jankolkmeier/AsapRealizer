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

import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.ParameterException;

/**
 * A unit of playable motion 
 * @author hvanwelbergen
 *
 */
public interface MotionUnit extends KeyPositionManager
{
    /**
     * Executes the motion unit, typically by rotating some VJoints
     * @param t execution time, 0 &lt; t &lt; 1
     * @throws MUPlayException if the play fails for some reason
     */
    void play(double t)throws MUPlayException;    
    
    /**
     * Prepares the motion unit for start     
     */
    void startUnit(double t)throws MUPlayException;
    
    /**
     * clears any resources used by the MotionUnit
     */
    default void cleanup()
    {
        
    }
    
    /**
     * @return Prefered duration (in seconds) of this face unit, 0 means not determined/infinite 
     */
    double getPreferedDuration();
        
    void setFloatParameterValue(String name, float value)throws ParameterException;;
    void setParameterValue(String name, String value)throws ParameterException;;
    String getParameterValue(String name)throws ParameterException;
    float getFloatParameterValue(String name)throws ParameterException;
   
}
