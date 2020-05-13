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
package asap.animationengine.ace.lmp;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.AfterPeg;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Unit tests for LMP
 * @author hvanwelbergen
 * 
 */
public class LMPTest
{
    private FeedbackManager fbm = NullFeedbackManager.getInstance();
    private BMLBlockPeg bbPeg = new BMLBlockPeg("bml1", 0);
    private PegBoard pegBoard = new PegBoard();

    @Test
    public void testSetTimePeg()
    {
        LMP lmp = new StubLMP(fbm, bbPeg, "bml1", "beh1", pegBoard, new HashSet<String>(), new HashSet<String>(), 1, 1, 2);
        TimePeg tp = TimePegUtil.createTimePeg(2);
        lmp.setTimePeg("start", tp);
        assertEquals(tp, lmp.getTimePeg("start"));
        assertEquals(tp, lmp.getStartPeg());
    }

    @Test
    public void testSetAfterTimePeg()
    {
        LMP lmp = new StubLMP(fbm, bbPeg, "bml1", "beh1", pegBoard, new HashSet<String>(), new HashSet<String>(), 1, 1, 2);
        TimePeg tp = TimePegUtil.createTimePeg(2);
        AfterPeg tpAfter = new AfterPeg(tp, 0, bbPeg);
        lmp.setTimePeg("start", tp);
        lmp.setTimePeg("start", tpAfter);
        assertEquals(tpAfter, lmp.getTimePeg("start"));
        assertEquals(tpAfter, lmp.getStartPeg());
    }
}
