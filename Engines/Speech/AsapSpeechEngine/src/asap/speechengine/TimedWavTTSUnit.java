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
package asap.speechengine;

import static com.google.common.collect.Sets.newHashSet;
import hmi.audioenvironment.SoundManager;
import hmi.audioenvironment.WavCreationException;
import hmi.tts.Bookmark;
import hmi.tts.TTSException;
import hmi.tts.TTSTiming;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;
import saiba.bml.core.Behaviour;
import saiba.bml.core.SpeechBehaviour;
import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import asap.audioengine.WavUnit;
import asap.audioengine.WavUnitImpl;
import asap.audioengine.WavUnitPlayException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.speechengine.ttsbinding.TTSBinding;

/**
 * Plans speech by generating a .wav file, then uses that .wav file to generate the speech
 * 
 * @author welberge
 */
@Slf4j
public class TimedWavTTSUnit extends TimedTTSUnit
{
    protected WavUnit wavUnit;
    private final SoundManager soundManager;
    private final String voiceId;
    private File tempFile;

    @GuardedBy("this")
    private Set<Bookmark> progressHandled = newHashSet();

    public TimedWavTTSUnit(FeedbackManager bfm, SoundManager sm, BMLBlockPeg bbPeg, String text, String voiceId, String bmlId, String id,
            TTSBinding ttsBin, Class<? extends Behaviour> behClass)
    {
        super(bfm, bbPeg, text, bmlId, id, ttsBin, behClass);
        soundManager = sm;
        this.voiceId = voiceId;
    }

    public TimedWavTTSUnit(FeedbackManager bfm, SoundManager sm, BMLBlockPeg bbPeg, String text, String id, String voiceId, String bmlId,
            TTSBinding ttsBin)
    {
        super(bfm, bbPeg, text, bmlId, id, ttsBin, SpeechBehaviour.class);
        soundManager = sm;
        this.voiceId = voiceId;
    }

    @Override
    protected void sendProgress(double playTime, double time)
    {
        List<BMLSyncPointProgressFeedback> sppfs = new ArrayList<BMLSyncPointProgressFeedback>();
        synchronized (this)
        {
            for (Bookmark b : timing.getBookmarks())
            {
                if (playTime >= b.getOffset() / 1000d)
                {
                    if (!progressHandled.contains(b))
                    {
                        String behaviorId = getId();
                        String syncId = b.getName();
                        double bmlBlockTime = time - bmlBlockPeg.getValue();
                        sppfs.add(new BMLSyncPointProgressFeedback(getBMLId(), behaviorId, syncId, bmlBlockTime, time));
                        progressHandled.add(b);
                    }
                }
            }
        }
        feedback(sppfs);
    }

    @Override
    public void playUnit(double time) throws TimedPlanUnitPlayException
    {
        double playTime = time - getStartTime();
        synchronized (this)
        {
            // logger.debug("PlayTimedWavUnit {}:{})", getBMLId(), getId());            
            try
            {
                wavUnit.play(playTime);
            }
            catch (WavUnitPlayException e)
            {
                throw new TimedPlanUnitPlayException(e.getLocalizedMessage(), this, e);
            }

            // TODO hook into the callback system?
        }
        // Send bookmark progress
        sendProgress(playTime, time);
    }

    @Override
    public void stopUnit(double time)
    {
        log.debug("StopTimedWavUnit {}:{}", getBMLId(), getId());
        sendProgress(time - getStartTime(), time);
        sendEndProgress(time);
        
        synchronized (this)
        {
            log.debug("StopTimedWavUnit in sync block)");
            wavUnit.stop();
        }
    }

    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException
    {
        super.startUnit(time);
        sendStartProgress(time);
        bmlStartTime = time;
        log.debug("Starting TimedWavTTSUnit {}:{} Time: {}, relative startTime: {}", new Object[] { this.getBMLId(), this.getId(), time,
                time - getStartTime() });
        synchronized (this)
        {
            if (wavUnit != null)
            {
                wavUnit.start(time - getStartTime());
            }
            else
            {
                throw new TimedPlanUnitPlayException("null wavUnit", this);
            }
        }
    }

    @Override
    protected synchronized void setupCache() throws SpeechUnitPlanningException
    {
        try
        {
            try
            {
                wavUnit = new WavUnitImpl(soundManager.createWav(tempFile.toURI().toURL().openStream(), voiceId));
            }
            catch (WavCreationException e)
            {
                throw new SpeechUnitPlanningException(e.getLocalizedMessage(), this, e);
            }
        }
        catch (IOException ex)
        {
            throw new SpeechUnitPlanningException(ex.getLocalizedMessage(), this, ex);
        }
        finally
        {
            if (tempFile != null)
            {
                if (!tempFile.delete())
                {
                    log.warn("Couldn't delete temp file in WavTTSUnit: {}", tempFile.toString());
                }
            }
        }
    }

    @Override
    protected TTSTiming getTiming() throws SpeechUnitPlanningException
    {
        String fileName = getBMLId() + "-" + getId();
        synchronized (this)
        {
            TTSTiming ti = null;
            try
            {
                tempFile = File.createTempFile(fileName, ".wav");
            }
            catch (IOException e)
            {
                throw new SpeechUnitPlanningException(e.getLocalizedMessage(), this, e);
            }
            try
            {
                ti = ttsBinding.speakToFile(getBehaviourClass(), speechText, tempFile.getAbsolutePath());
            }
            catch (IOException | TTSException e)
            {
                throw new SpeechUnitPlanningException(e.getLocalizedMessage(), this, e);
            }
            progressHandled.clear();
            return ti;
        }
    }

    @Override
    public void setFloatParameterValue(String paramId, float value) throws ParameterException
    {
        if(isSubUnitFloatParameter(paramId))
        {
            storeSubUnitParameterValue(paramId,value);
            return;
        }
        log.debug("TimedWavTTSUnit Setting parameter {} value {}", paramId, value);
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
    public void setParameterValue(String paramId, String value) throws ParameterException
    {
        if(isSubUnitFloatParameter(paramId))
        {
            storeSubUnitParameterValue(paramId,value);
            return;
        }
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
    public float getFloatParameterValue(String paramId) throws ParameterException
    {
        try
        {
            return wavUnit.getFloatParameterValue(paramId);
        }
        catch (ParameterNotFoundException e)
        {
            return super.getFloatParameterValue(paramId);            
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
            return super.getParameterValue(paramId);
        }
    }
}
