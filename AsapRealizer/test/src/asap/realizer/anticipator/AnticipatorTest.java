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
package asap.realizer.anticipator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;

/**
 * Unit tests for the anticipator
 * @author hvanwelbergen
 *
 */
public class AnticipatorTest
{
    private PegBoard pegBoard = new PegBoard();
    private static final String ANTICIPATOR_ID = "anticip1";
    private Anticipator anticip = new Anticipator(ANTICIPATOR_ID, pegBoard);
    private static final double PRECISION = 0.01;
    
    @Test
    public void testAdd()
    {
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);
        anticip.addSynchronisationPoint("test1", tp);
        assertEquals(tp, anticip.getSynchronisationPoint("test1"));
        assertEquals(tp, pegBoard.getTimePeg(BMLBlockPeg.ANTICIPATOR_PEG_ID,ANTICIPATOR_ID,"test1"));
    }
    
    @Test
    public void testUpdate()
    {
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);
        anticip.addSynchronisationPoint("test1", tp);
        anticip.setSynchronisationPoint("test1", 10);
        assertEquals(10, anticip.getSynchronisationPoint("test1").getGlobalValue(), PRECISION);
        assertEquals(10, tp.getGlobalValue(), PRECISION);        
    }
    
    @Test
    public void testgetTimePegs()
    {
        TimePeg tp1 = new TimePeg(BMLBlockPeg.GLOBALPEG);
        TimePeg tp2 = new TimePeg(BMLBlockPeg.GLOBALPEG);
        anticip.addSynchronisationPoint("test1", tp1);
        anticip.addSynchronisationPoint("test2", tp2);
        assertThat(anticip.getTimePegs(),IsIterableContainingInAnyOrder.containsInAnyOrder(tp1,tp2));
    }
}
