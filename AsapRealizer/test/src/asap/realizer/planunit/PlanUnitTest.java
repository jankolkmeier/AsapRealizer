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
package asap.realizer.planunit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizertestutil.planunit.StubPlanUnit;

/**
 * Unit test case for AbstractPlanUnit
 * @author Herwin
 *
 */
public class PlanUnitTest
{
    private FeedbackManager mockBmlFeedbackManager = mock(FeedbackManager.class);
    
    private StubPlanUnit createStubPlanUnit(String behId,String bmlId)
    {
        return new StubPlanUnit(mockBmlFeedbackManager,BMLBlockPeg.GLOBALPEG,behId,bmlId);
    }
    
    @Test
    public void testSetup()
    {
        TimedAbstractPlanUnit apu = spy(createStubPlanUnit("id1", "bml1"));
        apu.setState(TimedPlanUnitState.LURKING);
        when(apu.getEndTime()).thenReturn(2d);
        assertTrue(apu.isLurking());        
        assertTrue(apu.getEndTime()==2);        
        verify(apu,times(0)).getStartTime();
        verify(apu,times(1)).getEndTime();
    }
    
    @Test
    public void testPlayPastEnd() throws TimedPlanUnitPlayException
    {
        TimedAbstractPlanUnit apu = spy(createStubPlanUnit("id1", "bml1"));
        when(apu.getStartTime()).thenReturn(0d);
        when(apu.getEndTime()).thenReturn(2d);
        apu.setState(TimedPlanUnitState.IN_EXEC);
        apu.play(2);
        assertTrue(apu.isDone());
        verify(apu,times(0)).playUnit(anyDouble());
        verify(apu,times(1)).stopUnit(2d);
    }
    
    @Test
    public void testStart() throws TimedPlanUnitPlayException
    {
        TimedAbstractPlanUnit apu = spy(createStubPlanUnit("id1", "bml1"));
        when(apu.getStartTime()).thenReturn(0d);
        when(apu.getEndTime()).thenReturn(2d);
        apu.setState(TimedPlanUnitState.LURKING);
        apu.start(0);
        assertTrue(apu.getState()==TimedPlanUnitState.IN_EXEC);
        verify(apu,times(1)).startUnit(0d);
    }
    
    @Test
    public void testStartPastEnd() throws TimedPlanUnitPlayException
    {
        TimedAbstractPlanUnit apu = spy(createStubPlanUnit("id1", "bml1"));
        when(apu.getStartTime()).thenReturn(0d);
        when(apu.getEndTime()).thenReturn(2d);
        apu.setState(TimedPlanUnitState.LURKING);
        boolean ex = false;
        try
        {
            apu.start(3);
        }
        catch (TimedPlanUnitPlayException e)
        {
            ex = true;
        }
        assertTrue(ex);
        assertTrue(apu.getState()==TimedPlanUnitState.DONE);
        verify(apu,times(0)).startUnit(anyDouble());
    }
    
    @Test
    public void testPastEnd()throws TimedPlanUnitPlayException
    {
        TimedAbstractPlanUnit apu = spy(createStubPlanUnit("id1", "bml1"));
        when(apu.getStartTime()).thenReturn(0d);
        when(apu.getEndTime()).thenReturn(2d);
        apu.setState(TimedPlanUnitState.IN_EXEC);
        apu.play(3);        
        assertTrue(apu.getState()==TimedPlanUnitState.DONE);
        verify(apu,times(0)).playUnit(anyDouble());
        verify(apu,times(1)).relaxUnit(3);
    }
}
