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
package asap.ipaacaeventengine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitSetupException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;

/**
 * Unit tests for TimedIpaacaMessageUnit
 * @author hvanwelbergen
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BMLScheduler.class,BMLBlockManager.class})
public class TimedIpaacaMessageUnitTest extends AbstractTimedPlanUnitTest
{
    private MessageManager mockMessageManager = mock(MessageManager.class);
    @Override
    protected void assertSubsiding(TimedPlanUnit tpu)
    {
        assertEquals(TimedPlanUnitState.DONE, tpu.getState());
    }
    
    @Override
    @Test
    public void testSetStrokePeg() 
    {
        //XXX: remove from super?
    }

    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
            throws TimedPlanUnitSetupException
    {
        TimedIpaacaMessageUnit timu = new TimedIpaacaMessageUnit(bfm, bbPeg, bmlId, id, mockMessageManager, "cat1", new HashMap<String,String>());
        TimePeg start = new TimePeg(bbPeg);
        start.setGlobalValue(startTime);
        timu.setStartPeg(start);        
        return timu;        
    }
}
