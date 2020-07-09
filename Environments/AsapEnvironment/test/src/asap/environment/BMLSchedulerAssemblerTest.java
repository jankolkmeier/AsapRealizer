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
package asap.environment;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import hmi.util.Clock;

import org.junit.Test;

import saiba.bml.core.BehaviourBlock;
import saiba.bml.parser.BMLParser;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizer.scheduler.SchedulingHandler;
import asap.realizer.scheduler.SchedulingStrategy;
import asap.realizer.scheduler.SmartBodySchedulingStrategy;
import asap.realizerembodiments.impl.BMLSchedulerAssembler;

/**
 * Unit testcases for the BMLSchedulerAssembler
 * @author hvanwelbergen
 * 
 */
public class BMLSchedulerAssemblerTest
{
    private BMLParser mockParser = mock(BMLParser.class);
    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);
    private BMLBlockManager bmlBlockManager = new BMLBlockManager();
    private Clock mockSchedulingClock = mock(Clock.class);
    private static SchedulingHandler stubbedSchedulingHandler;
    private PegBoard pegBoard = new PegBoard();
    
    /**
     * SchedulingHandler test stub 
     * @author Herwin
     */
    public static class StubSchedulingHandler implements SchedulingHandler
    {
        private final SchedulingStrategy schedulingStrategy;

        public StubSchedulingHandler(SchedulingStrategy ss, PegBoard pb)
        {
            schedulingStrategy = ss;
            stubbedSchedulingHandler = this;
        }

        public SchedulingStrategy getSchedulingStrategy()
        {
            return schedulingStrategy;
        }

        @Override
        public void schedule(BehaviourBlock bb, BMLScheduler scheduler, double time)
        {
            
        }
    }

    @Test
    public void testSchedulingHandler()
    {

        BMLSchedulerAssembler assembler = new BMLSchedulerAssembler("x", mockParser, mockFeedbackManager, bmlBlockManager,
                mockSchedulingClock, pegBoard);
        String str = "<BMLScheduler>"
                + "<SchedulingHandler class=\"asap.environment.BMLSchedulerAssemblerTest$StubSchedulingHandler\""
                + " schedulingStrategy=\"asap.realizer.scheduler.SmartBodySchedulingStrategy\"/>" + "</BMLScheduler>";
        assembler.readXML(str);
        assertNotNull(stubbedSchedulingHandler);
        assertThat(stubbedSchedulingHandler, instanceOf(StubSchedulingHandler.class));
        assertThat( ((StubSchedulingHandler)stubbedSchedulingHandler).getSchedulingStrategy(), instanceOf(SmartBodySchedulingStrategy.class));
    }
}
