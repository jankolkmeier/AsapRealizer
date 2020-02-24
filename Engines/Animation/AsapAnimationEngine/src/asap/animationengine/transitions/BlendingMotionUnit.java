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
package asap.animationengine.transitions;

import hmi.animation.VJoint;
import hmi.math.Quat4f;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import asap.animationengine.AnimationPlayer;
import asap.motionunit.MUPlayException;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;

public class BlendingMotionUnit extends TransitionMU
{
    private float startPose[] = null;
    private List<VJoint> startJoints;
    private List<VJoint> endJoints;
    private List<VJoint> joints;

    public BlendingMotionUnit(List<VJoint> targetJoints, List<VJoint> startPoseJoints, List<VJoint> endPoseJoints)
    {
        super();
        joints = targetJoints;
        startJoints = startPoseJoints;
        endJoints = endPoseJoints;
    }

    @Override
    public Set<String> getPhysicalJoints()
    {
        return ImmutableSet.of();
    }

    @Override
    public Set<String> getKinematicJoints()
    {
        Collection<String> j = Collections2.transform(joints, new Function<VJoint, String>()
        {
            @Override
            public String apply(VJoint joint)
            {
                if (joint == null) return "";
                return joint.getSid();
            }
        });
        return ImmutableSet.copyOf(j);
    }

    @Override
    public Set<String> getAdditiveJoints()
    {
        return ImmutableSet.of();
    }

    @Override
    public void startUnit(double t) throws MUPlayException
    {
        setStartPose();
    }

    @Override
    public BlendingMotionUnit copy(AnimationPlayer player)
    {
        this.aniPlayer = player;
        return new BlendingMotionUnit(joints, startJoints, endJoints);
    }

    private float[] getEndPose()
    {
        int i = 0;
        float rotations[] = new float[joints.size() * 4];
        for (VJoint vj : endJoints)
        {
            vj.getRotation(rotations, i);
            i += 4;
        }
        return rotations;
    }

    @Override
    public void play(double t)
    {
        float result[] = new float[startPose.length];
        if (startPose != null)
        {
            Quat4f.interpolateArrays(result, startPose, getEndPose(), (float) t);
            int i = 0;
            for (VJoint vj : joints)
            {
                vj.setRotation(result, i);
                i += 4;
            }
        }
    }

    @Override
    public void setStartPose()
    {
        int i = 0;
        startPose = new float[joints.size() * 4];
        for (VJoint v : startJoints)
        {
            v.getRotation(startPose, i);
            i += 4;
        }
    }
}
