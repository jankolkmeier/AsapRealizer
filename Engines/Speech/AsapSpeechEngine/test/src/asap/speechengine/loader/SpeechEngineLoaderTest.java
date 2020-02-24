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
package asap.speechengine.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import hmi.audioenvironment.AudioEnvironment;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import org.junit.Before;
import org.junit.Test;

import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.speechengine.ttsbinding.TTSBindingLoader;

/**
 * Unit tests for the SpeechEngineLoader
 * @author hvanwelbergen
 *
 */
public class SpeechEngineLoaderTest
{
    private SpeechEngineLoader loader = new SpeechEngineLoader();
    private TTSBindingLoader mockTTSBindingLoader = mock(TTSBindingLoader.class);
    private AudioEnvironment mockAudioEnvironment = mock(AudioEnvironment.class);
    private AsapRealizerEmbodiment mockAsapRealizerEmbodiment = mock(AsapRealizerEmbodiment.class);
    private Loader[] reqLoaders = new Loader[] { mockAsapRealizerEmbodiment, mockTTSBindingLoader };
    private Environment reqEnvironments[] = new Environment[] { mockAudioEnvironment };

    @Before
    public void setup() 
    {
        when(mockAsapRealizerEmbodiment.getEmbodiment()).thenReturn(mockAsapRealizerEmbodiment);
    }
    
    @Test
    public void test() throws IOException
    {
        //@formatter:off
        String loaderStr =
          "<Loader id=\"speechengine\""+ 
                "loader=\"asap.speechengine.loader.SpeechEngineLoader\""+
                "requiredloaders=\"ttsbinding\">"+
            "<Voice factory=\"WAV_TTS\"/>"+
          "</Loader>";          
        //@formatter:on
        XMLTokenizer tok = new XMLTokenizer(loaderStr);
        tok.takeSTag();
        loader.readXML(tok, "ma1", "billie", "billie", reqEnvironments, reqLoaders);
        assertNotNull(loader.getEngine());
    }
}
