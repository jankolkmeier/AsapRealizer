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
package asap.naoengine.naounits;

import java.util.List;

import com.aldebaran.qi.Application;
import com.google.common.collect.ImmutableList;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedAbstractPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;

public abstract class AbstractNaoUnit extends TimedAbstractPlanUnit{

	private static String IP = "tcp://127.0.0.1:46320";
	protected static Application application = new Application(new String[0], IP);
	
	static {
		if (application != null) {
			System.out.println("Connected to " + IP);
			application.start();
		}
	}
	
	protected TimePeg startPeg;
	protected TimePeg endPeg;
	
	public AbstractNaoUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId) {
		super(fbm, bmlPeg, bmlId, behId);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public double getStartTime() {
		return startPeg.getGlobalValue();
	}

	@Override
	public double getEndTime() {
		if (endPeg == null) {
			return TimePeg.VALUE_UNKNOWN;
		} else {
			return endPeg.getGlobalValue();
		}
	}

	@Override
	public double getRelaxTime() {
		return getEndTime();
	}

	@Override
	public List<String> getAvailableSyncs() {
		// TODO Auto-generated method stub
		return ImmutableList.of("start", "end");
	}

	@Override
	public TimePeg getTimePeg(String syncId) {
		if (syncId.equals("start")) {
			return startPeg;
		} else if (syncId.equals("end")) {
			return endPeg;
		} else {
			return null;
		}
	}

	@Override
	public void setTimePeg(String syncId, TimePeg peg) {
		if (syncId.equals("start")) {
			setStart(peg);
		} else if (syncId.equals("end")) {
			setEnd(peg);
		}	
	}

	public void setEnd(TimePeg peg) {
		endPeg = peg;	
	}

	public void setStart(TimePeg peg) {
		startPeg = peg;		
	}

	@Override
	public boolean hasValidTiming() {
		/*if (getStartTime() > getEndTime())
        {
            return false;
        }*/
        return true;
	}
	
	protected void playUnit(double time) throws TimedPlanUnitPlayException {}
	protected void stopUnit(double time) throws TimedPlanUnitPlayException {}
	
}
