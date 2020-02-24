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
package asap.realizer.planunit;


import java.util.Collection;

import asap.realizer.feedback.FeedbackManager;

import com.google.common.collect.ImmutableCollection;

/**
 * Plays/stops a timedplanunit at time t, should start it if needed. 
 * Gathers all playing/stopping exceptions. They can be queried using getTimedPlanUnitPlayException/getTimedPlanUnitStopException.
 * Handled exceptions are removed using clearPlayExceptions/clearStopExceptions.
 * @author welberge
 */
public interface TimedPlanUnitPlayer
{
    void playUnit(TimedPlanUnit su, double t);
    void stopUnit(TimedPlanUnit su, double t);
    ImmutableCollection<TimedPlanUnitPlayException> getPlayExceptions();
    void clearPlayExceptions(Collection<TimedPlanUnitPlayException> removeExceptions);
    ImmutableCollection<TimedPlanUnitPlayException> getStopExceptions();
    void clearStopExceptions(Collection<TimedPlanUnitPlayException> removeExceptions);
    void handleStopExceptions(double t);
    void handlePlayExceptions(double t, FeedbackManager fbManager);
}
