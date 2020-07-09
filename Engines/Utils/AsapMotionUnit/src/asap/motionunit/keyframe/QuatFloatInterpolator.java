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
package asap.motionunit.keyframe;

import hmi.util.Builder;

import java.util.ArrayList;
import java.util.List;

import asap.math.QuatInterpolator;

import com.google.common.collect.ImmutableList;

/**
 * Linear interpolator for of a list of keyframes. Interpolates groups of 4 dofs as quat using slerp.
 * @author hvanwelbergen
 */
public class QuatFloatInterpolator<E extends QuatInterpolator> implements Interpolator
{
    private List<E> quatInterPolators = new ArrayList<E>();
    private List<KeyFrame> keyFrames;
    private int nrOfDof;
    private Builder<E> builder;

    public QuatFloatInterpolator(Builder<E> builder)
    {
        this.builder = builder;
    }

    @Override
    public void setKeyFrames(List<KeyFrame> frames, int nrOfDof)
    {
        keyFrames = frames;
        this.nrOfDof = nrOfDof;
        quatInterPolators.clear();

        for (int i = 0; i < nrOfDof / 4; i++)
        {
            double p[][] = new double[frames.size()][];
            int j = 0;
            for (KeyFrame kf : frames)
            {
                p[j] = new double[5];
                p[j][0] = kf.getFrameTime();
                for (int k = 0; k < 4; k++)
                {
                    p[j][k + 1] = kf.getDofs()[i * 4 + k];
                }
                j++;
            }
            E qi = builder.build();
            qi.setPVal(p);
            quatInterPolators.add(qi);
        }
    }

    @Override
    public KeyFrame interpolate(double time)
    {
        float dofs[] = new float[quatInterPolators.size() * 4];
        int i = 0;
        for (QuatInterpolator inter : quatInterPolators)
        {
            inter.interpolate(time, dofs, i);
            i += 4;
        }
        return new KeyFrame(time, dofs);
    }

    @Override
    public QuatFloatInterpolator<E> copy()
    {
        QuatFloatInterpolator<E> copy = new QuatFloatInterpolator<>(builder);
        copy.setKeyFrames(ImmutableList.copyOf(keyFrames), nrOfDof);
        return copy;
    }
}
