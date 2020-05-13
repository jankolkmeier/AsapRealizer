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
 * Null implementation of the FeedbackManager, ignores all feedback. 
 * Useful for building TimedPlanUnits that are executed outside a full blown Elckerlyc environment.
 * @author Herwin
 */
public final class NullFeedbackManager implements FeedbackManager
{
    private static final NullFeedbackManager NULL_MANAGER = new NullFeedbackManager();
    private NullFeedbackManager(){}
    
    public static NullFeedbackManager getInstance()
    {
        return NULL_MANAGER;
    }
    
    @Override
    public void addFeedbackListener(BMLFeedbackListener fb){}

	@Override
	public void feedback(BMLSyncPointProgressFeedback fb){}

	@Override
	public void feedback(List<BMLSyncPointProgressFeedback> fbs){}
    
    @Override
    public void removeAllFeedbackListeners(){}

    @Override
    public void removeFeedbackListener(BMLFeedbackListener fb){}
    
    @Override
	public ImmutableSet<String> getSyncsPassed(String bmlId, String behaviorId)
    {
        return new ImmutableSet.Builder<String>().build();   
	}

	@Override
	public void blockProgress(BMLABlockProgressFeedback psf) {}

	@Override
	public void puException(TimedPlanUnit timedMU, String message, double time){}
    
    @Override
    public void prediction(BMLAPredictionFeedback bpsf){}

    @Override
    public void warn(BMLWarningFeedback w, double time){}

}
