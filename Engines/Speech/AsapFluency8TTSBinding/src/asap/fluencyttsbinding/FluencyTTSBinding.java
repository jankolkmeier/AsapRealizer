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
package asap.fluencyttsbinding;

import hmi.tts.util.*;
import hmi.tts.BMLTTSBridge;
import hmi.tts.fluency8.*;
import hmi.util.StringUtil;
import saiba.bml.core.SpeechBehaviour;
import asap.bml.ext.fluency.FluencyBehaviour;
import asap.realizer.planunit.InvalidParameterException;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;
import asap.speechengine.ttsbinding.TTSBinding;

/**
 * Binds BML speech and fluency behaviors to bridges with a fluency ttsgenerator. 
 * @author Dennis Reidsma
 */
public class FluencyTTSBinding extends TTSBinding
{
    private final Fluency8TTSGenerator fluencyTTSGenerator;
    
    public FluencyTTSBinding()
    {
        this(null);
    }
    public FluencyTTSBinding(PhonemeToVisemeMapping ptvm)
    {
        fluencyTTSGenerator = new Fluency8TTSGenerator(ptvm);
        ttsGenerator = fluencyTTSGenerator;
        
        ttsBridgeMap.put(SpeechBehaviour.class,new BMLTTSBridge(fluencyTTSGenerator));
        ttsBridgeMap.put(FluencyBehaviour.class,new Fluency8TTSBridge(fluencyTTSGenerator));
        
        supportedBehaviours.add(FluencyBehaviour.class);
    }
    
    @Override
    public void setParameterValue(String parameter, String value)throws ParameterException
    {
        if(StringUtil.isNumeric(value))
        {
            setFloatParameterValue(parameter, Float.parseFloat(value));
        }
        throw new InvalidParameterException(parameter,value);
    }
    
    @Override
    public float getFloatParameterValue(String parameter)throws ParameterException
    {
        /*
        if(parameter.equals("rate"))
        {
            return fluencyTTSGenerator.getRate();
        }
        else if(parameter.equals("volume"))
        {
            return sapiTTSGenerator.getVolume();
        }
        */
        throw new ParameterNotFoundException(parameter);
    }
    
    @Override
    public String getParameterValue(String parameter)throws ParameterException
    {
        return ""+getFloatParameterValue(parameter);        
    }
    
    @Override
    public void setFloatParameterValue(String parameter, float value)throws ParameterException
    {
        /*
        if(parameter.equals("rate"))
        {
            sapiTTSGenerator.setRate((int)value);
        }
        else if(parameter.equals("volume"))
        {
            sapiTTSGenerator.setVolume((int)value);
        }
        else
        {
            */
        throw new ParameterNotFoundException(parameter);
    
    }

    @Override
    public void cleanup()
    {
        fluencyTTSGenerator.cleanup();
    }
}
