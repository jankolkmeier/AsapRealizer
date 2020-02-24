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
package asap.faceengine.lipsync;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.util.Resources;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import asap.faceengine.loader.FaceEngineLoader;
import asap.faceengine.loader.VisemeBindingLoader;
import asap.faceengine.viseme.MorphVisemeBinding;
import asap.faceengine.viseme.VisemeBinding;
import asap.realizer.lipsync.LipSynchProvider;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.LipSynchProviderLoader;

public class CoarticulationLipSyncProviderLoader implements LipSynchProviderLoader
{
    private String id;
    private LipSynchProvider lipSyncProvider;
    
    @Override
    public String getId()
    {
        return id;
    }
    
    public void setId(String newId)
    {
        id = newId;
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        setId(loaderId);

        FaceEngineLoader fal = ArrayUtils.getFirstClassOfType(requiredLoaders, FaceEngineLoader.class);
        if (fal == null)
        {
            throw tokenizer.getXMLScanException("CoarticulationLipSyncProvider requires FaceEngineLoader.");
        }
        
        AsapRealizerEmbodiment are = ArrayUtils.getFirstClassOfType(requiredLoaders, AsapRealizerEmbodiment.class);
        if (are == null)
        {
            throw new RuntimeException(
                    "CoarticulationLipSyncProvider requires an EmbodimentLoader containing a AsapRealizerEmbodiment");
        }
        
        Map<String, DominanceParameters> dps = null;
        Map<String, String> pc = null;
        Map<String, Double> mmm = null; 
        VisemeBinding visBinding = null;
        
        while (!tokenizer.atETag("Loader"))
        {
            if (VisemeBindingLoader.isAtVisemeBindingTag(tokenizer))
            {
                visBinding = VisemeBindingLoader.load(tokenizer, fal.getFACSConverter());
            }
            else if(tokenizer.atSTag("DominanceParameters"))
            {
                DominanceParameterLoader dpl = new DominanceParameterLoader();
                dpl.readXML(getResource(tokenizer,"DominanceParameters"));
                dps = dpl.getParameterMappings();
            }
            else if(tokenizer.atSTag("PhonemeClasses"))
            {
                SoundClassLoader scl = new SoundClassLoader();
                scl.readXML(getResource(tokenizer,"PhonemeClasses"));
                pc = scl.getSoundClassMappings();
            }
            else if(tokenizer.atSTag("PhonemeMagnitudes"))
            {
                MagnitudeLoader ml = new MagnitudeLoader();
                ml.readXML(getResource(tokenizer,"PhonemeMagnitudes"));
                mmm = ml.getMagnitudeModifierMappings();
            }
            else
            {
                throw new XMLScanException(tokenizer.getTagName() + " not a valid tag in TimedFaceUnitLipSynchProvider");
            }
        }
        
        
        
        lipSyncProvider = new CoarticulationLipSyncProvider(dps, pc, mmm, (MorphVisemeBinding)visBinding, fal.getFaceController(), fal.getPlanManager(), are.getPegBoard());
    }

    private Reader getResource(XMLTokenizer tok, String tag) throws IOException
    {
        String res = tok.getAttribute("resources");
        String filename = tok.getAttribute("filename");
        tok.takeSTag(tag);
        tok.takeETag();
        return new Resources(res).getReader(filename);
    }
    
    @Override
    public void unload()
    {
        
        
    }

    @Override
    public LipSynchProvider getLipSyncProvider()
    {
        return lipSyncProvider;
    }
    
}
