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
package asap.speechengine.util;

import hmi.tts.TTSTiming;
import hmi.tts.TimingInfo;
import saiba.bml.core.Behaviour;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.speechengine.SpeechUnitPlanningException;
import asap.speechengine.TimedTTSUnit;
import asap.speechengine.ttsbinding.TTSBinding;


/**
 * Testing stub for a TimedTTSUnit
 * @author welberge
 *
 */
public class TTSUnitStub extends TimedTTSUnit
{
    public TTSUnitStub(FeedbackManager bfm, BMLBlockPeg bbPeg, String text, String id, String bmlId, TTSBinding ttsBin,
            Class<? extends Behaviour> behClass, TTSTiming timing)
    {
        super(bfm, bbPeg, text, bmlId, id, ttsBin, behClass);         
        this.timing = timing;
    }

    @Override
    protected TimingInfo getTiming() throws SpeechUnitPlanningException
    {
        return null;
    }

    @Override
    public void sendProgress(double playTime, double time)
    {
    }

    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {
    }

    @Override
    protected void stopUnit(double time) throws TimedPlanUnitPlayException
    {
    }

    @Override
    public void setup()
    {

    }

    @Override
    public void setParameterValue(String paramId, String value)
    {
    }
}
