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
package asap.ipaacattsbinding;

import hmi.tts.BMLTTSBridge;
import hmi.tts.util.PhonemeToVisemeMapping;
import saiba.bml.core.SpeechBehaviour;
import asap.bml.ext.ssml.SSMLBehaviour;
import asap.speechengine.ttsbinding.TTSBinding;
import asap.tts.ipaaca.IpaacaSSMLTTSBridge;
import asap.tts.ipaaca.IpaacaTTSGenerator;
import asap.tts.ipaaca.VisualProsodyAnalyzer;

/**
 * Binds SpeechBehaviour to Ipaaca TTS generation.
 * @author hvanwelbergen
 *
 */
public class IpaacaTTSBinding extends TTSBinding
{
    private IpaacaTTSGenerator ipaacaTtsGenerator;
    
    public IpaacaTTSBinding(PhonemeToVisemeMapping ptv, VisualProsodyAnalyzer vpa)
    {
        ipaacaTtsGenerator = new IpaacaTTSGenerator(ptv,vpa);
        ttsGenerator = ipaacaTtsGenerator;
        
        ttsBridgeMap.put(SpeechBehaviour.class, new BMLTTSBridge(ipaacaTtsGenerator));
        ttsBridgeMap.put(SSMLBehaviour.class, new IpaacaSSMLTTSBridge(ipaacaTtsGenerator));
        
        supportedBehaviours.add(SSMLBehaviour.class);
    }
    
    @Override
    public void cleanup()
    {
        ipaacaTtsGenerator.close();
    }
}
