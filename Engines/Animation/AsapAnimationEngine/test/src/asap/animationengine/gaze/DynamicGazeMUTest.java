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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.testutil.animation.HanimBody;

import org.junit.Before;
import org.junit.Test;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.MUSetupException;

/**
 * Unit tests for the DynamicGazeMU
 * @author hvanwelbergen
 *
 */
public class DynamicGazeMUTest
{
    AnimationPlayer mockPlayer = mock(AnimationPlayer.class);
    
    @Before
    public void setup()
    {
        when(mockPlayer.getVCurr()).thenReturn(HanimBody.getLOA1HanimBody());
        when(mockPlayer.getVNext()).thenReturn(HanimBody.getLOA1HanimBody());
        when(mockPlayer.getVPrev()).thenReturn(HanimBody.getLOA1HanimBody());
    }
    
    @Test
    public void testCopy() throws MUSetupException
    {
        DynamicGazeMU mu = new DynamicGazeMU();
        mu.setInfluence(GazeInfluence.WAIST);
        DynamicGazeMU muCopy = mu.copy(mockPlayer);
        assertEquals(muCopy.influence, GazeInfluence.WAIST);
    }
}
