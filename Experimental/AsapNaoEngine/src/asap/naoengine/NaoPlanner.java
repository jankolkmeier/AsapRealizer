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
package asap.naoengine;

import java.util.ArrayList;
import java.util.List;

import asap.naoengine.naounits.AbstractNaoUnit;
import asap.naoengine.naounits.NaoGazeUnit;
import asap.naoengine.naounits.NaoTTSUnit;
import asap.realizer.AbstractPlanner;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.TimePegAndConstraint;
import saiba.bml.core.Behaviour;
import saiba.bml.core.GazeShiftBehaviour;
import saiba.bml.core.SpeechBehaviour;

public class NaoPlanner extends AbstractPlanner<AbstractNaoUnit> {

	public NaoPlanner(FeedbackManager feedbackManager, PlanManager<AbstractNaoUnit> planManager) {
		// TODO Auto-generated constructor stub
		super(feedbackManager, planManager);
	}

	@Override
	public List<SyncAndTimePeg> addBehaviour(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac,
			AbstractNaoUnit planElement) throws BehaviourPlanningException {

		System.out.println("Add Behaviour Start");
		
		if(planElement == null) {
			planElement = createNaoUnit(bbPeg, b);
		}
		linkSyncs(planElement,sac);
		
		planManager.addPlanUnit(planElement);
		
		List<SyncAndTimePeg> satps = constructSyncAndTimePegs(bbPeg, b, planElement);
		planManager.addPlanUnit(planElement);
		
		System.out.println("Add Behaviour End");
		
		return satps;
	}
	
	/**
	 * 
	 * @param nu
	 * @param sac
	 */
	private void linkSyncs(AbstractNaoUnit nu, List<TimePegAndConstraint> sac) {
		for (TimePegAndConstraint c : sac) {
			if (c.offset == 0) {
				nu.setTimePeg(c.syncId, c.peg);
			} else {
				nu.setTimePeg(c.syncId, new OffsetPeg(c.peg, -c.offset));
			}
		}
	}

	/**
	 * Specifies unknown sync points
	 */
	@Override
	public AbstractNaoUnit resolveSynchs(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac)
			throws BehaviourPlanningException {
		
		System.out.println("Resolve Start");

		AbstractNaoUnit unit = createNaoUnit(bbPeg, b);
		
		//NaoTTSUnit unit = new NaoTTSUnit(fbManager, bbPeg, b.getBmlId(), b.id);
		
		for (TimePegAndConstraint constraint : sac) {
			System.out.println(constraint.syncId);
			if (constraint.syncId.equals("start")) {
				if (constraint.peg.getGlobalValue() == TimePeg.VALUE_UNKNOWN) {
					constraint.peg.setLocalValue(0);
				}
			}
			TimePeg peg = constraint.peg;
			if (constraint.offset != 0){
				peg = new OffsetPeg(constraint.peg, -constraint.offset);
			}
			unit.setTimePeg(constraint.syncId, peg);
			System.out.println(unit.getStartTime());
		}
		
		System.out.println("Resolve End");
		
		return unit;
	}

	@Override
	public List<Class<? extends Behaviour>> getSupportedBehaviours() {
		// TODO Auto-generated method stub
		List<Class<? extends Behaviour>> list = new ArrayList<Class<? extends Behaviour>>();
		list.add(GazeShiftBehaviour.class);
		list.add(SpeechBehaviour.class);
		return list;
	}

	@Override
	public List<Class<? extends Behaviour>> getSupportedDescriptionExtensions() {
		// TODO Auto-generated method stub
		return new ArrayList<Class<? extends Behaviour>>();
	}

	@Override
	public double getRigidity(Behaviour beh) {
		// TODO Auto-generated method stub
		return 0;
	}

	private AbstractNaoUnit createNaoUnit(BMLBlockPeg peg, Behaviour b) {
		AbstractNaoUnit unit;
		
		if (b instanceof SpeechBehaviour) {
			SpeechBehaviour behaviour = (SpeechBehaviour) b;
			unit = new NaoTTSUnit(fbManager, peg, b.getBmlId(), b.id, behaviour.getContent());
		} else if (b instanceof GazeShiftBehaviour) {
			GazeShiftBehaviour behaviour = (GazeShiftBehaviour) b;
			String target = behaviour.getStringParameterValue("target");
			System.out.println(target);
			unit = new NaoGazeUnit(fbManager, peg, b.getBmlId(), b.id);
			((NaoGazeUnit)unit).setInfluence(behaviour.getStringParameterValue("influence"));
			//unit = new NaoUnit(fbManager, peg, b.getBmlId(), b.id);
		} else {
			unit = null;
		}
		
		return unit;
	}
	
}
