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

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.BMLBlockManager;

/**
 * Unit test cases for SpeechBehaviour planning using a TextPlanner 
 * @author welberge
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class TextPlannerTest extends AbstractTextPlannerTest<TimedSpeechTextUnit>
{
    private TextOutput mockTextOutput = mock(TextOutput.class);
    private PlanManager<TimedSpeechTextUnit> planManager = new PlanManager<TimedSpeechTextUnit>();
    
    @Before
    public void setup()
    {
        speechPlanner = new TextPlanner(mockFeedbackManager,mockTextOutput, planManager);
        super.setup();
    }    
}
