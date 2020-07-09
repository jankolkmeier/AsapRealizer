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
package asap.livemocapengine.planunit;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedAbstractPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;

import com.google.common.collect.ImmutableList;

/**
 * A LivemocapTMU is a superclass for all planunits that read from some input, process it 
 * and write it to some output. Classes implementing the LivemocapTMU should implement playUnit
 * to do so. LivemocapTMU takes care of running tmu at the right time, sending feedback, etc.
 * @author welberge
 */
@Slf4j
public abstract class LiveMocapTMU extends TimedAbstractPlanUnit
{
    protected TimePeg startPeg;
    protected TimePeg endPeg;
    
    public LiveMocapTMU(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId)
    {
        super(fbm, bmlPeg, bmlId, behId);  
        endPeg = new TimePeg(bmlPeg);
        startPeg = new TimePeg(bmlPeg);
    }

    @Override
    public double getEndTime()
    {
        return endPeg.getGlobalValue();
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

    /**
     * @param startPeg the startPeg to set
     */
    public void setStartPeg(TimePeg startPeg)
    {
        this.startPeg = startPeg;
    }

    /**
     * @param endPeg the endPeg to set
     */
    public void setEndPeg(TimePeg endPeg)
    {
        this.endPeg = endPeg;
    }

    @Override
    public TimePeg getTimePeg(String syncId)
    {
        if (syncId.equals("start")) return startPeg;
        else if (syncId.equals("end")) return endPeg;
        return null;
    }

    @Override
    public boolean hasValidTiming()
    {
        if (endPeg.getGlobalValue() != TimePeg.VALUE_UNKNOWN && startPeg.getGlobalValue() != TimePeg.VALUE_UNKNOWN)
        {
            return endPeg.getGlobalValue() >= startPeg.getGlobalValue();
        }
        return true;
    }

    @Override
    public void setTimePeg(String syncId, TimePeg peg)
    {
        if (syncId.equals("start"))
        {
            startPeg = peg;
        }
        else if (syncId.equals("end"))
        {
            endPeg = peg;
        }
        else
        {
            log.warn("Can't set TimePeg for sync {}", syncId);
        }
    }

    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException
    {
        feedback("start", time);
    }

    @Override
    protected void stopUnit(double time) throws TimedPlanUnitPlayException
    {
        feedback("end", time);
    } 
    
    @Override
    public List<String> getAvailableSyncs()
    {
        return ImmutableList.of("start","end");
    }
}
