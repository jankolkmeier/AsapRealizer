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

import hmi.faceembodiments.AUConfig;
import hmi.faceembodiments.FACSFaceEmbodiment;
import asap.livemocapengine.inputs.FACSFaceInput;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

/**
 * Uses an FACSFaceInput to remotely control a FACSFaceEmbodiment
 * @author welberge
 * 
 */
public class RemoteFaceFACSTMU extends LiveMocapTMU
{
    private final FACSFaceInput faceInput;
    private final FACSFaceEmbodiment faceEmbodiment;

    public RemoteFaceFACSTMU(FACSFaceInput faceInput, FACSFaceEmbodiment faceEmbodiment, FeedbackManager fbm, BMLBlockPeg bmlPeg,
            String bmlId, String behId)
    {
        super(fbm, bmlPeg, bmlId, behId);
        this.faceEmbodiment = faceEmbodiment;
        this.faceInput = faceInput;
    }

    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {
        AUConfig[] configs = faceInput.getAUConfigs();
        if (configs != null)
        {
            faceEmbodiment.setAUs(faceInput.getAUConfigs());
        }
    }
}
