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
package asap.realizer.feedback;

import java.util.List;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import saiba.bml.feedback.BMLWarningFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockProgressFeedback;
import asap.bml.ext.bmla.feedback.BMLAPredictionFeedback;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizerport.BMLFeedbackListener;

import com.google.common.collect.ImmutableSet;

/**
 * Interface for classes that manage and keep track of BML feedback
 * @author Herwin
 *
 */
public interface FeedbackManager
{
    void addFeedbackListener(BMLFeedbackListener fb);

    void feedback(BMLSyncPointProgressFeedback fb);
    
    /**
     * Send a list of feedback (in list-order) to the BMLFeedbackListeners. The listeners will
     * receive all feedbacks in the list before any subsequent feedback is sent using the feedback
     * functions.
     */
    void feedback(List<BMLSyncPointProgressFeedback> fbs);
    
    void removeAllFeedbackListeners();
    
    void removeFeedbackListener(BMLFeedbackListener fb);
    
    ImmutableSet<String> getSyncsPassed(String bmlId, String behaviorId);

    void blockProgress(BMLABlockProgressFeedback psf);
    
    void puException(TimedPlanUnit timedMU, String message, double time);
    
    void prediction(BMLAPredictionFeedback bpsf);        

    void warn(BMLWarningFeedback w, double time);
}
