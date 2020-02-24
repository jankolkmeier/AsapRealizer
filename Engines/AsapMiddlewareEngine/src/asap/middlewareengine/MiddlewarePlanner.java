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
package asap.middlewareengine;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.middlewareengine.bml.SendMessageBehavior;
import asap.middlewareengine.middlewarebinding.MiddlewareBinding;
import asap.middlewareengine.planunit.TimedMiddlewareUnit;
import asap.realizer.AbstractPlanner;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.LinearStretchResolver;
import asap.realizer.scheduler.TimePegAndConstraint;
import asap.realizer.scheduler.UniModalResolver;
import saiba.bml.BMLInfo;
import saiba.bml.core.Behaviour;
import saiba.bml.core.FaceLexemeBehaviour;
import saiba.bml.core.GazeBehaviour;
import saiba.bml.core.GestureBehaviour;
import saiba.bml.core.PostureBehaviour;
import saiba.bml.core.PostureShiftBehaviour;
import saiba.bml.core.ext.FaceFacsBehaviour;

public class MiddlewarePlanner extends AbstractPlanner<TimedMiddlewareUnit>
{
    static
    {
        BMLInfo.addBehaviourType(SendMessageBehavior.xmlTag(), SendMessageBehavior.class);
    }

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(MiddlewarePlanner.class.getName());

    private final MiddlewareBinding middlewareBinding;
    private UniModalResolver resolver;

    public MiddlewarePlanner(FeedbackManager bfm, MiddlewareBinding zb, PlanManager<TimedMiddlewareUnit> planManager)
    {
        super(bfm, planManager);
        middlewareBinding = zb;
        resolver = new LinearStretchResolver();
    }

    public MiddlewareBinding getMiddlewareBinding()
    {
        return middlewareBinding;
    }

    @Override
    public List<SyncAndTimePeg> addBehaviour(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sacs, TimedMiddlewareUnit planElement)
            throws BehaviourPlanningException
    {
        TimedMiddlewareUnit tzu;

        if (planElement == null)
        {
            List<TimedMiddlewareUnit> tzus = middlewareBinding.getMiddlewareUnit(fbManager, bbPeg, b);
            if (tzus.isEmpty())
            {
                throw new BehaviourPlanningException(b, "Behavior " + b.id
                        + " could not be constructed from the  binding (no matching constraints), behavior omitted.");
            }

            // for now, just add the first
            tzu = tzus.get(0);
            if (!tzu.getMiddlewareUnit().hasValidParameters())
            {
                throw new BehaviourPlanningException(b, "Behavior " + b.id
                        + " could not be constructed from the  binding because the parameters are not valid, behavior omitted.");
            }
        }
        else
        {
            tzu = (TimedMiddlewareUnit) planElement;
        }

        resolveDefaultKeyPositions(b, tzu);
        linkSynchs(tzu, sacs);

        planManager.addPlanUnit(tzu);

        return constructSyncAndTimePegs(bbPeg,b,tzu);
    }

    @Override
    public TimedMiddlewareUnit resolveSynchs(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac) throws BehaviourPlanningException
    {
        List<TimedMiddlewareUnit> tzus = middlewareBinding.getMiddlewareUnit(fbManager, bbPeg, b);
        if (tzus.isEmpty())
        {
            throw new BehaviourPlanningException(b, "Behavior " + b.id
                    + " could not be constructed from the binding (no matching constraints), behavior omitted.");
        }
        TimedMiddlewareUnit tzu = tzus.get(0);

        if (!tzu.getMiddlewareUnit().hasValidParameters())
        {
            throw new BehaviourPlanningException(b, "Behavior " + b.id
                    + " could not be constructed from the  binding because the parameters are not valid, behavior omitted.");
        }

        resolveDefaultKeyPositions(b, tzu);
        resolver.resolveSynchs(bbPeg, b, sac, tzu);
        return tzu;
    }

    public void resolveDefaultKeyPositions(Behaviour b, TimedMiddlewareUnit tnu)
    {
        if(b instanceof GazeBehaviour)
        {
            tnu.resolveGazeKeyPositions();
        }        
        else if(b instanceof PostureShiftBehaviour)
        {
            tnu.resolveStartAndEndKeyPositions();
        }
        else if(b instanceof PostureBehaviour)
        {
            tnu.resolvePostureKeyPositions();
        }
        else if(b instanceof FaceLexemeBehaviour)
        {
            tnu.resolveFaceKeyPositions();
        }
        else if(b instanceof FaceFacsBehaviour)
        {
            tnu.resolveFaceKeyPositions();
        }
        else if(b instanceof GestureBehaviour)
        {
		    tnu.resolveGestureKeyPositions();
		}
		else
		{
            tnu.resolveStartAndEndKeyPositions();
        }
    }

    @Override
    public double getRigidity(Behaviour beh)
    {
        return 0.5;
    }

    private void linkSynchs(TimedMiddlewareUnit tzu, List<TimePegAndConstraint> sacs)
    {
        for (TimePegAndConstraint s : sacs)
        {
            for (KeyPosition kp : tzu.getMiddlewareUnit().getKeyPositions())
            {
                if (s.syncId.equals(kp.id))
                {
                    if (s.offset == 0)
                    {
                        tzu.setTimePeg(kp, s.peg);
                    }
                    else
                    {
                        tzu.setTimePeg(kp, new OffsetPeg(s.peg, -s.offset));
                    }
                }
            }
        }
    }

    
    @Override
    public List<Class<? extends Behaviour>> getSupportedBehaviours()
    {
        List<Class<? extends Behaviour>> list = new ArrayList<Class<? extends Behaviour>>();
        list.add(SendMessageBehavior.class);
        return list;
    }

    @Override
    public List<Class<? extends Behaviour>> getSupportedDescriptionExtensions()
    {
        List<Class<? extends Behaviour>> list = new ArrayList<Class<? extends Behaviour>>();
        return list;
    }
}
