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
 * Phases from:
 * Kopp, Stefan and Wachsmuth, Ipke, 
 * Synthesizing multimodal utterances for conversational agents, 
 * in: Comput. Animat. Virtual Worlds, 15:1(39--52)
 * @author welberge
 */
public enum TimedPlanUnitState
{
    IN_PREP,    //being planned
    PENDING,    //IN_PREP completed
    LURKING,    //ready to be executed
    IN_EXEC,    //playing
    SUBSIDING,  //in retraction phase
    DONE;       //finished playing
    
    public boolean isPlaying()
    {
        return this == IN_EXEC || this == SUBSIDING;
    }
    
    public boolean isSubsiding()
    {
        return this == SUBSIDING;
    }
    
    public boolean isSubsidingOrDone()
    {
        return this == SUBSIDING || this == DONE;
    }
    
    public boolean isDone()
    {
        return this == DONE;
    }
    
    public boolean isLurking()
    {
        return this == LURKING;
    }
    
    public boolean isInExec()
    {
        return this == IN_EXEC;
    }
    
    public boolean isPending()
    {
        return this == PENDING;
    }
    
    public boolean isInPrep()
    {
        return this == IN_PREP;
    }
}
