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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.feedback.FeedbackManagerImpl;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizerport.util.ListBMLFeedbackListener;

/**
 * Test cases for TimedAbstractAudioUnit, executed by making a stub out of it and spying on some internal method calls
 * @author welberge
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class AudioUnitTest
{
    protected static final String CHARACTER_ID = "character1";
    
    private BMLBlockManager mockBmlBlockManager = mock(BMLBlockManager.class);
    private FeedbackManager fbManager = new FeedbackManagerImpl(mockBmlBlockManager,CHARACTER_ID);
    
    private ListBMLFeedbackListener feedbackListener;
    private List<BMLSyncPointProgressFeedback> feedbackList;
    
    @Before
    public void setup()
    {
        feedbackList = new ArrayList<BMLSyncPointProgressFeedback>(); 
        feedbackListener = new ListBMLFeedbackListener.Builder().feedBackList(feedbackList).build();    
    	when(mockBmlBlockManager.getCharacterId(anyString())).thenReturn(CHARACTER_ID);    
    }
    
    @Test
    public void testPrepState() throws TimedPlanUnitPlayException
    {
        StubAudioUnit stubAU = spy(new StubAudioUnit(fbManager,BMLBlockPeg.GLOBALPEG, null, "beh1","bml1"));
                
        fbManager.addFeedbackListener(feedbackListener);    
        
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tp.setGlobalValue(0);
        stubAU.setStart(tp);
        
        stubAU.start(1);
        stubAU.play(1);
        assertTrue(feedbackList.isEmpty());
        verify(stubAU,times(0)).playUnit(anyDouble());
        verify(stubAU,times(0)).startUnit(anyDouble());
    }
    
    @Test 
    public void testConstruct()
    {
        StubAudioUnit stubAU = new StubAudioUnit(fbManager,BMLBlockPeg.GLOBALPEG, null, "beh1","bml1");
        assertEquals(stubAU.getBMLId(),"bml1");
        assertEquals(stubAU.getId(),"beh1");
    }
    
    @Test 
    public void testGetEndTime()
    {
        StubAudioUnit stubAU = new StubAudioUnit(fbManager,BMLBlockPeg.GLOBALPEG, null, "beh1","bml1");
        assertEquals(TimePeg.VALUE_UNKNOWN,stubAU.getEndTime(),0.0001);
    }
    
    @Test
    public void testPlay() throws TimedPlanUnitPlayException
    {
        StubAudioUnit stubAU = spy(new StubAudioUnit(fbManager,BMLBlockPeg.GLOBALPEG, null, "beh1","bml1"));
        fbManager.addFeedbackListener(feedbackListener);    
        
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tp.setGlobalValue(0);
        stubAU.setStart(tp);
        
        stubAU.setState(TimedPlanUnitState.LURKING);
        stubAU.start(1);
        assertEquals(TimedPlanUnitState.IN_EXEC,stubAU.getState());        
        stubAU.play(1);
        assertTrue(feedbackList.isEmpty());        
        verify(stubAU,times(1)).playUnit(anyDouble());
        verify(stubAU,times(1)).startUnit(anyDouble());
    }
}
