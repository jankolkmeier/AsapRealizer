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
package asap.audioengine;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.audioenvironment.SoundManager;

import java.io.InputStream;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;

/**
 * Unit test cases for the TimedWavAudioUnit
 * @author welberge
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class TimedWavAudioUnitTest extends AbstractTimedPlanUnitTest
{
    InputStream mockInputStream = mock(InputStream.class);
    WavUnit mockWavUnit = mock(WavUnit.class);
    SoundManager mockSoundManager = mock(SoundManager.class);

    @Override //behavior does not subside
    public void testSubsiding()
    {
        
    }
    
    @Override
    // XXX no stroke on this behavior
    public void testSetStrokePeg()
    {

    }

    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        TimedWavAudioUnit twa = new TimedWavAudioUnit(mockSoundManager, bfm, bbPeg, mockInputStream, bmlId, id);
        TimePeg start = new TimePeg(bbPeg);
        start.setGlobalValue(startTime);
        twa.setTimePeg("start", start);

        // XXX a bit of a hack, simulates the setupCache
        twa.wavUnit = mockWavUnit;
        when(mockWavUnit.getDuration()).thenReturn(10d);
        twa.setPrefferedDuration(10d);

        return twa;
    }
}
