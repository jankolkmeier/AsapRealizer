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

/**
 * Handles the playback of a single behaviour plan 
 * @author welberge
 */
public interface PlanPlayer
{
    void play(double time);

    void updateTiming(String bmlId);
    
    void stopPlanUnit(String bmlId, String id, double globalTime);

    void stopBehaviourBlock(String bmlId, double time);
    
    /**
     * Gracefully interrupts the planunit     
     */
    void interruptPlanUnit(String bmlId, String id, double globalTime);

    /**
     * Gracefully interrupts the behaviour block
     */
    void interruptBehaviourBlock(String bmlId, double time);

    void reset(double time);

    void setBMLBlockState(String bmlId, TimedPlanUnitState state);

    void shutdown();
}
