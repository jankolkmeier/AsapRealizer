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
package asap.livemocapengine.planunit;

import hmi.headandgazeembodiments.GazeEmbodiment;
import asap.livemocapengine.inputs.EulerInput;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

/**
 * Gazes at a position provided by a PositionSensor
 * @author welberge
 *
 */
public class RemoteGazeTMU extends LiveMocapTMU
{
    private final EulerInput eulerInput;
    private final GazeEmbodiment gazeEmbodiment;
    
    public RemoteGazeTMU(EulerInput gazeinput, GazeEmbodiment embodiment, FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId)
    {
        super(fbm, bmlPeg, bmlId, behId);
        this.eulerInput = gazeinput;
        this.gazeEmbodiment = embodiment;   
    }

    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {
        gazeEmbodiment.setGazeRollPitchYawDegrees(eulerInput.getRollDegrees(), eulerInput.getPitchDegrees(), eulerInput.getYawDegrees());
    }
}
