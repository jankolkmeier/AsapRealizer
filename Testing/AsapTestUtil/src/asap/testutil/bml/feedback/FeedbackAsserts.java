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
package asap.testutil.bml.feedback;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import saiba.bml.feedback.BMLBlockPredictionFeedback;
import saiba.bml.feedback.BMLSyncPointProgressFeedback;

/**
 * Custom asserts for BML feedbacks
 * @author welberge
 */
public final class FeedbackAsserts
{
    private static final double TIME_PRECISION = 0.01;
    private FeedbackAsserts()
    {
    }

    public static void assertEqualSyncPointProgress(BMLSyncPointProgressFeedback expected, BMLSyncPointProgressFeedback actual)
    {
        assertEquals(expected.getBMLId(), actual.getBMLId());
        assertEquals(expected.getBehaviourId(),actual.getBehaviourId());
        assertEquals(expected.getSyncId(), actual.getSyncId());
        assertEquals(expected.getTime(), actual.getTime(), TIME_PRECISION);
        assertEquals(expected.getGlobalTime(), actual.getGlobalTime(),TIME_PRECISION);
    }

    public static void assertEqualPlanningStart(BMLBlockPredictionFeedback expected, BMLBlockPredictionFeedback actual)
    {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGlobalStart(), actual.getGlobalStart(), TIME_PRECISION);
    }

    public static void assertEqualPlanningFinished(BMLBlockPredictionFeedback expected, BMLBlockPredictionFeedback actual)
    {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGlobalStart(), actual.getGlobalStart(), TIME_PRECISION);
        assertEquals(expected.getGlobalEnd(), actual.getGlobalEnd(), TIME_PRECISION);
    }

    public static void assertOneFeedback(BMLSyncPointProgressFeedback expected, List<BMLSyncPointProgressFeedback> actual)
    {
        assertTrue("Expected one BMLSyncPointProgressFeedback, got " + actual, 1 == actual.size());
        assertEqualSyncPointProgress(expected, actual.get(0));
    }

    public static void assertOneFeedback(BMLBlockPredictionFeedback expected, List<BMLBlockPredictionFeedback> actual)
    {
        assertTrue("Expected one BMLBlockPredictionFeedback, got " + actual, 1 == actual.size());
        assertEqualPlanningStart(expected, actual.get(0));
    }
}
