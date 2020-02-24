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

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

public class NaoTTSUnit extends AbstractNaoUnit{

	private ALMemory memory;
	private ALTextToSpeech tts;
	private String speech;
	
	public NaoTTSUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId, String speech) {
		super(fbm, bmlPeg, bmlId, behId);
		this.speech = speech;
		try {
			this.feedback(application.session());
			tts = new ALTextToSpeech(application.session());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	
	public String getSpeech() {
		return speech;
	}

	@Override
	protected void startUnit(double time) throws TimedPlanUnitPlayException {
		System.out.println("Start");
		try {
			tts.say(speech);
			//TODO: Endzeit erst bei Feedback vom Roboter setzen.
			setEnd(startPeg);
		} catch (CallError | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void feedback(Session session) throws Exception {
		System.out.println("FEEDBACK");
		memory = new ALMemory(session);
		
		memory.subscribeToEvent("TextDone", new EventCallback<Float>() {
			@Override
			public void onEvent(Float arg0) {
				System.out.println("TEXT DONE ======================================================================================");
				if (arg0 > 0) {
					System.out.println("ICH HABE FERTIG MIT TEXT!!!!!!!!!!!!!!!");
				}
			}
		});
	}

}
