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

import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.core.BehaviourBlock;
import saiba.bml.core.CoreComposition;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
/**
 * Unit tests for the SmartBodySchedulingStrategy
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLScheduler.class)
public class SmartBodySchedulingStrategyTest
{
    private SmartBodySchedulingStrategy schedulingStrategy = new SmartBodySchedulingStrategy(new PegBoard());
    private BMLBlockPeg bbPeg = new BMLBlockPeg("bml1",0);
    BMLScheduler mockScheduler = mock(BMLScheduler.class);
    
    @Test
    public void testScheduleEmpty()
    {
        BehaviourBlock bb = new BehaviourBlock();
        bb.id = "bml1";
        schedulingStrategy.schedule(CoreComposition.MERGE, bb, bbPeg, mockScheduler, 0);        
    }
}
