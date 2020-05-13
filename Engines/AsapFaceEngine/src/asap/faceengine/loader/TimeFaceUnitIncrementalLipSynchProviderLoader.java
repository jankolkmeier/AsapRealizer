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
package asap.faceengine.loader;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import asap.faceengine.lipsync.TimedFaceUnitIncrementalLipSynchProvider;
import asap.faceengine.viseme.VisemeBinding;
import asap.realizer.lipsync.IncrementalLipSynchProvider;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.IncrementalLipSynchProviderLoader;

/**
 * Loader for TimeFaceUnitIncrementalLipSynchProvider
 * @author hvanwelbergen
 */
public class TimeFaceUnitIncrementalLipSynchProviderLoader implements IncrementalLipSynchProviderLoader
{
    private IncrementalLipSynchProvider lipSyncProvider;
    private String id;
    
    
    @Override
    public IncrementalLipSynchProvider getLipSyncProvider()
    {
        return lipSyncProvider;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        
        AsapRealizerEmbodiment are = ArrayUtils.getFirstClassOfType(requiredLoaders, AsapRealizerEmbodiment.class);
        if (are == null)
        {
            throw new RuntimeException(
                    "TimeFaceUnitIncrementalLipSynchProviderLoader requires an EmbodimentLoader containing a AsapRealizerEmbodiment");
        }
        
        FaceEngineLoader fal = ArrayUtils.getFirstClassOfType(requiredLoaders, FaceEngineLoader.class);
        if (fal == null)
        {
            throw tokenizer.getXMLScanException("TimedFaceUnitLipSynchProviderLoader requires FaceEngineLoader.");
        }
        
        VisemeBinding visBinding = VisemeBindingLoader.load(tokenizer, fal.getFACSConverter());
        
        if (visBinding == null)
        {
            throw tokenizer.getXMLScanException("TimedFaceUnitIncrementalLipSynchProvider requires a visimebinding.");
        }

        lipSyncProvider = new TimedFaceUnitIncrementalLipSynchProvider(visBinding, fal.getFaceController(), fal.getPlanManager(), are.getPegBoard());
        
    }

    @Override
    public void unload()
    {
                
    }
}
