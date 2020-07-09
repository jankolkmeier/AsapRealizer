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


import java.util.Collection;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import asap.realizer.feedback.FeedbackManager;

import com.google.common.collect.ImmutableCollection;

/**
 * Skeleton implemenation of the TimedPlanUnitPlayer
 * @author hvanwelbergen
 *
 */
@Slf4j
public abstract class AbstractTimedPlanUnitPlayer implements TimedPlanUnitPlayer
{
    protected List<TimedPlanUnitPlayException> playExceptions;
    protected List<TimedPlanUnitPlayException> stopExceptions;
    
    @Override
    public void handleStopExceptions(double t)
    {
        ImmutableCollection<TimedPlanUnitPlayException> exceptions = getStopExceptions();
        for (TimedPlanUnitPlayException tmuEx : exceptions)
        {
            log.warn("Exception stopping behaviour: ", tmuEx);            
        }
        clearStopExceptions(exceptions);
    }
    
    @Override
    public void handlePlayExceptions(double t, FeedbackManager fbManager)
    {
        ImmutableCollection<TimedPlanUnitPlayException> exceptions = getPlayExceptions();
        for (TimedPlanUnitPlayException tmuEx : exceptions)
        {
            TimedPlanUnit tmuR = tmuEx.getPlanUnit();
            fbManager.puException(tmuR,
                    "Runtime exception for behavior " + tmuR.getBMLId() + ":" + tmuR.getId() + ":" + tmuEx.getLocalizedMessage() + ".", t);
            stopUnit(tmuR, t);            
        }
        clearPlayExceptions(exceptions);
    }
    
    @Override
    public void clearStopExceptions(Collection<TimedPlanUnitPlayException> removeExceptions)
    {
        stopExceptions.removeAll(removeExceptions);        
    }
    
    @Override
    public void clearPlayExceptions(Collection<TimedPlanUnitPlayException> removeExceptions)
    {
        playExceptions.removeAll(removeExceptions);
    }
}
