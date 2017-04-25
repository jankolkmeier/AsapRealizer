/*******************************************************************************
 *******************************************************************************/
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
