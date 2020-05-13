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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.BMLGestureSync;
import asap.bml.ext.bmla.feedback.BMLABlockStatus;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.TimedPlanUnitState;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

/**
 * Unit testcases for the BMLBBlock
 * @author hvanwelbergen
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLScheduler.class)
public class BMLBBlockTest
{
    private static final String BLOCKID = "block1";
    private BMLScheduler mockScheduler = mock(BMLScheduler.class);
    private PegBoard pegBoard = new PegBoard();
    private final static ImmutableMap<String, TimedPlanUnitState> EMPTY_UPDATE_MAP = new ImmutableMap.Builder<String, TimedPlanUnitState>()
            .build();

    @Test
    public void testUpdate()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.LURKING);
        block.update(EMPTY_UPDATE_MAP, 0);
        verify(mockScheduler, times(1)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testNoUpdateInPending()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.PENDING);
        block.update(EMPTY_UPDATE_MAP, 0);
        verify(mockScheduler, times(0)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testNoUpdateWhenAppending()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard, Sets.newHashSet("bml2"), new ArrayList<String>(),
                new HashSet<String>());
        block.setState(TimedPlanUnitState.LURKING);
        block.update(ImmutableMap.of("bml2", TimedPlanUnitState.IN_EXEC), 0);
        verify(mockScheduler, times(0)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testFinishEmptyBlock()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.IN_EXEC);
        block.update(EMPTY_UPDATE_MAP, 0);
        assertEquals(TimedPlanUnitState.DONE, block.getState());
        verify(mockScheduler, times(1)).blockStopFeedback(BLOCKID, BMLABlockStatus.DONE, 0);
    }

    @Test
    public void testNotFinishNonEmptyBlock()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.IN_EXEC);
        when(mockScheduler.getBehaviours(BLOCKID)).thenReturn(Sets.newHashSet("beh1"));
        block.update(EMPTY_UPDATE_MAP, 0);
        assertEquals(TimedPlanUnitState.IN_EXEC, block.getState());
        verify(mockScheduler, times(0)).blockStopFeedback(BLOCKID, BMLABlockStatus.DONE, 0);
    }

    @Test
    public void testFinishNonEmptyBlock()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.IN_EXEC);
        when(mockScheduler.getBehaviours(BLOCKID)).thenReturn(Sets.newHashSet("beh1"));
        block.behaviorProgress("beh1", BMLGestureSync.END.getId());
        block.update(EMPTY_UPDATE_MAP, 0);
        assertEquals(TimedPlanUnitState.DONE, block.getState());
        verify(mockScheduler, times(1)).blockStopFeedback(BLOCKID, BMLABlockStatus.DONE, 0);
    }

    @Test
    public void testSubsiding()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.IN_EXEC);
        when(mockScheduler.getBehaviours(BLOCKID)).thenReturn(Sets.newHashSet("beh1"));
        block.behaviorProgress("beh1", BMLGestureSync.RELAX.getId());
        block.update(EMPTY_UPDATE_MAP, 0);
        assertEquals(TimedPlanUnitState.SUBSIDING, block.getState());
    }

    @Test
    public void testSubsidingToDone()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard);
        block.setState(TimedPlanUnitState.SUBSIDING);
        when(mockScheduler.getBehaviours(BLOCKID)).thenReturn(Sets.newHashSet("beh1"));
        block.behaviorProgress("beh1", BMLGestureSync.END.getId());
        block.update(EMPTY_UPDATE_MAP, 0);
        assertEquals(TimedPlanUnitState.DONE, block.getState());
        verify(mockScheduler, times(1)).blockStopFeedback(BLOCKID, BMLABlockStatus.DONE, 0);
    }

    @Test
    public void testChunk()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard, new HashSet<String>(), new ArrayList<String>(),
                Sets.newHashSet("bml2"));
        block.setState(TimedPlanUnitState.LURKING);
        block.update(ImmutableMap.of("bml2", TimedPlanUnitState.SUBSIDING), 0);
        verify(mockScheduler, times(1)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testChunkGone()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard, new HashSet<String>(), new ArrayList<String>(),
                Sets.newHashSet("bml2"));
        block.setState(TimedPlanUnitState.LURKING);
        block.update(EMPTY_UPDATE_MAP, 0);
        verify(mockScheduler, times(1)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testNotChunk()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard, new HashSet<String>(), new ArrayList<String>(),
                Sets.newHashSet("bml2"));
        block.setState(TimedPlanUnitState.LURKING);
        block.update(ImmutableMap.of("bml2", TimedPlanUnitState.IN_EXEC), 0);
        verify(mockScheduler, times(0)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testChunkAndAppend()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard, new HashSet<String>(), ImmutableList.of("bml3"),
                Sets.newHashSet("bml2"));
        block.setState(TimedPlanUnitState.LURKING);
        block.update(ImmutableMap.of("bml2", TimedPlanUnitState.SUBSIDING, "bml3", TimedPlanUnitState.DONE), 0);
        verify(mockScheduler, times(1)).startBlock(BLOCKID, 0);
    }

    @Test
    public void testChunkAndNoAppend()
    {
        BMLBBlock block = new BMLBBlock(BLOCKID, mockScheduler, pegBoard, new HashSet<String>(), ImmutableList.of("bml3"),
                Sets.newHashSet("bml2"));
        block.setState(TimedPlanUnitState.LURKING);
        block.update(ImmutableMap.of("bml2", TimedPlanUnitState.SUBSIDING, "bml3", TimedPlanUnitState.SUBSIDING), 0);
        verify(mockScheduler, times(1)).startBlock(BLOCKID, 0);
    }
}
