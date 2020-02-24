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
package asap.ipaacaeventengine;

import java.util.ArrayList;
import java.util.List;

import saiba.bml.BMLInfo;
import saiba.bml.core.Behaviour;
import asap.ipaacaeventengine.bml.IpaacaEventBehaviour;
import asap.ipaacaeventengine.bml.IpaacaMessage;
import asap.realizer.AbstractPlanner;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.TimePegAndConstraint;

/**
 * Planner for ipaacaevents
 * @author herwinvw
 *
 */
public class IpaacaEventPlanner extends AbstractPlanner<TimedIpaacaMessageUnit>
{
    private final MessageManager messageManager;
    
    static
    {
        BMLInfo.addBehaviourType(IpaacaEventBehaviour.xmlTag(), IpaacaEventBehaviour.class);        
    }
    
    public IpaacaEventPlanner(FeedbackManager fbm, PlanManager<TimedIpaacaMessageUnit> planManager, MessageManager messageManager)
    {
        super(fbm, planManager);
        this.messageManager = messageManager;        
    }

    @Override
    public List<SyncAndTimePeg> addBehaviour(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac,
            TimedIpaacaMessageUnit planElement) throws BehaviourPlanningException
    {
        IpaacaMessage msg = getMessage(b);
        if(planElement==null)
        {
            planElement = new TimedIpaacaMessageUnit(fbManager, bbPeg, b.getBmlId(), b.id, messageManager, msg.getCategory(), msg.getPayload());
        }
        
        validateSacs(b, sac);
        planElement.setStartPeg(sac.get(0).peg);
        
        planManager.addPlanUnit(planElement);

        List<SyncAndTimePeg> list = new ArrayList<SyncAndTimePeg>();
        list.add(new SyncAndTimePeg(b.getBmlId(), b.id, "start", sac.get(0).peg));
        return list;
    }

    private IpaacaMessage getMessage(Behaviour b) throws BehaviourPlanningException
    {
        if (!(b instanceof IpaacaEventBehaviour))
        {
            throw new BehaviourPlanningException(b, "Behaviour is not a IpaacaEventBehaviour");
        }
        IpaacaEventBehaviour ab = (IpaacaEventBehaviour) b;
        
        IpaacaMessage msg = new IpaacaMessage();
        msg.readXML(ab.getEvent());
        return msg;
    }
    private void validateSacs(Behaviour b, List<TimePegAndConstraint> sac) throws BehaviourPlanningException
    {
        if (sac.size() > 1)
        {
            throw new BehaviourPlanningException(b, "Multiple synchronization constraints on to ipaacaevent behavior " + b);
        }
        if (!sac.get(0).syncId.equals("start"))
        {
            throw new BehaviourPlanningException(b, "ipaacaevent behavior " + b + " has a synchronization constraint other than start.");
        }
    }
    
    @Override
    public TimedIpaacaMessageUnit resolveSynchs(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac)
            throws BehaviourPlanningException
    {
        IpaacaMessage msg = getMessage(b);
        TimedIpaacaMessageUnit timu = new TimedIpaacaMessageUnit(fbManager, bbPeg, b.getBmlId(), b.id, messageManager, msg.getCategory(), msg.getPayload());
        validateSacs(b, sac);
        TimePegAndConstraint sacStart = sac.get(0);
        
        if (sacStart.peg.getGlobalValue() == TimePeg.VALUE_UNKNOWN)
        {
            sacStart.peg.setLocalValue(0);
        }

        TimePeg start;
        if(sacStart.offset==0)
        {
            start = sacStart.peg;
        }
        else
        {
            start = new OffsetPeg(sacStart.peg,-sacStart.offset);
        }
        timu.setStartPeg(start);
        return timu;
    }

    @Override
    public List<Class<? extends Behaviour>> getSupportedBehaviours()
    {
        List<Class<? extends Behaviour>> list = new ArrayList<Class<? extends Behaviour>>();
        list.add(IpaacaEventBehaviour.class);
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
