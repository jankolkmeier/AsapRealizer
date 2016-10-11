/*******************************************************************************
 *******************************************************************************/
package asap.fluencyttsbinding.loader;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.*;
import hmi.util.*;
import java.util.*;

import java.io.IOException;

import asap.fluencyttsbinding.FluencyTTSBinding;
import hmi.tts.fluency8.*;
import hmi.tts.*;
import hmi.tts.util.*;
import asap.speechengine.ttsbinding.TTSBindingLoader;

/**
 * XML loader for the FluencyTTSBinding
 * @author Dennis Reidsma
 * 
 */
public class FluencyTTSBindingLoader implements TTSBindingLoader
{
    XMLStructureAdapter adapter = new XMLStructureAdapter();
    private String id;
    private FluencyTTSBinding binding;

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        id = loaderId;        
        PhonemeToVisemeMapping mapping = new NullPhonemeToVisemeMapping();
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(FluencyPhonemeToVisemeMapping.xmlTag()))
            {
                HashMap<String, String> attrMap = tokenizer.getAttributes();
                String resources = adapter.getRequiredAttribute("resources", attrMap, tokenizer);
                String filename = adapter.getRequiredAttribute("filename", attrMap, tokenizer);
                FluencyPhonemeToVisemeMapping fmapping = new FluencyPhonemeToVisemeMapping();
                try
                {
                    fmapping.readXML(new Resources(resources).getReader(filename));
                }
                catch (IOException e)
                {
                    XMLScanException ex = new XMLScanException(e.getMessage());
                    ex.initCause(e);
                    throw ex;
                }
                mapping = fmapping;
                tokenizer.takeEmptyElement(FluencyPhonemeToVisemeMapping.xmlTag());
            }
            else
            {
                throw new XMLScanException("Invalid tag " + tag);
            }
        }
        
        binding = new FluencyTTSBinding(mapping);        
    }

    @Override
    public void unload()
    {
        if (binding != null)
        {
            binding.cleanup();
        }
    }

    @Override
    public FluencyTTSBinding getTTSBinding()
    {
        return binding;
    }

}
