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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.animation.Hanim;
import hmi.animation.VJoint;
import hmi.testutil.animation.HanimBody;

import org.junit.Before;
import org.junit.Test;

import asap.animationengine.AnimationPlayer;
import asap.realizer.planunit.ParameterNotFoundException;
/**
 * generic TransitionMU testcases
 * @author welberge
 */
public abstract class AbstractTransitionMUTest
{
    protected AnimationPlayer mockAnimationPlayer = mock(AnimationPlayer.class); 
    protected VJoint human = HanimBody.getLOA1HanimBody();
    protected VJoint humanCopy = HanimBody.getLOA1HanimBody();
    
    protected abstract TransitionMU createTransistionMU();
    protected abstract TransitionMU createTransistionMUConnectedToPlayer();
    @Before
    public void setup()
    {
        when(mockAnimationPlayer.getVNext()).thenReturn(humanCopy);
    }
    
    @Test
    public void testSetJoints() throws ParameterNotFoundException
    {
        TransitionMU mu = createTransistionMUConnectedToPlayer();
        mu.setParameterValue("joints", Hanim.HumanoidRoot+" "+Hanim.r_shoulder);
        assertEquals(Hanim.HumanoidRoot+" "+Hanim.r_shoulder, mu.getParameterValue("joints"));
    }
    
    @Test
    public void testCopy() throws ParameterNotFoundException
    {
        TransitionMU mu = createTransistionMU();
        TransitionMU muCopy = mu.copy(mockAnimationPlayer);
        assertEquals(human.getParts().size(),muCopy.getParameterValue("joints").split(" ").length);
    }
}
