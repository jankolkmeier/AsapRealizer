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
package hmi.jnaoqiembodiment.loader;

import static org.junit.Assert.assertNotNull;
import hmi.environmentbase.Environment;
import hmi.util.OS;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Assume;
import org.junit.Test;

/**
 * Unit test for the NaoQiEmbodimentLoader
 * @author welberge
 * 
 */
public class NaoQiEmbodimentLoaderTest
{

    @Test
    public void test() throws IOException
    {
        // FIXME: make the NaoQiEmbodiment work in windows
        Assume.assumeTrue(OS.equalsOS(OS.WINDOWS));
        Assume.assumeTrue(System.getProperty("sun.arch.data.model")=="32"); 
        String str = "<Loader id=\"naoqiembodiment\" loader=\"hmi.jnaoqiembodiment.loader.NaoQiEmbodimentLoader\">"
                + "<naoqi ip=\"localhost\" port=\"10\"/>" + "</Loader>";
        NaoQiEmbodimentLoader loader = new NaoQiEmbodimentLoader();
        XMLTokenizer tok = new XMLTokenizer(str);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1", new Environment[0]);
        assertNotNull(loader.getEmbodiment());
    }
}
