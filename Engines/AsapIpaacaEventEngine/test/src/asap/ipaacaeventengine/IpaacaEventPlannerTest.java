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
package asap.ipaacaeventengine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.parser.Constraint;
import asap.ipaacaeventengine.bml.IpaacaEventBehaviour;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.TimePegAndConstraint;
import asap.realizertestutil.PlannerTests;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Unit tests for the IpaacaEventPlanner
 * @author herwinvw
 *
 */
public class IpaacaEventPlannerTest
{
    private IpaacaEventPlanner planner;
    private FeedbackManager mockBmlFeedbackManager = mock(FeedbackManager.class);
    private MessageManager mockMessageManager = mock(MessageManager.class);
    private static final String BMLID = "bml1";
    private PlannerTests<TimedIpaacaMessageUnit> plannerTests;

    @Before
    public void setup()
    {
        planner = new IpaacaEventPlanner(mockBmlFeedbackManager, new PlanManager<TimedIpaacaMessageUnit>(), mockMessageManager);
        plannerTests = new PlannerTests<TimedIpaacaMessageUnit>(planner, new BMLBlockPeg(BMLID, 0.3));
    }
    
    private IpaacaEventBehaviour createEventBehaviour() throws IOException
    {
        return createMessageBehaviour("<ipaaca:ipaacaevent xmlns:ipaaca=\""+IpaacaEventBehaviour.NAMESPACE+"\" id=\"i1\">"
                + "<message category=\"cat1\">"
                + "</message>"
                + "</ipaacaevent>");
    }
    
    private IpaacaEventBehaviour createMessageBehaviour(String bml) throws IOException
    {
        return new IpaacaEventBehaviour(BMLID, new XMLTokenizer(bml));
    }
    
    @Test
    public void testResolveUnsetStart() throws BehaviourPlanningException, IOException
    {
        plannerTests.testResolveUnsetStart(createEventBehaviour());
    }
    
    @Test
    public void testResolveStartOffset() throws BehaviourPlanningException, IOException
    {
        plannerTests.testResolveStartOffset(createEventBehaviour());
    }
    
    @Test(expected=BehaviourPlanningException.class)
    public void testResolveNonExistingSync() throws IOException, BehaviourPlanningException
    {
        plannerTests.testResolveNonExistingSync(createEventBehaviour());
    }
    
    @Test
    public void testResolve() throws BehaviourPlanningException, IOException
    {
        IpaacaEventBehaviour ipb = createMessageBehaviour(
                "<ipaaca:ipaacaevent xmlns:ipaaca=\""+IpaacaEventBehaviour.NAMESPACE+"\" id=\"a1\">"
                        + "<message category=\"cat1\">"
                        + "</message>" 
                        + "</ipaaca:ipaacaevent>");
        ArrayList<TimePegAndConstraint> sac = new ArrayList<TimePegAndConstraint>();
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);
        sac.add(new TimePegAndConstraint("start", tp, new Constraint(), 0));
        planner.resolveSynchs(BMLBlockPeg.GLOBALPEG, ipb, sac);

        assertEquals(0, tp.getGlobalValue(), 0.0001);
    }
    
    @Test(expected = BehaviourPlanningException.class)
    public void testResolveWithUnknownSyncAndStart() throws IOException, BehaviourPlanningException
    {
        IpaacaEventBehaviour ipb = createMessageBehaviour(
                "<ipaaca:ipaacaevent xmlns:ipaaca=\""+IpaacaEventBehaviour.NAMESPACE+"\" id=\"a1\">"
                        + "<message category=\"cat1\">"
                        + "</message>" 
                        + "</ipaaca:ipaacaevent>");
        ArrayList<TimePegAndConstraint> sac = new ArrayList<TimePegAndConstraint>();
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);
        sac.add(new TimePegAndConstraint("unknown", tp, new Constraint(), 0));
        sac.add(new TimePegAndConstraint("start", TimePegUtil.createTimePeg(0), new Constraint(), 0));
        planner.resolveSynchs(BMLBlockPeg.GLOBALPEG, ipb, sac);
    }
    
    @Test
    public void testAdd() throws BehaviourPlanningException, IOException
    {
        IpaacaEventBehaviour ipb = createMessageBehaviour(
                "<ipaaca:ipaacaevent xmlns:ipaaca=\""+IpaacaEventBehaviour.NAMESPACE+"\" id=\"a1\">"
                        + "<message category=\"cat1\">"
                        + "</message>" 
                        + "</ipaaca:ipaacaevent>");
        ArrayList<TimePegAndConstraint> sac = new ArrayList<TimePegAndConstraint>();
        TimePeg tp = new TimePeg(BMLBlockPeg.GLOBALPEG);

        sac.add(new TimePegAndConstraint("start", tp, new Constraint(), 0));
        TimedIpaacaMessageUnit p = planner.resolveSynchs(BMLBlockPeg.GLOBALPEG, ipb, sac);
        List<SyncAndTimePeg> satp = planner.addBehaviour(BMLBlockPeg.GLOBALPEG, ipb, sac, p);
        assertEquals(1, satp.size());
        assertEquals(tp, satp.get(0).peg);
    }
}
