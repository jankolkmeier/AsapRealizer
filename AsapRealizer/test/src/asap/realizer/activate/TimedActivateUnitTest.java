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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;

import com.google.common.collect.ImmutableSet;

/**
 * Unit test cases for the TimedActivateUnit
 * @author welberge
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({BMLScheduler.class,BMLBlockManager.class})
public class TimedActivateUnitTest extends AbstractTimedPlanUnitTest
{
    private BMLScheduler mockScheduler = mock(BMLScheduler.class);
    private static final String TARGET="bml2";
    
    @Override
    protected void assertSubsiding(TimedPlanUnit tpu)
    {
        assertEquals(TimedPlanUnitState.DONE, tpu.getState());
    }
    
    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        TimedActivateUnit tau = new TimedActivateUnit(bfm, bbPeg, bmlId, id, TARGET, mockScheduler);
        HashSet<String> behaviours = new HashSet<String>();
        behaviours.add("beh1");
        when(mockScheduler.getBehaviours(TARGET)).thenReturn(behaviours);
        when(mockScheduler.getSyncsPassed(TARGET, "beh1")).thenReturn(new ImmutableSet.Builder<String>().build());
        
        TimePeg start = new TimePeg(bbPeg);
        start.setGlobalValue(startTime);
        tau.setStartPeg(start);        
        return tau;
    }
    
    @Override
    @Test
    public void testSetStrokePeg() 
    {
        //XXX: remove from super?
    }   
    
    @Test
    public void testActivate() throws TimedPlanUnitPlayException
    {
        TimedPlanUnit tpu = setupPlanUnit(fbManager,BMLBlockPeg.GLOBALPEG,"a1","bml1",1);
        tpu.setState(TimedPlanUnitState.LURKING);
        tpu.start(1.1);
        verify(mockScheduler,times(1)).activateBlock(TARGET, 1.1);
    }
}
