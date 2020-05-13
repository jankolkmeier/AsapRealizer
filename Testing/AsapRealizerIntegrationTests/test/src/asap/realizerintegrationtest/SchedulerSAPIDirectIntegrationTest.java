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
package asap.realizerintegrationtest;

import hmi.testutil.LabelledParameterized;
import hmi.util.OS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import asap.realizer.feedback.FeedbackManagerImpl;
import asap.realizer.scheduler.BMLBlockManager;
import asap.sapittsbinding.SAPITTSBindingFactory;
import asap.speechengine.DirectTTSUnitFactory;

/**
 * Rapid test using TestSchedulerParameterized with only SAPI ttsgeneration
 * using direct output
 * 
 * @author welberge
 * 
 */
@RunWith(LabelledParameterized.class)
@Ignore("There are some problems with (probably) fluency voices when accessed through SAPI")
public class SchedulerSAPIDirectIntegrationTest extends SchedulerParameterizedIntegrationTest
{
     
    
    public SchedulerSAPIDirectIntegrationTest(String label, SpeechEngineFactory vp) throws IOException
    {
        super(label, vp);
    }

    @Parameters
    public static Collection<Object[]> configs() throws Exception
    {
        bbm = new BMLBlockManager();
        bfm = new FeedbackManagerImpl(bbm, "character1");

        ArrayList<SpeechEngineFactory> speechEngineFactories = new ArrayList<SpeechEngineFactory>();

        if (OS.equalsOS(OS.WINDOWS))
        {
            speechEngineFactories.add(new TTSEngineFactory(new DirectTTSUnitFactory(bfm), 
                    new SAPITTSBindingFactory(), soundManager));            
        }

        Collection<Object[]> objs = new ArrayList<Object[]>();

        for (SpeechEngineFactory sp : speechEngineFactories)
        {
            Object obj[] = new Object[2];

            obj[0] = "SpeechPlanner = " + sp.getType();
            obj[1] = sp;
            objs.add(obj);
        }
        return objs;
    }    
}
