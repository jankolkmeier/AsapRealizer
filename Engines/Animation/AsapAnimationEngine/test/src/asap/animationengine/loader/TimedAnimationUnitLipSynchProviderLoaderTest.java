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

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import asap.realizerembodiments.AsapRealizerEmbodiment;

/**
 * Unit test cases for TimedAnimationUnitLipSynchProviderLoader
 * @author Herwin
 * 
 */
public class TimedAnimationUnitLipSynchProviderLoaderTest
{
    private MixedAnimationEngineLoader mockMixedAnimationEngineLoader = mock(MixedAnimationEngineLoader.class);
    private AsapRealizerEmbodiment mockAsapRealizerEmbodiment = mock(AsapRealizerEmbodiment.class);

    @Test
    public void test() throws IOException
    {
        TimedAnimationUnitLipSynchProviderLoader loader = new TimedAnimationUnitLipSynchProviderLoader();
        String str = "<Loader id=\"l1\"><SpeechBinding basedir=\"\" resources=\"Humanoids/shared/speechbinding/\" "
                + "filename=\"ikpspeechbinding.xml\"/></Loader>";
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0], new Loader[] { mockMixedAnimationEngineLoader,
                mockAsapRealizerEmbodiment });
        assertNotNull(loader.getLipSyncProvider());
    }
}
