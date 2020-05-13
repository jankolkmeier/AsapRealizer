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
package asap.livemocapengine.inputs.loader;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.environmentbase.Environment;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import asap.realizer.AsapRealizer;
import asap.realizer.feedback.FeedbackManager;
import asap.realizerembodiments.JFrameEmbodiment;

/**
 * Unit tests for the KeyboardEulerInputLoader
 * @author Herwin
 *
 */
public class KeyboardEulerInputLoaderTest
{
    private AsapRealizer mockRealizer = mock(AsapRealizer.class);
    private FeedbackManager mockFbm = mock(FeedbackManager.class);
    private JFrameEmbodiment mockJFrameEmbodiment = mock(JFrameEmbodiment.class);
    @Before
    public void setup()
    {
        when(mockRealizer.getFeedbackManager()).thenReturn(mockFbm);
    }
    
    @Test
    public void testLoad() throws IOException
    {
        String str="<Loader id=\"l1\" loader=\"asap.livemocapengine.inputs.loader.KeyboardEulerInputLoader\"/>";
        KeyboardInputLoader loader = new KeyboardInputLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();        
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0], mockJFrameEmbodiment);
        assertNotNull(loader.getSensor());        
    }
}
