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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import hmi.faceanimation.FaceController;
import hmi.faceanimation.FaceInterpolator;
import hmi.faceanimation.converters.FACSConverter;
import hmi.faceanimation.model.MPEG4Configuration;

import org.junit.Before;
import org.junit.Test;

import asap.motionunit.MUPlayException;

/**
 * Unit tests for the KeyframeFacsFU
 * @author herwinvw
 *
 */
public class KeyframeFacsFUTest
{
private FaceController mockFaceController = mock(FaceController.class);
    
    private KeyframeFacsFU fu;
    private FACSConverter fconv = new FACSConverter();
    
    @Before
    public void setup()
    {
        FaceInterpolator mi = new FaceInterpolator();
        mi.readXML("<FaceInterpolator parts=\"1.BOTH 2.LEFT\">1 0.1 0.2\n 2 0.3 0.4</FaceInterpolator>");
        fu = new KeyframeFacsFU(mi);
        fu = fu.copy(mockFaceController, fconv, null, null);
    }
    
    @Test
    public void testPlay() throws MUPlayException
    {
        fu.play(0.5);
        verify(mockFaceController).addMPEG4Configuration(any(MPEG4Configuration.class));
    }
}
