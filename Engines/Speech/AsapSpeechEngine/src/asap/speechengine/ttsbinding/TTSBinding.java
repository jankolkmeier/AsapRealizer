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
package asap.speechengine.ttsbinding;

import hmi.tts.AbstractTTSGenerator;
import hmi.tts.TTSBridge;
import hmi.tts.TTSCallback;
import hmi.tts.TTSException;
import hmi.tts.TTSTiming;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saiba.bml.core.Behaviour;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;

/**
 * Binds a BML behavior to a TTSBridge
 * @author welberge
 */
public abstract class TTSBinding
{
    protected AbstractTTSGenerator ttsGenerator;

    protected List<Class<? extends Behaviour>> supportedBehaviours = new ArrayList<Class<? extends Behaviour>>();
    protected Map<Class<? extends Behaviour>, TTSBridge> ttsBridgeMap = new HashMap<Class<? extends Behaviour>, TTSBridge>();

    public List<Class<? extends Behaviour>> getSupportedBMLDescriptionExtensions()
    {
        return supportedBehaviours;
    }

    public void setParameterValue(String parameter, String value) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
    }

    public void setFloatParameterValue(String parameter, float value) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
    }

    public float getFloatParameterValue(String parameter) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
    }

    public String getParameterValue(String parameter) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
    }

    /**
     * Speaks out the sentence, does the appropriate callbacks, fills out visime,
     * phoneme, bookmark and word lists, uses the behClass to identify the content (e.g. BML, MS SAPI XML, Mary XML, SSML, ..)
     * 
     * @param text
     *            the text or script to speak
     * @throws TTSException
     */
    public TTSTiming speak(Class<? extends Behaviour> behClass, String text) throws TTSException
    {
        TTSBridge bridge = ttsBridgeMap.get(behClass);
        if(bridge!=null)
        {
            return bridge.speak(text);        
        }
        else
        {
            throw new TTSException("bridge not available", new Exception("bridge for "+behClass.getName()+" not available."));
        }
    }

    public TTSTiming speakToFile(Class<? extends Behaviour> behClass, String text, String filename) throws IOException,TTSException
    {
        TTSBridge bridge = ttsBridgeMap.get(behClass);
        if(bridge!=null)
        {
            return bridge.speakToFile(text, filename);        
        }
        else
        {
            throw new TTSException("bridge not available", new Exception("bridge for "+behClass.getName()+" not available."));
        }        
    }

    public TTSTiming getTiming(Class<? extends Behaviour> behClass, String text) throws TTSException
    {
        TTSBridge bridge = ttsBridgeMap.get(behClass);
        if (bridge != null)
        {
            return bridge.getTiming(text);
        }
        else
        {
            throw new TTSException("bridge not available", new Exception("bridge for "+behClass.getName()+" not available."));
        }
    }

    public void setCallback(TTSCallback cb)
    {
        ttsGenerator.setCallback(cb);
    }

    public void setVoice(String voice)
    {
        ttsGenerator.setVoice(voice);
    }

    public String getVoice()
    {
        return ttsGenerator.getVoice();
    }

    public String[] getVoices()
    {
        return ttsGenerator.getVoices();
    }

    public abstract void cleanup();
}
