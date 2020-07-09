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



import hmi.audioenvironment.SoundManager;
import hmi.audioenvironment.WavCreationException;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;
import asap.realizer.planunit.TimedPlanUnitPlayException;

import com.google.common.collect.ImmutableList;

/**
 * Plans audio by caching a .wav file, then playing it
 */
public class TimedWavAudioUnit extends TimedAbstractAudioUnit
{
    private static Logger logger = LoggerFactory
            .getLogger(TimedWavAudioUnit.class.getName());
    protected WavUnit wavUnit;
    private final SoundManager soundManager;
    
    public TimedWavAudioUnit(SoundManager soundManager,FeedbackManager bfm, BMLBlockPeg bbPeg, InputStream inputStream,
            String bmlId, String id)
    {
        super(bfm,bbPeg, inputStream, bmlId, id);
        this.soundManager = soundManager;
    }

    @Override
    public void sendProgress(double playTime, double time)
    {

    }

    @Override
    public void playUnit(double time) throws TimedPlanUnitPlayException
    {
        double playTime = time - getStartTime();
        try
        {
            wavUnit.play(playTime);
        }
        catch (WavUnitPlayException e)
        {
            throw new TimedAudioUnitPlayException(e.getLocalizedMessage(), this, e);            
        }

        // Send bookmark progress
        sendProgress(playTime, time);
    }

    @Override
    public void stopUnit(double time)
    {
        sendEndProgress(time);
        if (wavUnit != null)
        {
            wavUnit.stop();
        }
    }

    @Override
    public void cleanup()
    {
        if (wavUnit != null)
        {
            wavUnit.stop();
        }
    }
    
    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException
    {
        bmlStartTime = time;
        systemStartTime = System.nanoTime() / 1E9;        
        if (wavUnit != null)
        {
            wavUnit.start(time - getStartTime());
        }
        else
        {
            throw new TimedAudioUnitPlayException("Null wavUnit unit", this);
        }
        sendStartProgress(time);
    }

    /**
     * load file, determine timing/duration, etc
     * 
     * @throws AudioUnitPlanningException
     */
    @Override
    protected void setupCache() throws AudioUnitPlanningException
    {
        try
        {
            wavUnit = new WavUnitImpl(soundManager.createWav(inputStream));
        }
        catch (WavCreationException e)
        {
            throw new AudioUnitPlanningException(e.getLocalizedMessage(), this, e);            
        }
        setPrefferedDuration(wavUnit.getDuration());
        logger.debug("WAVUNIT DURATION: {}", wavUnit.getDuration());
    }

    @Override
    public void setFloatParameterValue(String paramId, float value)
            throws ParameterException
    {
        logger.debug("Setting wav parameter {}, value: {}", paramId, value);
        try
        {
            wavUnit.setParameterValue(paramId, value);
        }
        catch (ParameterNotFoundException e)
        {
            throw wrapIntoPlanUnitFloatParameterNotFoundException(e);
        }
    }

    @Override
    public void setParameterValue(String paramId, String value)
            throws ParameterException
    {
        try
        {
            wavUnit.setParameterValue(paramId, value);
        }
        catch (ParameterNotFoundException e)
        {
            throw wrapIntoPlanUnitParameterNotFoundException(e);
        }
    }

    @Override
    public float getFloatParameterValue(String paramId)
            throws ParameterException
    {
        try
        {
            return wavUnit.getFloatParameterValue(paramId);
        }
        catch (ParameterNotFoundException e)
        {
            throw wrapIntoPlanUnitFloatParameterNotFoundException(e);
        }
    }

    @Override
    public String getParameterValue(String paramId) throws ParameterException
    {
        try
        {
            return wavUnit.getParameterValue(paramId);
        }
        catch (ParameterNotFoundException e)
        {
            throw wrapIntoPlanUnitParameterNotFoundException(e);
        }
    }

    @Override
    public List<String> getAvailableSyncs()
    {
        return ImmutableList.of("start","end");
    }
}
