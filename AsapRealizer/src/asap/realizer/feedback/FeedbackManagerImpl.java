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
package asap.realizer.feedback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.jcip.annotations.GuardedBy;
import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import saiba.bml.feedback.BMLWarningFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockProgressFeedback;
import asap.bml.ext.bmla.feedback.BMLAPredictionFeedback;
import asap.bml.ext.bmla.feedback.BMLASyncPointProgressFeedback;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizerport.BMLFeedbackListener;

import com.google.common.collect.ImmutableSet;

/**
 * Default implementation for the FeedbackManager
 * @author Herwin
 *
 */
@Slf4j
public class FeedbackManagerImpl implements FeedbackManager
{
    private final BMLBlockManager bmlBlockManager;
    private final String defaultCharacterId;

    @GuardedBy("feedbackListeners")
    private final List<BMLFeedbackListener> feedbackListeners = Collections.synchronizedList(new ArrayList<BMLFeedbackListener>());


    public FeedbackManagerImpl(BMLBlockManager bbm, String vhId)
    {
        bmlBlockManager = bbm;
        defaultCharacterId = vhId;
    }

    @Override
    public void addFeedbackListener(BMLFeedbackListener fb)
    {
        feedbackListeners.add(fb);
    }

    
    @Override
    public void puException(TimedPlanUnit timedMU, String message, double time)
    {
        String bmlId = timedMU.getBMLId();        
        String exceptionText = message + "\nBehavior " + timedMU.getBMLId() + ":" + timedMU.getId() + " dropped.";
        warn(new BMLWarningFeedback(bmlId+":"+timedMU.getId(), "EXECUTION_FAILURE",exceptionText), time);
    }
    
    private BMLASyncPointProgressFeedback constructBMLASyncPointProgressFeedback(BMLSyncPointProgressFeedback fb)
    {
    	String characterId = bmlBlockManager.getCharacterId(fb.getBMLId());
    	if (characterId.isEmpty()) {
    		characterId = defaultCharacterId;
    	}
    	
        BMLASyncPointProgressFeedback fba = BMLASyncPointProgressFeedback.build(fb);        
        fba.setCharacterId(characterId);
        fba.setPosixTime(System.currentTimeMillis());
        return fba;
    }
    
    @Override
    public void feedback(BMLSyncPointProgressFeedback fb)
    {
        BMLASyncPointProgressFeedback fba = constructBMLASyncPointProgressFeedback(fb);
        
        synchronized (feedbackListeners)
        {
            for (BMLFeedbackListener fbl : feedbackListeners)
            {
                try
                {
                    fbl.feedback(fba.toBMLFeedbackString());
                }
                catch (Exception ex)
                {
                    log.warn("Exception in FeedbackListener: {}, feedback: {}", ex, fb.toBMLFeedbackString());
                }
            }
        }
        bmlBlockManager.syncProgress(fb);
    }

    @Override
    public void feedback(List<BMLSyncPointProgressFeedback> fbs)
    {
        synchronized (feedbackListeners)
        {
            for (BMLSyncPointProgressFeedback fb : fbs)
            {
            	
                BMLASyncPointProgressFeedback fba = constructBMLASyncPointProgressFeedback(fb);
                
                for (BMLFeedbackListener fbl : feedbackListeners)
                {
                    try
                    {
                        fbl.feedback(fba.toBMLFeedbackString());
                    }
                    catch (Exception ex)
                    {
                        log.warn("Exception in FeedbackListener: {}, feedback: {}", ex, fb.toBMLFeedbackString());
                    }
                }
            }
        }
        for (BMLSyncPointProgressFeedback fb : fbs)
        {
            bmlBlockManager.syncProgress(fb);
        }
    }

    @Override
    public void removeAllFeedbackListeners()
    {
        feedbackListeners.clear();
    }
    
    @Override
    public ImmutableSet<String> getSyncsPassed(String bmlId, String behaviorId)
    {
        return bmlBlockManager.getSyncsPassed(bmlId, behaviorId);
    }

    @Override
    public void removeFeedbackListener(BMLFeedbackListener fb)
    {
        feedbackListeners.remove(fb);
    }
    
    @Override
    public void blockProgress(BMLABlockProgressFeedback psf)
    {	
    	String characterId = bmlBlockManager.getCharacterId(psf.getBmlId());
    	if (characterId.isEmpty()) {
    		characterId = defaultCharacterId;
    	}
    	
        psf.setCharacterId(characterId);
        synchronized (feedbackListeners)
        {
            for (BMLFeedbackListener fbl : feedbackListeners)
            {
                try
                {
                    fbl.feedback(psf.toBMLFeedbackString());
                }
                catch (Exception ex)
                {
                    log.warn("Exception in FeedbackListener: {}, feedback: {}", ex, psf);
                }
            }
        }
        bmlBlockManager.blockProgress(psf);
    }

    
    @Override
    public void prediction(BMLAPredictionFeedback bpf)
    {
        synchronized (feedbackListeners)
        {
            String feedbackString = bpf.toBMLFeedbackString();
            for (BMLFeedbackListener pl : feedbackListeners)
            {
                try
                {                    
                    pl.feedback(feedbackString);                    
                }
                catch (Exception ex)
                {
                    log.warn("Exception in FeedbackListener: {}, feedback: {}", ex, feedbackString);
                }
            }
        }        
    }    
    
    @Override
    public void warn(BMLWarningFeedback w, double time)
    {
        synchronized (feedbackListeners)
        {
            for (BMLFeedbackListener wl : feedbackListeners)
            {
                try
                {
                    wl.feedback(w.toBMLFeedbackString());
                }
                catch (Exception ex)
                {
                    log.warn("Exception in WarningListener: {}, feedback: {}", ex, w.toBMLFeedbackString());
                }
            }
        }
        bmlBlockManager.warn(w, time);
    }
    
}
