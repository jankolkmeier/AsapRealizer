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
package asap.realizer.scheduler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hmi.util.SystemClock;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.parser.BMLParser;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.TimedPlanUnitState;

/**
 * Unit tests for managing BMLBBlocks
 * @author Herwin
 * 
 */
public class BMLBBlockManagerTest
{
    private PegBoard pegBoard = new PegBoard();
    private BMLBlockManager bbm = new BMLBlockManager();
    private BMLScheduler scheduler;

    @Before
    public void setup()
    {
        scheduler = new BMLScheduler("char1", new BMLParser(), NullFeedbackManager.getInstance(), new SystemClock(),
                new BMLASchedulingHandler(new SortedSmartBodySchedulingStrategy(pegBoard), pegBoard), bbm, pegBoard);
    }

    @Test
    public void testIsPendingOneBlock()
    {
        BMLBBlock bb1 = new BMLBBlock("bml1", scheduler, pegBoard);
        bb1.setState(TimedPlanUnitState.PENDING);
        bbm.addBMLBlock(bb1);
        assertTrue(bbm.isPending("bml1"));
    }

    @Test
    public void testIsPendingTwoBlocks()
    {
        BMLBBlock bb1 = new BMLBBlock("bml1", scheduler, pegBoard);
        BMLBBlock bb2 = new BMLBBlock("bml2", scheduler, pegBoard);
        bb1.setState(TimedPlanUnitState.LURKING);
        bb2.setState(TimedPlanUnitState.PENDING);
        bb1.addAppendTarget("bml2");
        bbm.addBMLBlock(bb1);
        bbm.addBMLBlock(bb2);
        assertTrue(bbm.isPending("bml1"));
    }

    @Test
    public void testIsPendingThreeBlocks()
    {
        BMLBBlock bb1 = new BMLBBlock("bml1", scheduler, pegBoard);
        BMLBBlock bb2 = new BMLBBlock("bml2", scheduler, pegBoard);
        BMLBBlock bb3 = new BMLBBlock("bml3", scheduler, pegBoard);
        bb1.setState(TimedPlanUnitState.LURKING);
        bb2.setState(TimedPlanUnitState.LURKING);
        bb3.setState(TimedPlanUnitState.PENDING);
        bb1.addAppendTarget("bml2");
        bb2.addChunkTarget("bml3");
        bbm.addBMLBlock(bb1);
        bbm.addBMLBlock(bb2);
        bbm.addBMLBlock(bb3);
        assertTrue(bbm.isPending("bml1"));
    }
    
    @Test(timeout=300)
    public void testIsPendingLoop()
    {
        BMLBBlock bb1 = new BMLBBlock("bml1", scheduler, pegBoard);
        BMLBBlock bb2 = new BMLBBlock("bml2", scheduler, pegBoard);
        bb1.setState(TimedPlanUnitState.LURKING);
        bb2.setState(TimedPlanUnitState.LURKING);
        bb1.addAppendTarget("bml2");
        bb2.addChunkTarget("bml1");
        bbm.addBMLBlock(bb1);
        bbm.addBMLBlock(bb2);
        assertFalse(bbm.isPending("bml1"));
    }
}
