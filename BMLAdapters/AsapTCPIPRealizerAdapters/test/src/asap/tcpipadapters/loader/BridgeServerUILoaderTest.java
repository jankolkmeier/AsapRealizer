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
package asap.tcpipadapters.loader;

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
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.PipeLoader;
import asap.tcpipadapters.TCPIPToBMLRealizerAdapter;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the BridgeServerUILoader
 * @author Herwin
 * 
 */
@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*",
    "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
    "org.slf4j.*","javax.swing.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({TCPIPToBMLRealizerAdapter.class})
public class BridgeServerUILoaderTest
{
    private JComponentEmbodimentLoader mockJCompEmbLoader = mock(JComponentEmbodimentLoader.class);
    private JComponentEmbodiment mockJCompEmb = mock(JComponentEmbodiment.class);
    private AsapRealizerEmbodiment mockAsapRealizerEmb = mock(AsapRealizerEmbodiment.class);
    private TCPIPToBMLRealizerAdapterLoader mockTcpIpToBMLRealizerAdapterLoader = mock(TCPIPToBMLRealizerAdapterLoader.class);
    private TCPIPToBMLRealizerAdapter mockTcpIpToBMLRealizerAdapter = mock(TCPIPToBMLRealizerAdapter.class);

    @Before
    public void setup()
    {
        when(mockAsapRealizerEmb.getPipeLoaders()).thenReturn(
                new ImmutableList.Builder<PipeLoader>().add(mockTcpIpToBMLRealizerAdapterLoader).build());
        when(mockTcpIpToBMLRealizerAdapterLoader.getTcpIpAdapter()).thenReturn(mockTcpIpToBMLRealizerAdapter);
        when(mockTcpIpToBMLRealizerAdapter.getFeedbackPort()).thenReturn(6000);
        when(mockTcpIpToBMLRealizerAdapter.getRequestPort()).thenReturn(6001);
        when(mockJCompEmbLoader.getEmbodiment()).thenReturn(mockJCompEmb);
    }

    @Test
    public void testLoad() throws IOException
    {
        String loaderString = "<Loader id=\"id\" class=\"BridgeServerUILoader\"/>";
        XMLTokenizer tok = new XMLTokenizer(loaderString);
        tok.takeSTag("Loader");
        BridgeServerUILoader loader = new BridgeServerUILoader();
        loader.readXML(tok, "id1", "vhId", "vhName", new Environment[0], new Loader[] { mockJCompEmbLoader, mockAsapRealizerEmb });
        verify(mockJCompEmb).addJComponent(any(JComponent.class));        
    }
}
