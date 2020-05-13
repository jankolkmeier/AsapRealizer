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
package asap.ipaacattsbinding.loader;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

import org.junit.Test;

/**
 * Unit tests for the IpaacaTTSBindingLoader
 * @author hvanwelbergen
 *
 */
public class IpaacaTTSBindingLoaderTest
{
    @Test
    public void test() throws IOException
    {
      //@formatter:off
        String bindingXML=
        "<Loader id=\"l1\" loader=\"asap.ipaacattsbinding.loader.IpaacaTTSBindingLoader\">"+
        "<PhonemeToVisemeMapping resources=\"Humanoids/shared/phoneme2viseme/\" filename=\"sampade2ikp.xml\"/>"+
        "<VisualProsodyAnalyzer type=\"OPENSMILE\"/>"+
        "</Loader>";            
        //@formatter:on
        IpaacaTTSBindingLoader loader = new IpaacaTTSBindingLoader();
        XMLTokenizer tok = new XMLTokenizer(bindingXML);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1" , new Environment[0], new Loader[0]);      
        assertNotNull(loader.getTTSBinding());        
    }
}
