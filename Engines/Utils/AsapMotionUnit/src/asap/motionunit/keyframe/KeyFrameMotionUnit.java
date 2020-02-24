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

import java.util.List;

import lombok.Delegate;
import asap.motionunit.MUPlayException;
import asap.motionunit.MotionUnit;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;
import asap.timemanipulator.LinearManipulator;
import asap.timemanipulator.TimeManipulator;

/**
 * Generic implementation a motion motion unit that interpolates a set of keyframes
 * @author hvanwelbergen
 *
 */
public abstract class KeyFrameMotionUnit implements MotionUnit
{
    @Delegate private KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();
    private TimeManipulator manip = new LinearManipulator();
    
    private final Interpolator interpolator;
    private final boolean allowDynamicStart;
    
    public KeyFrameMotionUnit(Interpolator interp, TimeManipulator m, boolean allowDynamicStart)
    {
        this.allowDynamicStart = allowDynamicStart;
        this.manip = m;
        interpolator = interp;
    }
    
    public KeyFrameMotionUnit(Interpolator interp)
    {
        this(interp, new LinearManipulator(), true);
    }
    
    protected double unifyKeyFrames(List<KeyFrame> keyFrames)
    {
        double preferedDuration = keyFrames.get(keyFrames.size()-1).getFrameTime();
        
        for(KeyFrame kf:keyFrames)
        {
            kf.setFrameTime(kf.getFrameTime() / preferedDuration);
        }
        return preferedDuration;
    }
    
    protected void setupDynamicStart(List<KeyFrame> keyFrames)
    {
        if(allowDynamicStart)
        {
            if(keyFrames.size()>0) 
            {
                if(keyFrames.get(0).getFrameTime()>0)
                {
                    keyFrames.add(0,getStartKeyFrame());
                }
            }
            else
            {
                keyFrames.add(getStartKeyFrame());
            }            
        }
    }
    
    @Override
    public void play(double t) throws MUPlayException
    {
        KeyFrame kf = interpolator.interpolate(manip.manip(t));
        applyKeyFrame(kf);
    }

    public abstract void applyKeyFrame(KeyFrame kf);
    
    public abstract KeyFrame getStartKeyFrame();
    
    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterException
    {
        throw new ParameterNotFoundException(name);
    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {
        throw new ParameterNotFoundException(name);
    }

    @Override
    public String getParameterValue(String name) throws ParameterException
    {
        throw new ParameterNotFoundException(name);
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException
    {
        throw new ParameterNotFoundException(name);
    }
}
