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
package asap.speechengine;

import hmi.util.OS;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.core.SpeechBehaviour;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizertestutil.util.TimePegUtil;
import asap.sapittsbinding.SAPITTSBinding;

/**
 * Integration tests for the sapi direct TTS.
 * @author Herwin
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
@Ignore("There are some problems with (probably) fluency voices when accessed through SAPI")
public class DirectSapiTTSUnitIntegrationTest extends AbstractTTSUnitTest
{
    private SAPITTSBinding sapiBinding;
    @Before
    public void setup() throws SpeechUnitPlanningException
    {
        Assume.assumeTrue(OS.equalsOS(OS.WINDOWS));
        sapiBinding = new SAPITTSBinding();
    }
    
    @After
    public void cleanup()
    {
        if(sapiBinding!=null)
        {
            sapiBinding.cleanup();
        }
    }
    
    protected TimedTTSUnit getTTSUnit(BMLBlockPeg bbPeg, String text, String id, String bmlId)
    {
        return new TimedDirectTTSUnit(fbManager, bbPeg, text, bmlId, id, sapiBinding, SpeechBehaviour.class);
    }

    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        TimedDirectTTSUnit ttsU = new TimedDirectTTSUnit(bfm, bbPeg, "hello world", bmlId, id, sapiBinding, SpeechBehaviour.class);
        ttsU.setTimePeg("start", TimePegUtil.createTimePeg(bbPeg, startTime));
        return ttsU;
    }    
}
