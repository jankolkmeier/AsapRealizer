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

import hmi.faceanimation.FaceController;
import hmi.faceanimation.FaceInterpolator;
import hmi.faceanimation.converters.EmotionConverter;
import hmi.faceanimation.converters.FACS2MorphConverter;
import hmi.faceanimation.converters.FACSConverter;

/**
 * Implements a keyframe animation on morphunits
 * e.g. for corpus reproduction.
 * @author herwinvw
 *
 */
public class KeyframeMorphFU extends KeyframeFaceUnit 
{
    public KeyframeMorphFU(FaceInterpolator mi)
    {
        super(mi);
    }
    
    @Override
    public void play(double t)
    {
        faceController.addMorphTargets(mi.getParts().toArray(new String[0]),getInterpolatedValue(t));
    }

    @Override
    public KeyframeMorphFU copy(FaceController fc, FACSConverter fconv, EmotionConverter econv, FACS2MorphConverter f2mconv)
    {
        KeyframeMorphFU copy = new KeyframeMorphFU(mi);
        setupCopy(copy,fc);       
        return copy;
    }
}
