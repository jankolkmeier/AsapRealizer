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

import static org.mockito.Mockito.mock;
import hmi.util.Clock;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.parser.BMLParser;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizerembodiments.impl.BMLSchedulerAssembler;

/**
 * Test cases to load some Asap-specific instantiations of an environment
 * @author hvanwelbergen
 */
public class EnvironmentLoadingTest
{
    private BMLBlockManager bbm = new BMLBlockManager();
    private PegBoard pegBoard = new PegBoard();
    private Clock mockClock = mock(Clock.class);

    @Test
    public void testLoadScheduler() throws IOException
    {
        BMLSchedulerAssembler asm = new BMLSchedulerAssembler("id1", new BMLParser(), NullFeedbackManager.getInstance(), bbm, mockClock,
                pegBoard);
        String str = "<BMLScheduler>" + "<SchedulingHandler class=\"asap.realizer.scheduler.BMLASchedulingHandler\" "
                + "schedulingStrategy=\"asap.realizer.scheduler.SortedSmartBodySchedulingStrategy\"/>" + "</BMLScheduler>";
        asm.readXML(new XMLTokenizer(str));
    }
}
