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
package asap.ipaacaeventengine.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import org.junit.Before;
import org.junit.Test;

import asap.realizer.Engine;
import asap.realizerembodiments.AsapRealizerEmbodiment;

/**
 * Unit tests for the IpaacaEventEngineLoader
 * @author hvanwelbergen
 *
 */
public class IpaacaEventEngineLoaderTest
{
    private EmbodimentLoader mockEmbodimentLoader = mock(EmbodimentLoader.class);
    private AsapRealizerEmbodiment  mockAsapRealizerEmbodiment = mock(AsapRealizerEmbodiment.class);
    
    @Before
    public void setup()
    {
        when(mockAsapRealizerEmbodiment.getEmbodiment()).thenReturn(mockAsapRealizerEmbodiment);
    }
    
    @Test
    public void test() throws IOException
    {
        String loaderStr = "<Loader id=\"ipaacaevent\" loader=\"asap.ipaacaeventengine.loader.IpaacaEventEngineLoader\"/>";                
        IpaacaEventEngineLoader loader = new IpaacaEventEngineLoader();
        XMLTokenizer tok = new XMLTokenizer(loaderStr);
        tok.takeSTag();
        loader.readXML(tok, "pa1", "billie", "billie", new Environment[0],
                new Loader[] { mockEmbodimentLoader, mockAsapRealizerEmbodiment });
        verify(mockAsapRealizerEmbodiment).addEngine(any(Engine.class));
        assertNotNull(loader.getEngine());
    }
}
