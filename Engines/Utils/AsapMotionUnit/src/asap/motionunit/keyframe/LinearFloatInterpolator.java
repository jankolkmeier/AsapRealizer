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

import java.util.ArrayList;
import java.util.List;

import asap.math.LinearInterpolator;

import com.google.common.collect.ImmutableList;

/**
 * Linear interpolator for of a list of keyframes. Interpolates each dof seperately. 
 * @author hvanwelbergen
 *
 */
public class LinearFloatInterpolator implements Interpolator
{
    private List<LinearInterpolator> linInterPolators = new ArrayList<LinearInterpolator>();
    private List<KeyFrame> keyFrames;
    private int nrOfDof;
    
    @Override
    public void setKeyFrames(List<KeyFrame> frames, int nrOfDof)
    {
        keyFrames = frames;
        this.nrOfDof = nrOfDof;
        linInterPolators.clear();
        for(int i=0;i<nrOfDof;i++)
        {
            double p[][]=new double[frames.size()][];
            int j=0;
            for(KeyFrame kf:frames)
            {
                p[j]=new double[2];
                p[j][0] = kf.getFrameTime();
                p[j][1] = kf.getDofs()[i];
                j++;
            }
            linInterPolators.add(new LinearInterpolator(p));            
        }
    }
    
    @Override
    public KeyFrame interpolate(double time)
    {
        float dofs[]=new float [linInterPolators.size()];
        int i=0;
        for(LinearInterpolator inter:linInterPolators)
        {
            dofs[i] = (float)inter.interpolate(time);
            i++;
        }
        return new KeyFrame(time,dofs);
    }
    
    @Override
    public LinearFloatInterpolator copy()
    {
        LinearFloatInterpolator interp = new LinearFloatInterpolator();
        interp.setKeyFrames(ImmutableList.copyOf(keyFrames), nrOfDof);
        return interp;
    }
}
