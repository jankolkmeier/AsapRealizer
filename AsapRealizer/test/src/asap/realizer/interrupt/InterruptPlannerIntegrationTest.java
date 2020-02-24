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
package asap.realizer.interrupt;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.parser.Constraint;
import asap.bml.ext.bmla.BMLAInfo;
import asap.bml.ext.bmla.BMLAInterruptBehaviour;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.DefaultPlayer;
import asap.realizer.Player;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.SingleThreadedPlanPlayer;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizer.scheduler.TimePegAndConstraint;

/**
 * Integration tests for the InterruptPlanner
 * 
 * @author welberge
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLScheduler.class)
public class InterruptPlannerIntegrationTest
{
    private InterruptPlanner interruptPlanner;

    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);
    
    private BMLScheduler mockScheduler = mock(BMLScheduler.class);
    
    private PlanManager<TimedInterruptUnit> planManager = new PlanManager<TimedInterruptUnit>();
    private Player player;
    
    private BMLAInterruptBehaviour createInteruptBehaviour(String bmlId, String bml) throws IOException
    {
        return new BMLAInterruptBehaviour(bmlId, new XMLTokenizer(bml));
    }

    @Before
    public void setup()
    {
        player = new DefaultPlayer(new SingleThreadedPlanPlayer<TimedInterruptUnit>(mockFeedbackManager, planManager));
        interruptPlanner = new InterruptPlanner(mockFeedbackManager, mockScheduler, planManager);
    }

    @Test
    public void testPlay() throws BehaviourPlanningException, IOException
    {
        BMLAInterruptBehaviour ipb = createInteruptBehaviour("bml1",
                "<interrupt xmlns=\""+BMLAInfo.BMLA_NAMESPACE+"\" target=\"bml3\" id=\"i1\"/>");
        ArrayList<TimePegAndConstraint> sac = new ArrayList<TimePegAndConstraint>();
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);

        sac.add(new TimePegAndConstraint("start", tp, new Constraint(), 0));
        TimedInterruptUnit p = interruptPlanner.resolveSynchs(BMLBlockPeg.GLOBALPEG, ipb, sac);
        interruptPlanner.addBehaviour(BMLBlockPeg.GLOBALPEG, ipb, sac, p);

        planManager.setBMLBlockState("bml1", TimedPlanUnitState.LURKING);
        when(mockScheduler.getBehaviours("bml3")).thenReturn(new HashSet<String>());

        player.play(0);
        assertEquals(TimedPlanUnitState.DONE, p.getState());
    }
}
