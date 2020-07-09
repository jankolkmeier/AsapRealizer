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
package asap.realizer.scheduler;

import saiba.bml.core.BMLBlockComposition;
import saiba.bml.core.BehaviourBlock;
import asap.realizer.pegboard.BMLBlockPeg;

/**
 * Interface for strategies to schedule BML blocks (that is: resolve the time constraints and add the behaviors to the appropiate plans). 
 * @author welberge
 */
public interface SchedulingStrategy
{
    /**
     * Schedules the behaviour block. That is: resolve the time constraints and add timedplanunit implementations of all behaviours to 
     * the plans (using the engines through the scheduler).
     */
    void schedule(BMLBlockComposition mechanism, BehaviourBlock bb, BMLBlockPeg bmlBlockPeg, BMLScheduler scheduler, double schedulingTime);
}
