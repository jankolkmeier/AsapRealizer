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
package asap.realizer.activate;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedEventUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.scheduler.BMLScheduler;

/**
 * Implementation of the bmla activate behavior.
 * Activates preplanned bml blocks 
 * @author welberge
 */
public class TimedActivateUnit extends TimedEventUnit
{
    private final BMLScheduler scheduler;
    private final String target;
    private static final Logger logger = LoggerFactory.getLogger(TimedActivateUnit.class.getName());
    
    public TimedActivateUnit(FeedbackManager bfm, BMLBlockPeg bmlPeg, String bmlId, String id, String target, BMLScheduler s)
    {
        super(bfm,bmlPeg, bmlId, id);    
        scheduler = s;
        this.target = target;
        logger.debug("Created activate unit {} {} {}",new String[]{getBMLId(),getId(),target});
    }
    
    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException
    {
        logger.debug("starting activate unit {} {}",getBMLId(),getId());
        scheduler.activateBlock(target,time);
        feedback("start",time);              
    }
}
