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
package asap.animationengine;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.animation.VJoint;
import hmi.testutil.animation.HanimBody;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Provides a mockup for the AnimationPlayer that uses a 'real' set of joints for VNext and VCurr
 * @author hvanwelbergen
 *
 */
public final class AnimationPlayerMock
{
    private AnimationPlayerMock(){}
    
    private static final class PartBySidAnswer implements Answer<VJoint>
    {
        private final VJoint joint;
        
        public PartBySidAnswer(VJoint vj)
        {
            this.joint = vj;
        }
        @Override
        public VJoint answer(InvocationOnMock invocation) throws Throwable
        {
            String sid = (String)invocation.getArguments()[0];
            return joint.getPartBySid(sid);
        }        
    }
    
    public static AnimationPlayer createAnimationPlayerMock(VJoint vCurr, VJoint vNext)
    {
        AnimationPlayer mockAniplayer = mock(AnimationPlayer.class);
        when(mockAniplayer.getVCurr()).thenReturn(vCurr);
        when(mockAniplayer.getVNext()).thenReturn(vNext);
        when(mockAniplayer.getVCurrPartBySid(anyString())).thenAnswer(new PartBySidAnswer(vCurr));
        when(mockAniplayer.getVNextPartBySid(anyString())).thenAnswer(new PartBySidAnswer(vNext));
        return mockAniplayer;
    }
    
    public static AnimationPlayer createAnimationPlayerMock()
    {
        return createAnimationPlayerMock(HanimBody.getLOA1HanimBody(), HanimBody.getLOA1HanimBody());
    }
}
