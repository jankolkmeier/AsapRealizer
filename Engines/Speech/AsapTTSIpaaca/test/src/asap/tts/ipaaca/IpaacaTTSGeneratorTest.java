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
package asap.tts.ipaaca;

import hmi.tts.TTSException;
import ipaaca.Initializer;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit tests for the IpaacaTTSGenerator, requires a running spread daemon and a running speech client
 * @author hvanwelbergen
 */
public class IpaacaTTSGeneratorTest
{
    static
    {
        Initializer.initializeIpaacaRsb();
    }
    private IpaacaTTSGenerator ttsGen = new IpaacaTTSGenerator(); 
    
    @Ignore
    @Test
    public void test() throws InterruptedException, TTSException
    {
        //System.out.println(ttsGen.speakBML("test<sync id=\"s1\"/> 1 2 3"));
        ttsGen.speakBMLToFile("test<sync id=\"s1\"/> 1 2 3 4","/tmp/test.wav");
        //ttsGen.speak("test 1 2 3");
        //ttsGen.getTiming("test 1 2 3");
        Thread.sleep(4000);
    }
}
