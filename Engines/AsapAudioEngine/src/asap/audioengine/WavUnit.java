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
package asap.audioengine;

import asap.realizer.planunit.ParameterException;

/**
 * Interface for the playback of (wav) audio units
 * @author hvanwelbergen
 *
 */
public interface WavUnit
{
    void setParameterValue(String parameter, float value)throws ParameterException;
    void setParameterValue(String parameter, String value)throws ParameterException;
    String getParameterValue(String parameter)throws ParameterException;
    float getFloatParameterValue(String parameter)throws ParameterException;
    
    /**
     * @param relTime time relative to the start of the WavUnit
     */
    void start(double relTime);
    
    /**
     * Stops and cleans up the WavUnit
     */
    void stop();
    
    /**
     * Play
     * @param relTime relative to start of WavUnit
     */
    void play(double relTime) throws WavUnitPlayException;
    
    /**
     * Get the duration of the WavUnit in seconds
     * @return
     */
    double getDuration();
}
