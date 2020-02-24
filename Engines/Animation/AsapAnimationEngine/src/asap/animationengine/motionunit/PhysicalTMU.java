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
package asap.animationengine.motionunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.controller.ControllerMU;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.TimedPlanUnitPlayException;


/**
 * Timed motion unit steering a Physical motion unit.
 * @author Herwin
 * 
 */
public class PhysicalTMU extends TimedAnimationMotionUnit
{
    private static Logger logger = LoggerFactory.getLogger(PhysicalTMU.class.getName());

    public PhysicalTMU(FeedbackManager bbm, BMLBlockPeg bbPeg, String bmlId, String id, AnimationUnit m, PegBoard pb, AnimationPlayer aniPlayer)
    {
        super(bbm, bbPeg, bmlId, id, m, pb, aniPlayer);
    }

    @Override
    public void startUnit(double t) throws TimedPlanUnitPlayException
    {
        ControllerMU pc = (ControllerMU) getMotionUnit();
        pc.reset();
        logger.debug("Resetting controller {}:{}", getBMLId(), getId());
    }
    
    public int getPriority()
    {
        return ((ControllerMU)getMotionUnit()).getPriority();
    }
}
