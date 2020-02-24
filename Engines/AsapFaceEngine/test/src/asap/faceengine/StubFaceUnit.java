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
package asap.faceengine;

import hmi.faceanimation.FaceController;
import hmi.faceanimation.converters.EmotionConverter;
import hmi.faceanimation.converters.FACS2MorphConverter;
import hmi.faceanimation.converters.FACSConverter;
import lombok.Delegate;
import asap.faceengine.faceunit.FaceUnit;
import asap.faceengine.faceunit.TimedFaceUnit;
import asap.motionunit.MUPlayException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;

/**
 * Testing stub for FaceUnit
 * @author hvanwelbergen
 *
 */
public class StubFaceUnit implements FaceUnit
{
    @Delegate private final KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();
        
    @Override
    public void play(double t) throws MUPlayException
    {
                
    }

    @Override
    public void startUnit(double t) throws MUPlayException
    {
                
    }
   

    @Override
    public double getPreferedDuration()
    {
        return 3;
    }

    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterException
    {
                
    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {
                
    }

    @Override
    public String getParameterValue(String name) throws ParameterException
    {
        return null;
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException
    {
        return 0;
    }

    @Override
    public boolean hasValidParameters()
    {
        return true;
    }

    @Override
    public TimedFaceUnit createTFU(FeedbackManager bfm, BMLBlockPeg bbPeg, String bmlId, String id, PegBoard pb)
    {
        return new TimedFaceUnit(bfm, bbPeg, bmlId, id, this, pb);
    }

    @Override
    public FaceUnit copy(FaceController fc, FACSConverter fconv, EmotionConverter econv, FACS2MorphConverter f2mconv)
    {
        return this;
    }

    @Override
    public void interruptFromHere()
    {
                
    }
}
