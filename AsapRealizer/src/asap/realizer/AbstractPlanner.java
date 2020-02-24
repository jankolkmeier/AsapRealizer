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
package asap.realizer;

import java.util.ArrayList;
import java.util.List;

import saiba.bml.core.Behaviour;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.scheduler.TimePegAndConstraint;


/**
 * Skeleton implementation of a Planner. Handles the feedbackListeners and delegates some
 * functionality to the players.
 * 
 * @author Herwin
 * @param <T> type of TimedPlanUnit the planner manages
 */
public abstract class AbstractPlanner<T extends TimedPlanUnit> implements Planner<T>
{
    protected final PlanManager<T> planManager;
    protected final FeedbackManager fbManager;

    protected TimePegAndConstraint getSac(String sync, List<TimePegAndConstraint> sacs)
    {
        for (TimePegAndConstraint sac : sacs)
        {
            if (sac.syncId.equals(sync))
            {
                return sac;
            }
        }
        return null;
    }
    
    protected TimePegAndConstraint getSacStart(List<TimePegAndConstraint> sacs)
    {
        return getSac("start", sacs);
    }

    protected TimePegAndConstraint getSacEnd(List<TimePegAndConstraint> sacs)
    {
        return getSac("end", sacs);
    }
    
    public AbstractPlanner(FeedbackManager fbm, PlanManager<T> planManager)
    {
        fbManager = fbm;
        this.planManager = planManager;
    } 
    
    public ArrayList<SyncAndTimePeg> constructSyncAndTimePegs(BMLBlockPeg bbPeg, Behaviour b, T bs)
    {
        ArrayList<SyncAndTimePeg> satp = new ArrayList<SyncAndTimePeg>();
        for (String sync : bs.getAvailableSyncs())
        {
            TimePeg p = bs.getTimePeg(sync);
            if (p == null)
            {
                p = new TimePeg(bbPeg);
                bs.setTimePeg(sync,p);
            }
            satp.add(new SyncAndTimePeg(b.getBmlId(), b.id, sync, p));            
        }
        return satp;
    }
    
    protected void setTimePegFromSac(String syncId, TimePegAndConstraint sac, T iu)
    {
        TimePeg tp;
        if(sac.offset==0)
        {
            tp = sac.peg;
        }
        else
        {
            tp = new OffsetPeg(sac.peg,-sac.offset);
        }
        iu.setTimePeg(syncId, tp);
    }
    
    @Override
    public void shutdown()
    {
        
    }
}
