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
package asap.maryttsbinding;

import hmi.tts.BMLTTSBridge;
import hmi.tts.mary.MaryAllophonesTTSBridge;
import hmi.tts.mary.MarySSMLTTSBridge;
import hmi.tts.mary.MaryTTSGenerator;
import hmi.tts.mary.MaryWordsTTSBridge;
import hmi.tts.mary.MaryXMLTTSBridge;
import hmi.tts.util.PhonemeToVisemeMapping;
import saiba.bml.core.SpeechBehaviour;
import asap.bml.ext.maryxml.MaryAllophonesBehaviour;
import asap.bml.ext.maryxml.MaryWordsBehaviour;
import asap.bml.ext.maryxml.MaryXMLBehaviour;
import asap.bml.ext.ssml.SSMLBehaviour;
import asap.speechengine.ttsbinding.TTSBinding;

/**
 * Binds SpeechBehaviour, SSMLBehaviour, MaryXMLBehaviour, MaryWordsBehaviour and MaryAllophonesBehaviour
 * to the MaryTTS speech generation
 * @author welberge
 *
 */
public class MaryTTSBinding extends TTSBinding
{
    private MaryTTSGenerator maryTTSGenerator;
    public MaryTTSBinding(String marydir, PhonemeToVisemeMapping ptv)
    {
        try
        {
            maryTTSGenerator = new MaryTTSGenerator(marydir, ptv);
            ttsGenerator = maryTTSGenerator;
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }        
        ttsBridgeMap.put(SpeechBehaviour.class, new BMLTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(SSMLBehaviour.class, new MarySSMLTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(MaryXMLBehaviour.class, new MaryXMLTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(MaryWordsBehaviour.class, new MaryWordsTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(MaryAllophonesBehaviour.class, new MaryAllophonesTTSBridge(maryTTSGenerator));
        
        supportedBehaviours.add(SSMLBehaviour.class);
        supportedBehaviours.add(MaryXMLBehaviour.class);
        supportedBehaviours.add(MaryWordsBehaviour.class);
        supportedBehaviours.add(MaryAllophonesBehaviour.class);
    }
    @Override
    public void cleanup()
    {
            
    }
}
