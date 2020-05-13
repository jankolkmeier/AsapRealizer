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
package asap.animationengine.loader;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import asap.animationengine.gesturebinding.SpeechBinding;
import asap.animationengine.lipsync.TimedAnimationUnitLipSynchProvider;
import asap.realizer.lipsync.LipSynchProvider;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.LipSynchProviderLoader;

/**
 * Loader for TimedAnimationUnitLipSynchProviderLoader
 * @author Herwin
 * 
 */
public class TimedAnimationUnitLipSynchProviderLoader implements LipSynchProviderLoader
{
    private String id;
    private LipSynchProvider lipSyncProvider;

    public void setId(String newId)
    {
        id = newId;
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
        setId(loaderId);
        AsapRealizerEmbodiment are = null;
        MixedAnimationEngineLoader ael = null;
        for (Loader e : requiredLoaders)
        {
            if (e instanceof MixedAnimationEngineLoader) ael = (MixedAnimationEngineLoader) e;
            if (e instanceof AsapRealizerEmbodiment) are = (AsapRealizerEmbodiment) e;
        }
        if (ael == null)
        {
            throw tokenizer.getXMLScanException("TimedAnimationUnitLipSynchProviderLoader requires mixedanimationenvironment.");
        }
        if (are == null)
        {
            throw new RuntimeException(
                    "TimedAnimationUnitLipSynchProviderLoader requires an EmbodimentLoader containing a AsapRealizerEmbodiment");
        }
        SpeechBinding sb = SpeechBindingLoader.load(tokenizer);
        if (sb == null)
        {
            throw tokenizer.getXMLScanException("TimedAnimationUnitLipSynchProviderLoaderTest requires a speechbinding.");
        }
        lipSyncProvider = new TimedAnimationUnitLipSynchProvider(sb, ael.getAnimationPlayer(), ael.getPlanManager(), are.getPegBoard());
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
