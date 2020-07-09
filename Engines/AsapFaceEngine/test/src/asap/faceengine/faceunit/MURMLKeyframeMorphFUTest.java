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
package asap.faceengine.faceunit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import hmi.faceanimation.FaceController;

import org.junit.Before;
import org.junit.Test;

import asap.motionunit.MUPlayException;
import asap.motionunit.keyframe.KeyFrame;
import asap.motionunit.keyframe.LinearFloatInterpolator;
import asap.timemanipulator.LinearManipulator;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the KeyframeMorphFU
 * @author Herwin
 * 
 */
public class MURMLKeyframeMorphFUTest
{
    private FaceController mockFaceController = mock(FaceController.class);
    private MURMLKeyframeMorphFU fu;

    @Before
    public void setup()
    {
        fu = new MURMLKeyframeMorphFU(ImmutableList.of("morph1", "morph2", "morph3"), new LinearFloatInterpolator(), new LinearManipulator(),
                ImmutableList.of(new KeyFrame(0.5, new float[] { 4, 6, 8}), new KeyFrame(1, new float[] { 2, 3, 4 })), 3, true);
        fu.setFaceController(mockFaceController);
    }

    @Test
    public void testPlay() throws MUPlayException
    {
        fu.startUnit(0);
        fu.play(0.25);
        verify(mockFaceController).addMorphTargets(any(String[].class), eq(new float[] { 2, 3, 4 }));
        fu.play(0.5);
        verify(mockFaceController).addMorphTargets(any(String[].class), eq(new float[] { 4, 6, 8 }));
        fu.play(1);
        verify(mockFaceController,times(2)).addMorphTargets(any(String[].class), eq(new float[] { 2, 3, 4 }));
    }

    @Test
    public void testInterrupt() throws MUPlayException
    {
        fu.startUnit(0);
        fu.play(0.25);
        fu.interruptFromHere();
        fu.play(0.5);
        verify(mockFaceController,times(2)).addMorphTargets(any(String[].class), eq(new float[] { 2, 3, 4 }));
    }
}
