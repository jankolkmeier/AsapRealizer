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
package asap.realizer.wait;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mock;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.core.WaitBehaviour;
import saiba.bml.parser.Constraint;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.scheduler.TimePegAndConstraint;
import asap.realizertestutil.PlannerTests;

/**
 * WaitPlanner Unit test cases
 * @author Herwin
 */
public class WaitPlannerTest
{
    private WaitPlanner waitPlanner;
    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);
    private PlannerTests<TimedWaitUnit> plannerTests;
    private static final String BMLID = "bml1";
    private static final float RESOLVE_TIME_PRECISION = 0.0001f;

    @Before
    public void setup()
    {
        waitPlanner = new WaitPlanner(mockFeedbackManager, new PlanManager<TimedWaitUnit>());
        plannerTests = new PlannerTests<TimedWaitUnit>(waitPlanner, new BMLBlockPeg(BMLID, 0.3));
    }

    public WaitBehaviour createWaitBehaviour(String bml) throws IOException
    {
        return new WaitBehaviour(BMLID, new XMLTokenizer(bml));
    }

    public WaitBehaviour createWaitBehaviour() throws IOException
    {
        return createWaitBehaviour("<wait xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"" + "id=\"w1\" max-wait=\"10\"/>");
    }

    @Test
    public void testResolveUnsetStart() throws BehaviourPlanningException, IOException
    {
        plannerTests.testResolveUnsetStart(createWaitBehaviour());
    }

    @Test
    public void testResolveStartOffset() throws BehaviourPlanningException, IOException
    {
        plannerTests.testResolveStartOffset(createWaitBehaviour());
    }

    @Test(expected = BehaviourPlanningException.class)
    public void testResolveNonExistingSync() throws IOException, BehaviourPlanningException
    {
        plannerTests.testResolveNonExistingSync(createWaitBehaviour());
    }

    @Test
    public void testResolve1() throws BehaviourPlanningException, IOException
    {
        WaitBehaviour wb = createWaitBehaviour("<wait xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " + "id=\"w1\" max-wait=\"10\"/>");
        ArrayList<TimePegAndConstraint> sacs = new ArrayList<TimePegAndConstraint>();
        TimePeg startPeg = new TimePeg(BMLBlockPeg.GLOBALPEG);
        sacs.add(new TimePegAndConstraint("start", startPeg, new Constraint(), 0));

        TimedPlanUnit pu = waitPlanner.resolveSynchs(BMLBlockPeg.GLOBALPEG, wb, sacs);

        assertEquals("bml1", pu.getBMLId());
        assertEquals("w1", pu.getId());
        assertEquals(0, pu.getStartTime(), RESOLVE_TIME_PRECISION);
        assertEquals(10, pu.getEndTime(), RESOLVE_TIME_PRECISION);
        assertEquals(0, startPeg.getGlobalValue(), RESOLVE_TIME_PRECISION);
    }

    @Test
    public void testResolve2() throws BehaviourPlanningException, IOException
    {
        WaitBehaviour wb = createWaitBehaviour("<wait xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" max-wait=\"1\" id=\"w1\"/>");
        ArrayList<TimePegAndConstraint> sacs = new ArrayList<TimePegAndConstraint>();
        TimePeg startPeg = new TimePeg(BMLBlockPeg.GLOBALPEG);
        sacs.add(new TimePegAndConstraint("start", startPeg, new Constraint(), 0));

        TimedPlanUnit pu = waitPlanner.resolveSynchs(BMLBlockPeg.GLOBALPEG, wb, sacs);

        assertEquals("bml1", pu.getBMLId());
        assertEquals("w1", pu.getId());
        assertEquals(0, pu.getStartTime(), RESOLVE_TIME_PRECISION);
        assertEquals(1, pu.getEndTime(), RESOLVE_TIME_PRECISION);
        assertEquals(0, startPeg.getGlobalValue(), RESOLVE_TIME_PRECISION);
    }

    @Test
    public void testResolve3() throws BehaviourPlanningException, IOException
    {
        WaitBehaviour wb = createWaitBehaviour("<wait xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"w1\"/>");
        ArrayList<TimePegAndConstraint> sacs = new ArrayList<TimePegAndConstraint>();
        TimePeg startPeg = new TimePeg(BMLBlockPeg.GLOBALPEG);
        startPeg.setGlobalValue(3);
        sacs.add(new TimePegAndConstraint("start", startPeg, new Constraint(), 0));
        TimePeg endPeg = new TimePeg(BMLBlockPeg.GLOBALPEG);
        endPeg.setGlobalValue(5);
        sacs.add(new TimePegAndConstraint("end", endPeg, new Constraint(), 0));

        TimedPlanUnit pu = waitPlanner.resolveSynchs(BMLBlockPeg.GLOBALPEG, wb, sacs);

        assertEquals("bml1", pu.getBMLId());
        assertEquals("w1", pu.getId());
        assertEquals(3, pu.getStartTime(), RESOLVE_TIME_PRECISION);
        assertEquals(5, pu.getEndTime(), RESOLVE_TIME_PRECISION);
        assertEquals(3, startPeg.getGlobalValue(), RESOLVE_TIME_PRECISION);
        assertEquals(5, endPeg.getGlobalValue(), RESOLVE_TIME_PRECISION);
    }
    
    @Test
    public void testAddWithStartConstraint() throws BehaviourPlanningException, IOException
    {
        WaitBehaviour wb = createWaitBehaviour("<wait xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" max-wait=\"1\" id=\"w1\"/>");
        ArrayList<TimePegAndConstraint> sacs = new ArrayList<TimePegAndConstraint>();
        TimePeg startPeg = new TimePeg(BMLBlockPeg.GLOBALPEG);
        sacs.add(new TimePegAndConstraint("start", startPeg, new Constraint(), 0));
        TimedWaitUnit pu = waitPlanner.resolveSynchs(BMLBlockPeg.GLOBALPEG, wb, sacs);
        List<SyncAndTimePeg> syncAndTimePeg = waitPlanner.addBehaviour(BMLBlockPeg.GLOBALPEG, wb, sacs, pu);
        assertEquals(2, syncAndTimePeg.size());
        assertEquals("start", syncAndTimePeg.get(0).sync);
        assertEquals("end", syncAndTimePeg.get(1).sync);
        assertEquals(0, syncAndTimePeg.get(0).peg.getGlobalValue(), RESOLVE_TIME_PRECISION);
        assertEquals(1, syncAndTimePeg.get(1).peg.getGlobalValue(), RESOLVE_TIME_PRECISION);
    }
    
    @Test
    public void testAddEndStartConstraint() throws BehaviourPlanningException, IOException
    {
        WaitBehaviour wb = createWaitBehaviour("<wait xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" max-wait=\"2\" id=\"w1\"/>");
        ArrayList<TimePegAndConstraint> sacs = new ArrayList<TimePegAndConstraint>();
        TimePeg endPeg = new TimePeg(BMLBlockPeg.GLOBALPEG);
        sacs.add(new TimePegAndConstraint("end", endPeg, new Constraint(), 0));
        TimedWaitUnit pu = waitPlanner.resolveSynchs(BMLBlockPeg.GLOBALPEG, wb, sacs);
        List<SyncAndTimePeg> syncAndTimePeg = waitPlanner.addBehaviour(BMLBlockPeg.GLOBALPEG, wb, sacs, pu);
        assertEquals(2, syncAndTimePeg.size());
        assertEquals("start", syncAndTimePeg.get(0).sync);
        assertEquals("end", syncAndTimePeg.get(1).sync);
        assertEquals(0, syncAndTimePeg.get(0).peg.getGlobalValue(), RESOLVE_TIME_PRECISION);
        assertEquals(2, syncAndTimePeg.get(1).peg.getGlobalValue(), RESOLVE_TIME_PRECISION);
    }
}
