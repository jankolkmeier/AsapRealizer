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
package asap.picture.swing;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.JComponentEmbodiment;
import hmi.jcomponentenvironment.loader.JComponentEmbodimentLoader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

/**
 * unit test for the JComponentPictureEmbodiment
 * @author Herwin
 * 
 */
public class JComponentPictureEmbodimentTest
{
    private JComponentEmbodimentLoader mockJel = mock(JComponentEmbodimentLoader.class);
    private JComponentEmbodiment mockEmbodiment = mock(JComponentEmbodiment.class);

    @Before
    public void setup()
    {
        when(mockJel.getEmbodiment()).thenReturn(mockEmbodiment);
    }

    @Test
    public void test() throws IOException
    {
        JComponentPictureEmbodiment jpe = new JComponentPictureEmbodiment();
        String str = "<Loader id=\"l1\"/>";
        XMLTokenizer tok = new hmi.xml.XMLTokenizer(str);
        tok.takeSTag();
        jpe.readXML(tok, "id1", "id1", "id1", new Environment[] {}, new Loader[] {mockJel});
        assertNotNull(jpe.getEmbodiment());
    }
}
