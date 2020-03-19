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
package asap.faceengine;


import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import asap.faceengine.faceunit.FaceUnit;
import asap.faceengine.faceunit.TimedFaceUnit;
import asap.motionunit.MUPlayException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.feedback.FeedbackManagerImpl;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.SingleThreadedPlanPlayer;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizerport.util.ListBMLFeedbackListener;
import asap.realizertestutil.util.KeyPositionMocker;
import asap.testutil.bml.feedback.FeedbackAsserts;

/**
 * Unit test cases for the FacePlanPlayer
 * @author hvanwelbergen
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class FacePlanPlayerTest
{
    protected static final String CHARACTER_ID = "character1";
    
    private FaceUnit fuMock1 = mock(FaceUnit.class);
    private BMLBlockManager mockBmlBlockManager = mock(BMLBlockManager.class);
    
    private FeedbackManager fbManager = new FeedbackManagerImpl(mockBmlBlockManager, CHARACTER_ID);
    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);
    private PlanManager<TimedFaceUnit> planManager = new PlanManager<TimedFaceUnit>();
    private final PegBoard pegBoard = new PegBoard();

    @Before
    public void setup()
    {
    	when(mockBmlBlockManager.getCharacterId(anyString())).thenReturn(CHARACTER_ID);
    }
    
    @Test
    public void testPlayTfu() throws MUPlayException 
    {
        //Playing one face unit at t=0, checking for state transition, feedback calls, no warnings
        List<BMLSyncPointProgressFeedback> fbList = new ArrayList<BMLSyncPointProgressFeedback>();
        SingleThreadedPlanPlayer<TimedFaceUnit>fpp = new SingleThreadedPlanPlayer<TimedFaceUnit>(mockFeedbackManager, planManager);
        TimedFaceUnit tfu1 = new TimedFaceUnit(fbManager,BMLBlockPeg.GLOBALPEG, "bml1", "behaviour1", fuMock1, pegBoard);        
        fbManager.addFeedbackListener(new ListBMLFeedbackListener.Builder().feedBackList(fbList).build());
        planManager.addPlanUnit(tfu1);
        KeyPositionMocker.stubKeyPositions(fuMock1, new KeyPosition("start",0,1), new KeyPosition("end",1,1));
                
        TimePeg tpStart = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tpStart.setGlobalValue(0);
        tfu1.setTimePeg("start", tpStart);
        tfu1.setState(TimedPlanUnitState.LURKING);        
        fpp.play(0);
        assertEquals(TimedPlanUnitState.IN_EXEC,tfu1.getState());
        assertEquals(1, fbList.size());
        FeedbackAsserts.assertEqualSyncPointProgress(new BMLSyncPointProgressFeedback("bml1","behaviour1","start",0,0), fbList.get(0));
        verify(fuMock1,times(1)).play(0);        
    }
}
