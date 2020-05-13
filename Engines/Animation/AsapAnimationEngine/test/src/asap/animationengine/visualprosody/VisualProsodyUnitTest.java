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
package asap.animationengine.visualprosody;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.animationengine.AnimationPlayer;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitSetupException;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;
import asap.realizertestutil.util.TimePegUtil;
import asap.visualprosody.VisualProsody;

/**
 * Unit test cases for transition timed motion units
 * @author Herwin
 * 
 */
@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
        "org.slf4j.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BMLScheduler.class, BMLBlockManager.class })
public class VisualProsodyUnitTest extends AbstractTimedPlanUnitTest
{
    private TimedPlanUnit mockSpeechUnit = mock(TimedPlanUnit.class);
    private AnimationPlayer mockAnimationPlayer = mock(AnimationPlayer.class);
    private VisualProsody mockVisualProsody = mock(VisualProsody.class);

    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
            throws TimedPlanUnitSetupException
    {
        when(mockVisualProsody.firstHeadMotion((double[]) any(), anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(
                new double[3]);
        when(mockVisualProsody.getOffset()).thenReturn(new float[3]);
        when(mockVisualProsody.nextHeadMotion((double[]) any(), (double[]) any(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(new double[3]);

        VisualProsodyUnit vp = new VisualProsodyUnit(bfm, bbPeg, bmlId, id, mockSpeechUnit, mockVisualProsody, mockAnimationPlayer,
                new double[10], new double[10], 0.01, TimePegUtil.createTimePeg(bbPeg, startTime), TimePegUtil.createTimePeg(bbPeg,
                        startTime + 2));
        return vp;
    }

    @Override
    @Test
    public void testSetStrokePeg() throws TimedPlanUnitSetupException
    {

    }
}
