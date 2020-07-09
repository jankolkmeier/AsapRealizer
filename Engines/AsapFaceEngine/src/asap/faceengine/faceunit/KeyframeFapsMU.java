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
import hmi.faceanimation.model.MPEG4Configuration;

import java.util.ArrayList;
import java.util.List;

import asap.motionunit.MUPlayException;

import com.google.common.collect.ImmutableList;

/**
 * MotionUnit for MPEG-4 keyframe animation
 * @author herwinvw
 *
 */
public class KeyframeFapsMU extends KeyframeFaceUnit
{
    private final List<Integer> parts;

    public KeyframeFapsMU(FaceInterpolator mi)
    {
        super(mi);
        List<Integer> ps = new ArrayList<Integer>();
        for (String part : mi.getParts())
        {
            ps.add(Integer.parseInt(part));
        }
        parts = ImmutableList.copyOf(ps);
    }

    @Override
    public KeyframeFapsMU copy(FaceController fc, FACSConverter fconv, EmotionConverter econv, FACS2MorphConverter f2mconv)
    {
        KeyframeFapsMU copy = new KeyframeFapsMU(mi);
        setupCopy(copy, fc);
        return copy;
    }

    @Override
    public void play(double t) throws MUPlayException
    {
        float current[] = getInterpolatedValue(t);
        MPEG4Configuration config = new MPEG4Configuration();
        int i = 0;
        for (Integer part : parts)
        {
            config.setValue(part-1, Math.round(current[i]));
            i++;
        }
        faceController.addMPEG4Configuration(config);
    }

}
