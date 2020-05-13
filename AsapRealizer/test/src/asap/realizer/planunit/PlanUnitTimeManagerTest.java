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
package asap.realizer.planunit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import asap.realizer.SyncPointNotFoundException;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;

/**
 * PlanUnitTimeManager unit test cases
 * @author Herwin
 *
 */
public class PlanUnitTimeManagerTest
{
    PlanUnitTimeManager puTimeManager;
    KeyPositionManager kpManager;
    
    @Before
    public void setup()
    {
        kpManager = new KeyPositionManagerImpl();
        puTimeManager = new PlanUnitTimeManager(kpManager);
    }
    
    @Test
    public void testGetAvailableSyncs()
    {
        kpManager.addKeyPosition(new KeyPosition("start",0,1));
        kpManager.addKeyPosition(new KeyPosition("end",1,1));
        assertThat(puTimeManager.getAvailableSyncs(),contains("start","end"));
    }
    
    @Test
    public void testGetRelativeTime() throws SyncPointNotFoundException
    {
        kpManager.addKeyPosition(new KeyPosition("start",0,1));
        kpManager.addKeyPosition(new KeyPosition("stroke",0.4,1));
        assertEquals(0,puTimeManager.getRelativeTime("start"),0.01);
        assertEquals(0.4,puTimeManager.getRelativeTime("stroke"),0.01);
    }
    
    @Test
    public void testGetTimePeg()
    {
        KeyPosition kpStart = new KeyPosition("start",0,1);
        kpManager.addKeyPosition(kpStart);
        TimePeg tpStart = new TimePeg(BMLBlockPeg.GLOBALPEG);
        puTimeManager.setTimePeg(kpStart, tpStart);
        assertEquals(tpStart,puTimeManager.getTimePeg("start"));
    }
    
    @Test
    public void testGetUnsetEndTime()
    {
        assertEquals(TimePeg.VALUE_UNKNOWN,puTimeManager.getEndTime(),0.001f);
    }
    
    @Test
    public void testGetEndTime()
    {
        KeyPosition kpEnd = new KeyPosition("end",1,1);
        kpManager.addKeyPosition(kpEnd);
        TimePeg tpEnd = new TimePeg(BMLBlockPeg.GLOBALPEG);
        tpEnd.setGlobalValue(1);
        puTimeManager.setTimePeg(kpEnd, tpEnd);
        assertEquals(1,puTimeManager.getEndTime(),0.001f);
    }
}
