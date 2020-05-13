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
package asap.animationengine.restpose;

import static hmi.testutil.math.Quat4fTestUtil.assertQuat4fEquals;
import static org.junit.Assert.assertEquals;
import hmi.animation.Hanim;
import hmi.animation.SkeletonPose;
import hmi.math.Quat4f;

import org.junit.Test;

import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.TimedPlanUnitPlayException;

import com.google.common.collect.Sets;

/**
 * Unit tests for the SkeletonPoseRestPose
 * @author hvanwelbergen
 * 
 */
public class SkeletonPoseRestPoseTest extends AbstractRestPoseTest
{
    private PegBoard pegBoard = new PegBoard();

    @Test
    public void testcreateTransitionToRest() throws TimedPlanUnitPlayException
    {
        String[] poseJoints = { Hanim.l_elbow, Hanim.l_wrist };
        float[] rotations = { 0, 0, 0, 1, 0, 1, 0, 0 };
        SkeletonPose pose = new SkeletonPose(poseJoints, rotations, "R");

        RestPose restPose = new SkeletonPoseRestPose(pose);
        restPose.setAnimationPlayer(mockAnimationPlayer);
        TimedAnimationMotionUnit tmu = restPose.createTransitionToRest(NullFeedbackManager.getInstance(),
                Sets.newHashSet("l_shoulder", "l_wrist"), 1, 2, "bml1", "transition1", BMLBlockPeg.GLOBALPEG, pegBoard);
        assertEquals(BMLBlockPeg.GLOBALPEG, tmu.getBMLBlockPeg());
        assertEquals("transition1", tmu.getId());
        assertEquals("bml1", tmu.getBMLId());

        tmu.start(1);
        tmu.play(2.99999);
        float[] r = Quat4f.getQuat4f();
        vNext.getPartBySid("l_elbow").getRotation(r);
        assertQuat4fEquals(1f, 0f, 0f, 0f, r, 0.001f);

        vNext.getPartBySid("l_shoulder").getRotation(r);
        assertQuat4fEquals(1f, 0f, 0f, 0f, r, 0.001f);

        vNext.getPartBySid("l_wrist").getRotation(r);
        assertQuat4fEquals(0f, 1f, 0f, 0f, r, 0.001f);
    }
}
