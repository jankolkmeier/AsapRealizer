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
package asap.realizer.visualprosody;

import saiba.bml.core.Behaviour;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnit;

/**
 * Provides visual prosody movement for the provided f0, rmsEnergy arrays, in which each lasts frameDuration seconds
 * @author hvanwelbergen
 */
public interface VisualProsodyProvider
{

    void visualProsody(BMLBlockPeg bbPeg, Behaviour beh, TimedPlanUnit speechUnit, double f0[], double rmsEnergy[], double frameDuration,
            float amount, float k);

    default void visualProsody(BMLBlockPeg bbPeg, Behaviour beh, TimedPlanUnit speechUnit, double f0[], double rmsEnergy[],
            double frameDuration, float amount)
    {
        visualProsody(bbPeg, beh, speechUnit, f0, rmsEnergy, frameDuration, amount, 1f);
    }
    
    default void visualProsody(BMLBlockPeg bbPeg, Behaviour beh, TimedPlanUnit speechUnit, double f0[], double rmsEnergy[],
            double frameDuration)
    {
        visualProsody(bbPeg, beh, speechUnit, f0, rmsEnergy, frameDuration, 1f, 1f);
    }
}
