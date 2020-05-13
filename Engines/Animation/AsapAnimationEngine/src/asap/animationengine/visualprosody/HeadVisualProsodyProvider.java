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
package asap.animationengine.visualprosody;

import saiba.bml.core.Behaviour;
import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.TimedAnimationUnit;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.visualprosody.VisualProsodyProvider;
import asap.visualprosody.VisualProsody;

/**
 * Generates head movement on the basis of speech prosody
 * @author hvanwelbergen
 */
public class HeadVisualProsodyProvider implements VisualProsodyProvider
{
    private final VisualProsody visualProsody;
    private final AnimationPlayer animationPlayer;
    private final PlanManager<TimedAnimationUnit> animationPlanManager;
    
    public HeadVisualProsodyProvider(VisualProsody v, AnimationPlayer ap, PlanManager<TimedAnimationUnit> animationPlanManager)
    {
        this.visualProsody = v;
        this.animationPlayer = ap;
        this.animationPlanManager = animationPlanManager;        
    }

    @Override
    public void visualProsody(BMLBlockPeg bbPeg, Behaviour beh, TimedPlanUnit speechUnit, double[] f0, double[] rmsEnergy,
            double frameDuration, float amplitude, float k)
    {
        System.out.println("scheduling visual prosody");
        long time = System.currentTimeMillis();

        VisualProsodyUnit tmu = new VisualProsodyUnit(NullFeedbackManager.getInstance(), bbPeg, beh.getBmlId(), beh.id, 
                speechUnit, visualProsody, animationPlayer, f0, rmsEnergy, frameDuration,
                speechUnit.getTimePeg("start"), speechUnit.getTimePeg("end"));
        tmu.setAmplitude(amplitude);
        tmu.setK(k);
        tmu.setSubUnit(true);
        tmu.setTimePeg("start", speechUnit.getTimePeg("start"));
        animationPlanManager.addPlanUnit(tmu);
        System.out.println("Scheduling visual prosody took " + (System.currentTimeMillis() - time) + " ms.");
        System.out.println("Speech duration: " + ((int) (speechUnit.getPreferedDuration() * 1000)) + " ms.");
    }
}
