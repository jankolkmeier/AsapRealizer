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
package asap.faceengine;

import hmi.faceanimation.FaceControllerPose;
import asap.faceengine.faceunit.TimedFaceUnit;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.PlanPlayer;
import asap.realizer.planunit.SingleThreadedPlanPlayer;
import asap.realizer.planunit.TimedPlanUnitState;

/**
 * Specialized PlanPlayer that handles conflict resolution for TimedFaceUnit and manages the FaceController
 * @author hvanwelbergen
 */
public class FaceAnimationPlanPlayer implements PlanPlayer
{
    private final SingleThreadedPlanPlayer<TimedFaceUnit> defPlayer;
    /** used to construct faceplan settings before copying them in one go */
    private final FaceControllerPose facePose; 
    /** potential additional controller to manipulate face outside of engine. Usef for faceanimationUI stuff */
    private final FaceControllerPose facePose2;
    
    public FaceAnimationPlanPlayer(FeedbackManager fbm, PlanManager<TimedFaceUnit> planManager, FaceControllerPose fcp)
    {
        this(fbm,planManager, fcp, null);
    }
    public FaceAnimationPlanPlayer(FeedbackManager fbm, PlanManager<TimedFaceUnit> planManager, FaceControllerPose fcp, FaceControllerPose fcp2)
    {
        defPlayer = new SingleThreadedPlanPlayer<>(fbm, planManager);
        facePose = fcp;
        facePose2 = fcp2;
    }

    @Override
    public void play(double t)
    {
        facePose.clear();
        defPlayer.play(t);
        facePose.toTarget();
        if (facePose2!=null)facePose2.toTargetAdditive();
    }

    @Override
    public void stopPlanUnit(String bmlId, String id, double globalTime)
    {
        defPlayer.stopPlanUnit(bmlId, id, globalTime);
    }

    @Override
    public void stopBehaviourBlock(String bmlId, double time)
    {
        defPlayer.stopBehaviourBlock(bmlId, time);
    }

    @Override
    public void interruptPlanUnit(String bmlId, String id, double globalTime)
    {
        defPlayer.interruptPlanUnit(bmlId, id, globalTime);
    }

    @Override
    public void interruptBehaviourBlock(String bmlId, double time)
    {
        defPlayer.interruptBehaviourBlock(bmlId, time);
    }

    @Override
    public void reset(double time)
    {
        defPlayer.reset(time);
    }

    @Override
    public void setBMLBlockState(String bmlId, TimedPlanUnitState state)
    {
        defPlayer.setBMLBlockState(bmlId, state);

    }

    @Override
    public void shutdown()
    {
        defPlayer.shutdown();
    }

    @Override
    public void updateTiming(String bmlId)
    {
                
    }
}
