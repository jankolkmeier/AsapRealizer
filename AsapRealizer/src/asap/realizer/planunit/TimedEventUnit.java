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
package asap.realizer.planunit;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;

import com.google.common.collect.ImmutableList;

/**
 * TimePlanUnit that only has a start and is then finished instantly 
 * @author herwinvw
 */
@Slf4j
public abstract class TimedEventUnit extends TimedAbstractPlanUnit
{
    public TimedEventUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId)
    {
        super(fbm, bmlPeg, bmlId, behId);        
    }

    private TimePeg startPeg;
    
    /**
     * @param startPeg the startPeg to set
     */
    public void setStartPeg(TimePeg startPeg)
    {
        this.startPeg = startPeg;
        log.debug("Setting start peg to {}",startPeg);
    }
    
    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {
        stop(time);
    }
    
    @Override
    protected void stopUnit(double time) throws TimedPlanUnitPlayException
    {
        log.debug("stopping interrupt unit {} {}",getBMLId(),getId());        
        feedback("end",time);        
    }
    
    @Override
    public double getEndTime()
    {
        return TimePeg.VALUE_UNKNOWN;
    }

    @Override
    public double getRelaxTime()
    {
        return getEndTime();
    }
    
    @Override
    public double getStartTime()
    {
        return startPeg.getGlobalValue();
    }
    
    @Override
    public boolean hasValidTiming()
    {
        return true;
    }

    @Override
    public TimePeg getTimePeg(String syncId)
    {
        if(syncId.equals("start"))return startPeg;
        return null;
    }    
    
    @Override
    public void setTimePeg(String syncId, TimePeg peg)
    {
        if(syncId.equals("start"))
        {
            startPeg = peg;            
        }
        else
        {
            log.warn("Can't set TimePeg for sync {}, only setting start is allowed",syncId);            
        }
    }    
    
    @Override
    public List<String> getAvailableSyncs()
    {
        return ImmutableList.of("start","end");
    }
}
