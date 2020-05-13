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
package asap.realizer.wait;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saiba.bml.BMLGestureSync;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedAbstractPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;

import com.google.common.collect.ImmutableList;

/**
 * TimedPlanUnit realizations of the BML &lt;wait&gt; behavior.
 * Simply runs as a no-op behavior and sends the appropriate feedback.
 * @author Herwin
 * 
 */
public class TimedWaitUnit extends TimedAbstractPlanUnit
{
    private TimePeg startPeg;
    private TimePeg endPeg;
    private static final Logger logger = LoggerFactory.getLogger(TimedWaitUnit.class.getName());

    protected TimedWaitUnit(FeedbackManager bfm, BMLBlockPeg bmlPeg, String bmlId, String id)
    {
        super(bfm, bmlPeg, bmlId, id);
    }

    public void setStartPeg(TimePeg startPeg)
    {
        this.startPeg = startPeg;
    }

    public void setEndPeg(TimePeg endPeg)
    {
        this.endPeg = endPeg;
    }

    @Override
    public double getStartTime()
    {
        return startPeg.getGlobalValue();
    }

    @Override
    public double getEndTime()
    {
        if (endPeg == null)
        {
            return TimePeg.VALUE_UNKNOWN;
        }
        return endPeg.getGlobalValue();
    }

    @Override
    public double getRelaxTime()
    {
        return getEndTime();
    }
    
    @Override
    public boolean hasValidTiming()
    {
        if (startPeg == null)
        {
            return false;
        }
        if (startPeg.getGlobalValue() == TimePeg.VALUE_UNKNOWN)
        {
            return false;
        }
        if (endPeg == null)
        {
            return true;
        }
        if (endPeg.getGlobalValue() == TimePeg.VALUE_UNKNOWN)
        {
            return true;
        }
        return (startPeg.getGlobalValue() < endPeg.getGlobalValue());
    }

    @Override
    public void startUnit(double time)
    {
        feedback("start", time);        
    }

    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {

    }

    @Override
    protected void stopUnit(double time) throws TimedPlanUnitPlayException
    {
        feedback("end", time);
    }

    @Override
    public double getPreferedDuration()
    {
        if (getEndTime() != TimePeg.VALUE_UNKNOWN && getStartTime() != TimePeg.VALUE_UNKNOWN)
        {
            return getEndTime() - getStartTime();
        }
        return 0;
    }

    @Override
    public TimePeg getTimePeg(String syncId)
    {
        if (syncId.equals("start")) return startPeg;
        if (syncId.equals("end")) return endPeg;
        return null;
    }
    
    @Override
    public List<String> getAvailableSyncs()
    {
        return ImmutableList.of("start","end");
    }

    @Override
    public void setTimePeg(String syncId, TimePeg peg)
    {
        if (BMLGestureSync.isBMLSync(syncId))
        {
            if (BMLGestureSync.get(syncId).isAfter(BMLGestureSync.STROKE))
            {
                setEndPeg(peg);
            }
            else
            {
                setStartPeg(peg);
            }
        }
        else
        {
            logger.warn("Can't set TimePeg on non-BML sync {}", syncId);
        }
    }

}
