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
package asap.animationengine.transitions;

import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.motionunit.MUPlayException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;
import asap.realizertestutil.util.KeyPositionMocker;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Unit test cases for transition timed motion units
 * @author Herwin
 * 
 */
@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
        "org.slf4j.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BMLScheduler.class, BMLBlockManager.class })
public class TransitionTMUTest extends AbstractTimedPlanUnitTest
{
    TransitionMU mockTransitionMU = mock(TransitionMU.class);
    FeedbackManager mockBmlFeedbackManager = mock(FeedbackManager.class);
    private PegBoard pegBoard = new PegBoard();
    private AnimationPlayer mockAnimationPlayer = mock(AnimationPlayer.class);
    
    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        TimedAnimationMotionUnit tmu = new TimedAnimationMotionUnit(bfm, bbPeg, bmlId, id, mockTransitionMU, pegBoard, mockAnimationPlayer);
        KeyPositionMocker.stubKeyPositions(mockTransitionMU, new KeyPosition("start", 0, 1), new KeyPosition("ready", 0, 1),
                new KeyPosition("strokeStart", 0, 1), new KeyPosition("stroke", 0.5, 1), new KeyPosition("strokeEnd", 1, 1),
                new KeyPosition("relax", 1, 1), new KeyPosition("end", 1, 1));
        tmu.resolveGestureKeyPositions();
        tmu.setTimePeg("start", TimePegUtil.createTimePeg(bbPeg, startTime));
        return tmu;
    }

    @Test
    public void testExecStates() throws TimedPlanUnitPlayException, MUPlayException
    {
        TimedAnimationMotionUnit tmu = new TimedAnimationMotionUnit(mockBmlFeedbackManager, BMLBlockPeg.GLOBALPEG, "bml1", "behaviour1",
                mockTransitionMU, pegBoard, mockAnimationPlayer);
        TimePeg tpStart = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tpStart.setGlobalValue(0);
        TimePeg tpEnd = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tpEnd.setGlobalValue(1);

        final KeyPosition start = new KeyPosition("start", 0, 1);
        final KeyPosition end = new KeyPosition("end", 1, 1);
        when(mockTransitionMU.getKeyPosition("start")).thenReturn(start);
        when(mockTransitionMU.getKeyPosition("end")).thenReturn(end);

        tmu.setState(TimedPlanUnitState.LURKING);
        tmu.start(0.5);
        tmu.setTimePeg("start", tpStart);
        tmu.setTimePeg("end", tpEnd);
        tmu.setState(TimedPlanUnitState.IN_EXEC);
        tmu.play(0.5);

        verify(mockTransitionMU, atLeastOnce()).getKeyPosition("start");
        verify(mockTransitionMU, atLeastOnce()).getKeyPosition("end");
        verify(mockTransitionMU, atLeastOnce()).startUnit(eq(0.5, 0.01));
        verify(mockTransitionMU, times(1)).play(eq(0.5, 0.01));
    }
}
