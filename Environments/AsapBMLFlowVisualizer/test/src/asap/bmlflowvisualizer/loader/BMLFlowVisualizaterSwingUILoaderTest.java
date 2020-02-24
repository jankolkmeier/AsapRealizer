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
package asap.bmlflowvisualizer.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.JComponentEmbodiment;
import hmi.jcomponentenvironment.loader.JComponentEmbodimentLoader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import javax.swing.JComponent;

import org.junit.Before;
import org.junit.Test;

import saiba.bmlflowvisualizer.BMLFlowVisualizerPort;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.PipeLoader;

import com.google.common.collect.ImmutableList;

/**
 * AsapBMLFlowVisualizerLoader
 * @author hvanwelbergen
 */
public class BMLFlowVisualizaterSwingUILoaderTest
{
    private JComponentEmbodimentLoader mockJCompEmbLoader = mock(JComponentEmbodimentLoader.class);
    private JComponentEmbodiment mockJCompEmb = mock(JComponentEmbodiment.class);
    private AsapRealizerEmbodiment mockAsapRealizerEmb = mock(AsapRealizerEmbodiment.class);
    private BMLFlowVisualizerPort mockVisPort = mock(BMLFlowVisualizerPort.class);
    private AsapBMLFlowVisualizerPortLoader mockVisPortLoader = mock(AsapBMLFlowVisualizerPortLoader.class);
    
    @Before
    public void setup()
    {
        when(mockVisPortLoader.getAdaptedRealizerPort()).thenReturn(mockVisPort);
        when(mockAsapRealizerEmb.getPipeLoaders()).thenReturn(
                new ImmutableList.Builder<PipeLoader>().add(mockVisPortLoader).build());
        when(mockJCompEmbLoader.getEmbodiment()).thenReturn(mockJCompEmb);
    } 
    
    @Test
    public void test() throws IOException
    {
        String loaderString = "<Loader id=\"id\" class=\"BMLFlowVisualizaterSwingUILoader\"/>";
        XMLTokenizer tok = new XMLTokenizer(loaderString);
        tok.takeSTag("Loader");
        BMLFlowVisualizaterSwingUILoader loader = new BMLFlowVisualizaterSwingUILoader();
        loader.readXML(tok, "id1", "vhId", "vhName", new Environment[0], new Loader[] { mockJCompEmbLoader, mockAsapRealizerEmb });
        verify(mockJCompEmb).addJComponent(any(JComponent.class));
    }
}
