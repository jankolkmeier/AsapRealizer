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
package asap.speechengine;

import hmi.audioenvironment.SoundManager;
import saiba.bml.core.Behaviour;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.speechengine.ttsbinding.TTSBinding;

/**
 * Factory to generate WavSpeechUnit
 * 
 * @author welberge
 */
public final class WavTTSUnitFactory implements TimedTTSUnitFactory
{
    private final FeedbackManager fbManager;
    private final SoundManager soundManager;
    
    public WavTTSUnitFactory(FeedbackManager fbm, SoundManager soundManager)
    {
        fbManager = fbm;
        this.soundManager = soundManager; 
    }

    @Override
    public TimedTTSUnit createTimedTTSUnit(BMLBlockPeg bbPeg, String text,
            String voiceId, String bmlId, String id, TTSBinding ttsBin,
            Class<? extends Behaviour> behClass)
    {
        return new TimedWavTTSUnit(fbManager, soundManager, bbPeg, text, voiceId, bmlId, id, ttsBin, behClass);
    }
}
