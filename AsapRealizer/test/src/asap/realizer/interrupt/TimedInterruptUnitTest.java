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
package asap.realizer.interrupt;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitSetupException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;

import com.google.common.collect.ImmutableSet;

/**
 * Test cases for the TimedInterruptUnit
 * @author welberge
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BMLScheduler.class, BMLBlockManager.class })
public class TimedInterruptUnitTest extends AbstractTimedPlanUnitTest
{
    private BMLScheduler mockScheduler = mock(BMLScheduler.class);
    private static final String TARGET = "bml2";

    @Override
    protected void assertSubsiding(TimedPlanUnit tpu)
    {
        assertEquals(TimedPlanUnitState.DONE, tpu.getState());
    }

    @Override
    protected TimedInterruptUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        TimedInterruptUnit tiu = new TimedInterruptUnit(bfm, bbPeg, bmlId, id, TARGET, mockScheduler);
        when(mockScheduler.getBehaviours(TARGET)).thenReturn(ImmutableSet.of("beh1", "beh2"));
        when(mockScheduler.getSyncsPassed(TARGET, "beh1")).thenReturn(new ImmutableSet.Builder<String>().build());

        TimePeg start = new TimePeg(bbPeg);
        start.setGlobalValue(startTime);
        tiu.setStartPeg(start);

        return tiu;
    }

    @Test
    public void testInterruptBlock() throws TimedPlanUnitPlayException, TimedPlanUnitSetupException
    {
        TimedPlanUnit tpu = setupPlanUnitWithListener(BMLBlockPeg.GLOBALPEG, "id1", "bml1", 0);
        tpu.setState(TimedPlanUnitState.LURKING);
        tpu.start(0);
        tpu.play(0);
        verify(mockScheduler, times(1)).interruptBlock(TARGET, 0);
    }

    @Test
    public void testInterruptBehavior() throws TimedPlanUnitPlayException, TimedPlanUnitSetupException
    {
        TimedInterruptUnit tpu = (TimedInterruptUnit) setupPlanUnitWithListener(BMLBlockPeg.GLOBALPEG, "id1", "bml1", 0);
        tpu.setInclude(ImmutableSet.of("beh1"));
        tpu.setState(TimedPlanUnitState.LURKING);
        tpu.start(0);
        tpu.play(0);
        verify(mockScheduler, times(1)).interruptBehavior(TARGET, "beh1", 0);
    }

    @Test
    public void testInterruptBehaviorAll() throws TimedPlanUnitPlayException, TimedPlanUnitSetupException
    {
        TimedInterruptUnit tpu = (TimedInterruptUnit) setupPlanUnitWithListener(BMLBlockPeg.GLOBALPEG, "id1", "bml1", 0);
        tpu.setInclude(ImmutableSet.of("beh1", "beh2"));
        tpu.setState(TimedPlanUnitState.LURKING);
        tpu.start(0);
        tpu.play(0);
        verify(mockScheduler, times(1)).interruptBlock(TARGET, 0);
    }

    @Override
    @Test
    public void testSetStrokePeg()
    {
        // XXX: remove from super?
    }
}
