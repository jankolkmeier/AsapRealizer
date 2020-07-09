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
import static org.mockito.Mockito.verify;
import hmi.faceanimation.FaceController;
import hmi.faceanimation.FaceInterpolator;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for the KeyframeMorphFU
 * @author herwinvw
 *
 */
public class KeyframeMorphFUTest
{
    private FaceController mockFaceController = mock(FaceController.class);
    
    private KeyframeMorphFU fu;

    @Before
    public void setup()
    {
        FaceInterpolator mi = new FaceInterpolator();
        mi.readXML("<FaceInterpolator parts=\"morph1 morph2\">1 0.1 0.2\n 2 0.3 0.4</FaceInterpolator>");
        fu = new KeyframeMorphFU(mi);
        fu = fu.copy(mockFaceController, null, null, null);
    }
    
    @Test
    public void testPlay()
    {
        fu.play(0.5);
        verify(mockFaceController).addMorphTargets(any(String[].class), eq(new float[] { 0.2f, 0.3f}));
    }
}
