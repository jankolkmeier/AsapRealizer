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
package asap.animationengine.gaze;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.when;
import hmi.animation.VJoint;
import hmi.testutil.animation.HanimBody;
import hmi.worldobjectenvironment.WorldObjectManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.MUSetupException;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Unit tests for the DynamicRestGaze
 * @author hvanwelbergen
 * 
 */
public class DynamicRestGazeTest
{
    private BMLBlockPeg bbPeg = new BMLBlockPeg("bml1",0);
    private PegBoard pb = new PegBoard();
    private AnimationPlayer mockAnimationPlayer = mock(AnimationPlayer.class); 
    private WorldObjectManager woManager = new WorldObjectManager();
    
    @Before
    public void setup()
    {
        final VJoint vNext = HanimBody.getLOA1HanimBodyWithEyes();
        final VJoint vCurr = HanimBody.getLOA1HanimBodyWithEyes();
        when(mockAnimationPlayer.getVNext()).thenReturn(vNext);
        when(mockAnimationPlayer.getVCurr()).thenReturn(vCurr);        
        
        stub(mockAnimationPlayer.getVCurrPartBySid(any(String.class))).toAnswer(new Answer<VJoint>() {
            public VJoint answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return vCurr.getPart((String)args[0]);
            }
        });
        stub(mockAnimationPlayer.getVNextPartBySid(any(String.class))).toAnswer(new Answer<VJoint>() {
            public VJoint answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return vNext.getPart((String)args[0]);
            }
        });
        
        when(mockAnimationPlayer.getWoManager()).thenReturn(woManager);
    }
    
    @Test
    public void testCreateGazeShiftTMU() throws MUSetupException, ParameterException
    {
        DynamicRestGaze g = new DynamicRestGaze();
        g.setParameterValue("target", "target");
        g.setParameterValue("influence","SHOULDER");
        g = g.copy(mockAnimationPlayer);
        assertNotNull(g.createGazeShiftTMU(NullFeedbackManager.getInstance(), bbPeg, "bml1", "gaze1", pb));
    }
    
    @Test
    public void testPlayGazeShiftTMU()throws MUSetupException, TimedPlanUnitPlayException, ParameterException
    {
        DynamicRestGaze g = new DynamicRestGaze();
        g.setParameterValue("target", "1,1,1");
        g.setParameterValue("influence","SHOULDER");
        g = g.copy(mockAnimationPlayer);
        GazeShiftTMU tmu = g.createGazeShiftTMU(NullFeedbackManager.getInstance(), bbPeg, "bml1", "gaze1", pb);
        tmu.setTimePeg("start", TimePegUtil.createAbsoluteTimePeg(0));
        tmu.setState(TimedPlanUnitState.LURKING);
        tmu.start(0);
        tmu.play(0);
        assertEquals(TimedPlanUnitState.IN_EXEC, tmu.getState());
    }
}
