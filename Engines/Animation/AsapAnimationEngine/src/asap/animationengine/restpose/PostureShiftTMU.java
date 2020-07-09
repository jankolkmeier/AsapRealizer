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

import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.AnimationUnit;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;

/**
 * Runs the TimedAnimationUnit, ends with setting the new rest pose state.
 * @author welberge
 * 
 */
public class PostureShiftTMU extends TimedAnimationMotionUnit
{

    private final RestPose restPose;
    private AnimationPlayer aniPlayer;

    public PostureShiftTMU(FeedbackManager bbf, BMLBlockPeg bmlBlockPeg, String bmlId, String id, AnimationUnit mu, PegBoard pb,
            RestPose restPose, AnimationPlayer ap)
    {
        super(bbf, bmlBlockPeg, bmlId, id, mu, pb, ap);
        this.restPose = restPose;
        aniPlayer = ap;
    }

    @Override
    protected void stopUnit(double time)
    {
        super.stopUnit(time);
        aniPlayer.setRestPose(restPose);
        restPose.start(time);
    }
}
