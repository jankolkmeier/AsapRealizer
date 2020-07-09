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

import hmi.animation.Hanim;
import hmi.animation.VJoint;
import hmi.animation.VJointUtils;
import hmi.math.Quat4f;

import java.util.HashSet;
import java.util.Set;

import saiba.bml.core.OffsetDirection;

import com.google.common.collect.ImmutableSet;

/**
 * Utils for gaze motionunits
 * @author herwinvw
 *
 */
public final class GazeUtils
{
    private GazeUtils()
    {
    }
    
    public static final float[] getOffsetRotation(OffsetDirection offsetDirection, double offsetAngle)
    {
        float[] q = Quat4f.getQuat4f();
        Quat4f.setIdentity(q);
        switch (offsetDirection)
        {
        case NONE:
            break;
        case RIGHT:
            Quat4f.setFromAxisAngle4f(q, 0, -1, 0, (float) Math.toRadians(offsetAngle));
            break;
        case LEFT:
            Quat4f.setFromAxisAngle4f(q, 0, 1, 0, (float) Math.toRadians(offsetAngle));
            break;
        case UP:
            Quat4f.setFromAxisAngle4f(q, -1, 0, 0, (float) Math.toRadians(offsetAngle));
            break;
        case DOWN:
            Quat4f.setFromAxisAngle4f(q, 1, 0, 0, (float) Math.toRadians(offsetAngle));
            break;
        case UPRIGHT:
            Quat4f.setFromAxisAngle4f(q, -1, -1, 0, (float) Math.toRadians(offsetAngle));
            break;
        case UPLEFT:
            Quat4f.setFromAxisAngle4f(q, -1, 1, 0, (float) Math.toRadians(offsetAngle));
            break;
        case DOWNLEFT:
            Quat4f.setFromAxisAngle4f(q, 1, 1, 0, (float) Math.toRadians(offsetAngle));
            break;
        case DOWNRIGHT:
            Quat4f.setFromAxisAngle4f(q, 1, -1, 0, (float) Math.toRadians(offsetAngle));
            break;
        case POLAR:
            break;
        }
        return q;
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings(
            value={"SF_SWITCH_FALLTHROUGH","SF_SWITCH_NO_DEFAULT"}, 
            justification="Fallthrough by design to add joints in order.")
    public static ImmutableSet<String> getJoints(VJoint root, GazeInfluence influence)
    {
        Set<String> joints = new HashSet<String>();
        switch (influence)
        {
        case WAIST:
            joints.addAll(VJointUtils.gatherJointSids(Hanim.LUMBAR_JOINTS, root));
        case SHOULDER:
            joints.addAll(VJointUtils.gatherJointSids(Hanim.THORACIC_JOINTS, root));
        case NECK:
            joints.addAll(VJointUtils.gatherJointSids(Hanim.CERVICAL_JOINTS, root));
        case EYES:            
        default:
            joints.addAll(VJointUtils.gatherJointSids(new String[] { Hanim.r_eyeball_joint, Hanim.l_eyeball_joint }, root));
        }
        return ImmutableSet.copyOf(joints);
    }
}
