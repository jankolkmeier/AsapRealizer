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

import static org.junit.Assert.assertNotNull;
import hmi.testutil.animation.HanimBody;

import org.junit.Test;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.AnimationPlayerMock;
import asap.animationengine.motionunit.MUSetupException;
import asap.motionunit.MUPlayException;

/**
 * Unit testcases for EyeGazeMU
 * @author Herwin
 */
public class EyeGazeMUTest
{
    private AnimationPlayer mockAnimationPlayer;

    @Test(expected = MUSetupException.class)
    public void testNoEyes() throws MUPlayException, MUSetupException
    {
        mockAnimationPlayer = AnimationPlayerMock.createAnimationPlayerMock(HanimBody.getLOA1HanimBody(), HanimBody.getLOA1HanimBody());
        EyeGazeMU mu = new EyeGazeMU();
        mu.copy(mockAnimationPlayer);
    }

    @Test
    public void testCopy() throws MUSetupException
    {
        mockAnimationPlayer = AnimationPlayerMock.createAnimationPlayerMock(HanimBody.getLOA1HanimBodyWithEyes(),
                HanimBody.getLOA1HanimBodyWithEyes());
        EyeGazeMU mu = new EyeGazeMU();
        assertNotNull(mu.copy(mockAnimationPlayer));
    }
}
