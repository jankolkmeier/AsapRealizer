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
package asap.livemocapengine.planunit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import hmi.headandgazeembodiments.EulerHeadEmbodiment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.livemocapengine.inputs.EulerInput;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Unit tests for the RemoteHeadTMU
 * @author welberge
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class RemoteHeadTMUTest extends AbstractTimedPlanUnitTest
{
    private EulerInput mockEulerInput = mock(EulerInput.class);
    private EulerHeadEmbodiment mockHead = mock(EulerHeadEmbodiment.class);

    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        RemoteHeadTMU tmu = new RemoteHeadTMU(mockEulerInput, mockHead, bfm, bbPeg, bmlId, id);
        TimePeg start = new TimePeg(bbPeg);
        start.setGlobalValue(startTime);
        tmu.setStartPeg(start);
        return tmu;
    }

    @Override
    @Test
    public void testSetStrokePeg()
    {
        // XXX: remove from super?
    }

    @Override
    @Test
    public void testSubsiding() throws TimedPlanUnitPlayException
    {

    }

    @Test
    public void testPlay() throws TimedPlanUnitPlayException
    {
        TimedPlanUnit tpu = setupPlanUnit(fbManager, BMLBlockPeg.GLOBALPEG, "rh1", "bml1", 0);
        tpu.setTimePeg("end", TimePegUtil.createTimePeg(2));
        when(mockEulerInput.getRollDegrees()).thenReturn(10f);        
        when(mockEulerInput.getPitchDegrees()).thenReturn(20f);
        when(mockEulerInput.getYawDegrees()).thenReturn(30f);
        tpu.setState(TimedPlanUnitState.LURKING);
        tpu.start(0);
        tpu.play(0);
        verify(mockHead).setHeadRollPitchYawDegrees(10f, 20f, 30f);
    }
}
