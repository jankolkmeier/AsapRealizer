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
package asap.realizer.interrupt;

import java.util.ArrayList;
import java.util.List;

import saiba.bml.core.Behaviour;
import asap.bml.ext.bmla.BMLAInterruptBehaviour;
import asap.realizer.AbstractPlanner;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizer.scheduler.TimePegAndConstraint;

/**
 * Planner for the bmla interrupt behavior 
 * @author welberge
 */
public class InterruptPlanner extends AbstractPlanner<TimedInterruptUnit>
{
    private BMLScheduler scheduler;    
    
    public InterruptPlanner(FeedbackManager bfm,BMLScheduler s, PlanManager<TimedInterruptUnit> planManager)
    {
        this(bfm,planManager);
        scheduler = s;
    }

    /**
     * Should call setScheduler before actual use
     */
    public InterruptPlanner(FeedbackManager bfm, PlanManager<TimedInterruptUnit> planManager)
    {
        super(bfm, planManager);        
    }

    
    
    public void setScheduler(BMLScheduler s)
    {
        scheduler = s;
    }    
    
    @Override
    public List<SyncAndTimePeg> addBehaviour(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac,
            TimedInterruptUnit iu) throws BehaviourPlanningException
    {
        if (!(b instanceof BMLAInterruptBehaviour))
        {
            throw new BehaviourPlanningException(b, "Behaviour is not a BMLTInterruptBehaviour");
        }
        BMLAInterruptBehaviour ib = (BMLAInterruptBehaviour) b;

        if (iu == null)
        {
            iu = new TimedInterruptUnit(fbManager, bbPeg, b.getBmlId(), b.id, ib.getTarget(), scheduler);
        }
        iu.setInclude(ib.getInclude());
        iu.setExclude(ib.getExclude());
        
        validateSacs(b, sac);
        setTimePegFromSac("start",sac.get(0), iu);
        

        planManager.addPlanUnit(iu);

        List<SyncAndTimePeg> list = new ArrayList<SyncAndTimePeg>();
        list.add(new SyncAndTimePeg(b.getBmlId(), b.id, "start", iu.getTimePeg("start")));
        return list;
    }

    private void validateSacs(Behaviour b, List<TimePegAndConstraint> sac) throws BehaviourPlanningException
    {
        if (sac.size() > 1)
        {
            throw new BehaviourPlanningException(b, "Multiple synchronization constraints on to interrupt behavior " + b);
        }
        if (!sac.get(0).syncId.equals("start"))
        {
            throw new BehaviourPlanningException(b, "Interrupt behavior " + b + " has a synchronization constraint other than start.");
        }
    }

    @Override
    public TimedInterruptUnit resolveSynchs(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac)
            throws BehaviourPlanningException
    {
        TimedInterruptUnit iu = new TimedInterruptUnit(fbManager, bbPeg, b.getBmlId(), b.id, b.getStringParameterValue("target"), scheduler);
        validateSacs(b, sac);

        TimePegAndConstraint sacStart = sac.get(0);
        if (sacStart.peg.getGlobalValue() == TimePeg.VALUE_UNKNOWN)
        {
            sacStart.peg.setLocalValue(0);
        }

        setTimePegFromSac("start",sac.get(0), iu);
        return iu;
    }

    @Override
    public List<Class<? extends Behaviour>> getSupportedBehaviours()
    {
        List<Class<? extends Behaviour>> list = new ArrayList<Class<? extends Behaviour>>();
        list.add(BMLAInterruptBehaviour.class);
        return list;
    }

    @Override
    public List<Class<? extends Behaviour>> getSupportedDescriptionExtensions()
    {
        return new ArrayList<Class<? extends Behaviour>>();
    }

    @Override
    public double getRigidity(Behaviour beh)
    {
        return 0;
    }
}
