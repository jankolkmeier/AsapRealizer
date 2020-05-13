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
package asap.textengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedAbstractPlanUnit;

/**
 * Skeleton implementation for TimedTextUnits.
 * Keeps track of text.
 * Provides convenience methods for start and end feedback sending.
 * @author welberge
 */
public abstract class TimedAbstractTextUnit extends TimedAbstractPlanUnit
{
    private TimePeg startSync;
    private TimePeg endSync;
    
    protected String speechText;   
    protected double bmlStartTime;
    
    private static Logger logger = LoggerFactory.getLogger(TimedAbstractTextUnit.class.getName());
    
    TimedAbstractTextUnit(FeedbackManager bfm,BMLBlockPeg bbPeg, String text, String bmlId, String id)
    {
        super(bfm,bbPeg,bmlId,id);
        speechText = text;        
    }
    
    @Override
    public double getStartTime()
    {
        if(startSync == null)
        {
            return TimePeg.VALUE_UNKNOWN;
        }
        return startSync.getGlobalValue();
    }

    @Override
    public double getEndTime()
    {
        double endTime;
        if(endSync==null)
        {
            endTime = TimePeg.VALUE_UNKNOWN; 
        }
        else
        {
            endTime = endSync.getGlobalValue();
        }
        if (endTime == TimePeg.VALUE_UNKNOWN)
        {
            double startTime = getStartTime();
            if (startTime != TimePeg.VALUE_UNKNOWN)
            {
                return startTime + getPreferedDuration();
            }
        }
        return endTime;
    }
    
    @Override
    public double getRelaxTime()
    {
        return getEndTime();
    }

    public TimePeg getEndPeg()
    {
        return endSync;
    }

    public TimePeg getStartPeg()
    {
        return startSync;
    }
    
    public void setStart(TimePeg s)
    {
        startSync = s;
    }

    public void setEnd(TimePeg s)
    {
        endSync = s;
    }
    
    protected void sendStartProgress(double time)
    {
        logger.debug("Sending start progress feedback.");
        String bmlId = getBMLId();
        String behaviorId = getId();
        
        double bmlBlockTime = time - bmlBlockPeg.getValue();
        feedback(new BMLSyncPointProgressFeedback(bmlId,behaviorId,"start",bmlBlockTime,time));
    }
    
    /**
     * Checks wether the TimedPlanUnit has sync sync
     */
    public boolean hasSync(String sync)
    {
        for(String s:getAvailableSyncs())
        {
            if(s.equals(sync))return true;
        }
        return false;
    }
    /**
     * Send the end progress feedback info, should be called only from the VerbalPlanPlayer.
     * @param time time since start of BML execution 
     */
    public void sendEndProgress(double time)
    {
        String bmlId = getBMLId();
        String behaviorId = getId();        
        double bmlBlockTime = time - bmlBlockPeg.getValue();
        feedback(new BMLSyncPointProgressFeedback(bmlId,behaviorId,"end",bmlBlockTime,time));
    }
}
