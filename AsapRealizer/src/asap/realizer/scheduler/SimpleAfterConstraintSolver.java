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

import saiba.bml.core.BehaviourBlock;
import saiba.bml.parser.AfterConstraint;

/**
 * Fast solver that can satifisfy after constraints in most BML scripts.
 * @author hvanwelbergen
 */
public class SimpleAfterConstraintSolver
{
    private boolean afterConstraintsSatisfied(BehaviourBlock bb, BMLScheduler scheduler)
    {
        for (AfterConstraint ac : scheduler.getParser().getAfterConstraints())
        {

        }
        return true;
    }

    public void scheduleAfterConstraints(BehaviourBlock bb, BMLScheduler scheduler)
    {
        if (afterConstraintsSatisfied(bb, scheduler))
        {
            return;
        }
    }
}
