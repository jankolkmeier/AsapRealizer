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

import java.util.List;

import lombok.Delegate;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;

import com.google.common.collect.ImmutableList;

/**
 * Plays a sequence of motionunits. Currently does not do anything smart with key positions.
 * @author hvanwelbergen
 *
 */
public class MUSequence implements MotionUnit
{
    private ImmutableList<MotionUnit> mus;

    public MUSequence(List<MotionUnit> mus)
    {
        this.mus = ImmutableList.copyOf(mus);
    }
    
    @Delegate
    private KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();

    @Override
    public void play(double t) throws MUPlayException
    {
        double totalDuration = getPreferedDuration();
        double prev = 0;
        for (MotionUnit mu : mus)
        {
            double next = prev+mu.getPreferedDuration()/totalDuration;
            if(t>=prev && t<next)
            {
                double time = (t-prev);
                double dur = mu.getPreferedDuration()/totalDuration;
                mu.play(time/dur);
                return;
            }
            prev += mu.getPreferedDuration()/totalDuration;
        }
    }

    @Override
    public void startUnit(double t) throws MUPlayException
    {

    }

    @Override
    public double getPreferedDuration()
    {
        double duration = 0;
        for (MotionUnit mu : mus)
        {
            duration += mu.getPreferedDuration();
        }
        return duration;
    }

    @Override
    public void setFloatParameterValue(String name, float value)
    {
        for (MotionUnit mu : mus)
        {
            try
            {
                mu.setFloatParameterValue(name, value);
            }
            catch (ParameterException ex)
            {

            }
        }
    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {
        for (MotionUnit mu : mus)
        {
            try
            {
                mu.setParameterValue(name, value);
            }
            catch (ParameterException ex)
            {

            }
        }
    }

    @Override
    public String getParameterValue(String name) throws ParameterException
    {
        for (MotionUnit mu : mus)
        {
            
            try
            {
                String value = mu.getParameterValue(name);
                return value;
            }
            catch(ParameterException ex)
            {
                
            }
            
        }
        throw new ParameterException("No parameter "+name+" defined.");
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException
    {
        for (MotionUnit mu : mus)
        {
            
            try
            {
                float value = mu.getFloatParameterValue(name);
                return value;
            }
            catch(ParameterException ex)
            {
                
            }
            
        }
        throw new ParameterException("No parameter "+name+" defined.");
    }

}
