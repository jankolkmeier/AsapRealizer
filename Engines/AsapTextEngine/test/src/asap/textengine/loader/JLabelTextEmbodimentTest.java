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
package asap.textengine.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.JComponentEmbodiment;
import hmi.jcomponentenvironment.loader.JComponentEmbodimentLoader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import javax.swing.JLabel;

import org.junit.Test;

import asap.textengine.JLabelTextEmbodiment;

/**
 * Unit tests for the JLabelTextEmbodiment
 * @author hvanwelbergen
 *
 */
public class JLabelTextEmbodimentTest
{
    private JComponentEmbodiment mockComponentEmbodiment = mock(JComponentEmbodiment.class);
    private JComponentEmbodimentLoader mockJComponenentEmbodimentLoader = mock(JComponentEmbodimentLoader.class);
    
    @Test
    public void test() throws IOException, InterruptedException
    {
        when(mockJComponenentEmbodimentLoader.getEmbodiment()).thenReturn(mockComponentEmbodiment);
        String loaderStr = "<Loader id=\"pictureengine\" loader=\"asap.textengine.loader.JLabelTextEmbodiment\"/>";
        JLabelTextEmbodiment loader = new JLabelTextEmbodiment();
        XMLTokenizer tok = new XMLTokenizer(loaderStr);
        tok.takeSTag();
        loader.readXML(tok, "pa1", "billie", "billie", new Environment[0], new Loader[]{mockJComponenentEmbodimentLoader});
        Thread.sleep(500);
        verify(mockComponentEmbodiment).addJComponent(any(JLabel.class));
    }
}
