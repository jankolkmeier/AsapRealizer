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
package asap.speechengine;

import hmi.util.OS;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.scheduler.BMLBlockManager;
import asap.sapittsbinding.SAPITTSBinding;

/**
 * Unit test cases for TimedWavTTSUnits that use a SAPITTSBinding
 * @author hvanwelbergen
 *
 */
@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*",
    "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
    "org.slf4j.*" })
@RunWith(PowerMockRunner.class)
@Ignore("There are some problems with (probably) fluency voices when accessed through SAPI")
@PrepareForTest(BMLBlockManager.class)
public class TimedWavTTSUnitSAPIIntegrationTest extends AbstractTimedWavTTSUnitTest
{
    private SAPITTSBinding sapiBinding; 
    @Before
    public void setup()
    {
        Assume.assumeTrue(OS.equalsOS(OS.WINDOWS));
        super.setup();        
        sapiBinding = new SAPITTSBinding();
        ttsBinding = sapiBinding;
        
    }
    
    @After
    public void tearDown()
    {
        if(sapiBinding!=null)
        {
            sapiBinding.cleanup();
        }
        super.tearDown();        
    }    
}
