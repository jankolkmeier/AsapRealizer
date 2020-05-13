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
package asap.audioengine;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;

import org.junit.Test;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.planunit.MultiThreadedPlanPlayer;
import asap.realizer.planunit.PlanManager;

public class AudioPlanPlayerAudioUnitMockupTest
{
    private TimedAbstractAudioUnit mockPlanUnit1 = mock(TimedAbstractAudioUnit.class);
    private TimedAbstractAudioUnit mockPlanUnit2 = mock(TimedAbstractAudioUnit.class);
    private TimedAbstractAudioUnit mockPlanUnit3 = mock(TimedAbstractAudioUnit.class);
    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);

    @Test
    public void testInterruptUnit() throws InterruptedException
    {
        PlanManager<TimedAbstractAudioUnit> planManager = new PlanManager<TimedAbstractAudioUnit>();
        MultiThreadedPlanPlayer<TimedAbstractAudioUnit> app = new MultiThreadedPlanPlayer<TimedAbstractAudioUnit>(mockFeedbackManager,
                planManager);
        when(mockPlanUnit1.getEndTime()).thenReturn(3.0);
        when(mockPlanUnit1.getBMLId()).thenReturn("bml1");
        when(mockPlanUnit1.getId()).thenReturn("beh1");
        when(mockPlanUnit2.getStartTime()).thenReturn(1.0);
        when(mockPlanUnit2.getEndTime()).thenReturn(4.0);
        when(mockPlanUnit2.getBMLId()).thenReturn("bml1");
        when(mockPlanUnit2.getId()).thenReturn("beh2");
        when(mockPlanUnit3.getStartTime()).thenReturn(0.0);
        when(mockPlanUnit3.getEndTime()).thenReturn(5.0);
        when(mockPlanUnit3.getBMLId()).thenReturn("bml2");
        when(mockPlanUnit3.getBMLId()).thenReturn("beh3");

        planManager.addPlanUnit(mockPlanUnit1);
        planManager.addPlanUnit(mockPlanUnit2);
        planManager.addPlanUnit(mockPlanUnit3);
        assertEquals(3, app.getNumberOfPlanUnits());

        app.stopBehaviourBlock("bml1", 1.0);
        assertEquals(1, app.getNumberOfPlanUnits());
    }
}
