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
package asap.textengine;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyDouble;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import saiba.bml.core.SpeechBehaviour;
import saiba.bml.feedback.BMLWarningFeedback;
import saiba.bml.parser.Constraint;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.DefaultPlayer;
import asap.realizer.Player;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.feedback.FeedbackManagerImpl;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.MultiThreadedPlanPlayer;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.TimePegAndConstraint;
import asap.realizerport.util.ListBMLFeedbackListener;

/**
 * Tests the combination of a TextPlanner, (default) player and 'real' TimedTextUnits
 * 
 * @author welberge
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BMLBlockManager.class, TimedSpeechTextUnit.class })
public class TextPlannerIntegrationTest
{
    TextOutput mockTextOutput = mock(TextOutput.class);
    private BMLBlockManager mockBmlBlockManager = mock(BMLBlockManager.class);
    private FeedbackManager fbManager = new FeedbackManagerImpl(mockBmlBlockManager, "character1");
    private PlanManager<TimedSpeechTextUnit> planManager = new PlanManager<TimedSpeechTextUnit>();

    @Test
    public void testPlayExceptionLeadsToBMLException() throws BehaviourPlanningException, InterruptedException, IOException,
            TimedPlanUnitPlayException
    {
        // checks if TextUnit failure properly appears as BMLWarningFeedback
        List<BMLWarningFeedback> exceptionList = new ArrayList<BMLWarningFeedback>();
        Player vp = new DefaultPlayer(new MultiThreadedPlanPlayer<TimedSpeechTextUnit>(fbManager, planManager));
        TextPlanner textP = new TextPlanner(fbManager, mockTextOutput, planManager);
        fbManager.addFeedbackListener(new ListBMLFeedbackListener.Builder().warningList(exceptionList).build());
        final BMLBlockPeg bbPeg = new BMLBlockPeg("Peg1", 0.3);

        SpeechBehaviour beh = new SpeechBehaviour("bml1", new XMLTokenizer("<speech xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\""
                + "id=\"speech1\"><text>Hello world</text></speech>"));
        ArrayList<TimePegAndConstraint> sacs = new ArrayList<TimePegAndConstraint>();
        TimePeg sp = new TimePeg(bbPeg);
        sp.setGlobalValue(1);

        sacs.add(new TimePegAndConstraint("start", sp, new Constraint(), 0, false));

        TimedSpeechTextUnit pu = textP.resolveSynchs(bbPeg, beh, sacs);

        TimedSpeechTextUnit spyPu = spy(pu);
        doThrow(new TimedPlanUnitPlayException("Play failed!", pu)).when(spyPu).play(anyDouble());

        spyPu.setState(TimedPlanUnitState.LURKING);
        textP.addBehaviour(bbPeg, beh, sacs, spyPu);
        assertEquals(1, spyPu.getStartTime(), 0.0001);
        assertThat(spyPu.getEndTime(), greaterThan(spyPu.getStartTime()));

        vp.play(0);
        assertThat(exceptionList, hasSize(0));

        vp.play(1.1);
        Thread.sleep(400);
        assertThat(exceptionList, hasSize(1));
    }

}
