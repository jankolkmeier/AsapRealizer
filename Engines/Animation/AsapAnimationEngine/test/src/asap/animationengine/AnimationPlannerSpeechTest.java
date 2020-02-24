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
package asap.animationengine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import hmi.physics.PhysicalHumanoid;
import hmi.physics.mixed.MixedSystem;
import hmi.testutil.animation.HanimBody;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import asap.animationengine.gaze.ForwardRestGaze;
import asap.animationengine.gaze.GazeInfluence;
import asap.animationengine.gesturebinding.GestureBinding;
import asap.animationengine.motionunit.AnimationUnit;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.animationengine.motionunit.TimedAnimationUnit;
import asap.animationengine.restpose.SkeletonPoseRestPose;
import asap.realizer.DefaultEngine;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.DefaultTimedPlanUnitPlayer;
import asap.realizer.planunit.PlanManager;

/**
 * Tests if visemeunits are properly added and removed from the animation plan
 * @author welberge
 */
public class AnimationPlannerSpeechTest
{
    private AnimationUnit mockUnit1 = mock(AnimationUnit.class);
    private AnimationUnit mockUnit2 = mock(AnimationUnit.class);
    private AnimationUnit mockUnit3 = mock(AnimationUnit.class);
    private AnimationUnit mockUnit4 = mock(AnimationUnit.class);
    private GestureBinding mockBinding = mock(GestureBinding.class);
    private PhysicalHumanoid mockPh = mock(PhysicalHumanoid.class);
    private FeedbackManager mockBmlFeedbackManager = mock(FeedbackManager.class);
    private PegBoard pegBoard = new PegBoard();
    private AnimationPlayer mockAnimationPlayer = mock(AnimationPlayer.class);

    private AnimationPlanner animationPlanner;
    PlanManager<TimedAnimationUnit> planManager = new PlanManager<>();
    DefaultEngine<TimedAnimationUnit> animationEngine;

    @Before
    public void setup()
    {
        final BMLBlockPeg bbPeg = new BMLBlockPeg("Peg1", 0.3);
        ArrayList<MixedSystem> m = new ArrayList<MixedSystem>();
        MixedSystem ms = new MixedSystem(new float[] { 0, -9.8f, 0 }, mockPh);
        m.add(ms);
        AnimationPlayer ap = new AnimationPlayer(HanimBody.getLOA1HanimBody(), HanimBody.getLOA1HanimBody(), HanimBody.getLOA1HanimBody(),
                m, 0.001f, new AnimationPlanPlayer(new SkeletonPoseRestPose(), new ForwardRestGaze(GazeInfluence.WAIST),
                        mockBmlFeedbackManager, planManager, new DefaultTimedPlanUnitPlayer(), pegBoard));

        animationPlanner = new AnimationPlanner(mockBmlFeedbackManager, ap, mockBinding, planManager, pegBoard);
        List<TimedAnimationMotionUnit> visemeMUs = new ArrayList<TimedAnimationMotionUnit>();

        animationEngine = new DefaultEngine<TimedAnimationUnit>(animationPlanner, ap, planManager);

        TimedAnimationMotionUnit tmu = new TimedAnimationMotionUnit(mockBmlFeedbackManager, bbPeg, "bml1", "speech1", mockUnit1, pegBoard,
                mockAnimationPlayer);
        visemeMUs.add(tmu);
        tmu = new TimedAnimationMotionUnit(mockBmlFeedbackManager, bbPeg, "bml1", "speech1", mockUnit2, pegBoard, mockAnimationPlayer);
        tmu.setSubUnit(true);
        visemeMUs.add(tmu);

        tmu = new TimedAnimationMotionUnit(mockBmlFeedbackManager, bbPeg, "bml1", "speech1", mockUnit3, pegBoard, mockAnimationPlayer);
        visemeMUs.add(tmu);
        tmu.setSubUnit(true);

        tmu = new TimedAnimationMotionUnit(mockBmlFeedbackManager, bbPeg, "bml1", "speech1", mockUnit4, pegBoard, mockAnimationPlayer);
        visemeMUs.add(tmu);
        tmu.setSubUnit(true);

        for (TimedAnimationMotionUnit vis : visemeMUs)
        {
            vis.setSubUnit(true);
            planManager.addPlanUnit(vis);
        }
    }

    @Test
    public void testAddSpeech()
    {
        assertEquals(1, animationEngine.getBehaviours("bml1").size());
    }

    @Test
    public void testRemoveSpeech()
    {
        animationEngine.stopBehaviour("bml1", "speech1", 0);
        assertEquals(0, animationEngine.getBehaviours("bml1").size());
        assertEquals(0, planManager.getBehaviours("bml1").size());
    }
}
