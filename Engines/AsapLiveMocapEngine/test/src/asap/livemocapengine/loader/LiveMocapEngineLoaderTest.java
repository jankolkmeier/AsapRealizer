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
package asap.livemocapengine.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.SensorLoader;
import hmi.headandgazeembodiments.EulerHeadEmbodiment;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import asap.livemocapengine.inputs.EulerInput;
import asap.realizer.AsapRealizer;
import asap.realizer.feedback.FeedbackManager;
import asap.realizerembodiments.AsapRealizerEmbodiment;

/**
 * Unit test for the LiveMocapEngineLoader
 * @author welberge
 */
public class LiveMocapEngineLoaderTest
{
    private AsapRealizer mockRealizer = mock(AsapRealizer.class);
    private AsapRealizerEmbodiment mockRealizerEmbodiment = mock(AsapRealizerEmbodiment.class);
    private EmbodimentLoader mockArmandiaLoader = mock(EmbodimentLoader.class);
    private SensorLoader mockArrowEulerLoader = mock(SensorLoader.class);
    private FeedbackManager mockFbm = mock(FeedbackManager.class);
    private EulerInput mockEulerInput = mock(EulerInput.class);
    private EulerHeadEmbodiment mockEmbodiment = mock(EulerHeadEmbodiment.class);

    @Before
    public void setup()
    {
        when(mockArmandiaLoader.getId()).thenReturn("armandia");
        when(mockArrowEulerLoader.getId()).thenReturn("arroweuler");
        when(mockArrowEulerLoader.getSensor()).thenReturn(mockEulerInput);
        when(mockArmandiaLoader.getEmbodiment()).thenReturn(mockEmbodiment);
        when(mockRealizer.getFeedbackManager()).thenReturn(mockFbm);
        when(mockRealizerEmbodiment.getFeedbackManager()).thenReturn(mockFbm);
        when(mockRealizerEmbodiment.getEmbodiment()).thenReturn(mockRealizerEmbodiment);
    }

    @Test(timeout = 1000)
    public void test() throws IOException
    {
        LiveMocapEngineLoader loader = new LiveMocapEngineLoader();
        String str = "<Loader id=\"livemocapengine\" loader=\"asap.livemocapengine.loader.LiveMocapEngineLoader\">"
                + "<input name=\"arroweuler\" interface=\"asap.livemocapengine.inputs.EulerInput\"/>"
                + "<output name=\"armandia\" interface=\"hmi.headandgazeembodiments.EulerHeadEmbodiment\"/>" + "</Loader>";
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0], mockArmandiaLoader, mockArrowEulerLoader, mockRealizerEmbodiment);
        assertNotNull(loader.getEngine());
    }
}
