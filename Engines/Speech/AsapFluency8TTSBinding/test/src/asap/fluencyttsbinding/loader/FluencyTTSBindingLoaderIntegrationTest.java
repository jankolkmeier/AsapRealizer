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
package asap.fluencyttsbinding.loader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.tts.TTSException;
import hmi.tts.TTSTiming;
import hmi.tts.Visime;
import hmi.util.OS;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import saiba.bml.core.SpeechBehaviour;
import asap.fluencyttsbinding.loader.FluencyTTSBindingLoader;

/**
 * Integration tests for the FluencyTTSBinding
 * @author Dennis Reidsma
 *
 */
public class FluencyTTSBindingLoaderIntegrationTest
{
    @Before
    public void setup()
    {
        Assume.assumeTrue(OS.equalsOS(OS.WINDOWS));   
    }
    
    @Test
    public void test() throws IOException, InterruptedException, TTSException
    {
        //@formatter:off
        String bindingXML=
        "<Loader id=\"l1\" loader=\"asap.fluencyttsbinding.loader.FluencyTTSBindingLoader\"><PhonemeToVisemeMapping resources=\"visemes/\" filename=\"fluency2ikp.xml\"/></Loader>";
        //@formatter:on        
        FluencyTTSBindingLoader loader = new FluencyTTSBindingLoader();
        XMLTokenizer tok = new XMLTokenizer(bindingXML);
        tok.takeSTag();
        loader.readXML(tok, "id1", "id1", "id1" , new Environment[0], new Loader[0]);      
        assertNotNull(loader.getTTSBinding());
        TTSTiming ti = loader.getTTSBinding().speak(SpeechBehaviour.class,"blah blah test 1 2 3");
        assertThat(ti.getDuration(),greaterThan(0d));
        
        boolean hasVisemes = false;
        for(Visime v: ti.getVisimes())
        {
            if(v.getNumber()!=0)
            {
                hasVisemes = true;
            }
        }
        assertTrue("Failed to set up PhonemeToVisemeMapping",hasVisemes);
        Thread.sleep(4000);
    }
}
