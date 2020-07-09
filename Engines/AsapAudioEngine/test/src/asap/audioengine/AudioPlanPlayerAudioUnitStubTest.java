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

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.feedback.BMLWarningFeedback;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.MultiThreadedPlanPlayer;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizerport.util.ListBMLFeedbackListener;

/**
 * Tests for the TimedAbstractAudioUnit through a stub
 * @author Herwin
 */
public class AudioPlanPlayerAudioUnitStubTest
{
    private PlanManager<TimedAbstractAudioUnit> planManager = new PlanManager<TimedAbstractAudioUnit>();
    private MultiThreadedPlanPlayer<TimedAbstractAudioUnit> app; 
    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);
    
    @Before
    public void setup()
    {
        app = new MultiThreadedPlanPlayer<TimedAbstractAudioUnit>(mockFeedbackManager,planManager);        
    }
    
    @Test
    public void testPlayAudioUnit() throws InterruptedException, TimedPlanUnitPlayException
    {
        List<BMLWarningFeedback> beList = new ArrayList<BMLWarningFeedback>();
        
        StubAudioUnit auStub = new StubAudioUnit(mockFeedbackManager,BMLBlockPeg.GLOBALPEG,null,"id1","bml1");
        
        auStub.setState(TimedPlanUnitState.LURKING);
        TimePeg tpStart = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tpStart.setGlobalValue(0);
        auStub.setStart(tpStart);
        
        app.addFeedbackListener(new ListBMLFeedbackListener.Builder().warningList(beList).build());        
        planManager.addPlanUnit(auStub);
        app.play(0);
        
        Thread.sleep(100);
        assertTrue(beList.size()==0);
        app.shutdown();
    }
}
