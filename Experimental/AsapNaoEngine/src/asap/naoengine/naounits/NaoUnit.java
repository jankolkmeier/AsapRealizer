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

import java.util.ArrayList;
import java.util.List;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;
import com.google.common.collect.ImmutableList;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedAbstractPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class NaoUnit extends AbstractNaoUnit {
	
	public NaoUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId) {
		super(fbm, bmlPeg, bmlId, behId);
		// TODO Auto-generated constructor stub
        // Start your application
	}

	@Override
	protected void startUnit(double time) throws TimedPlanUnitPlayException {
		System.out.println("Starting");
		try{
			
			System.out.println("Start Unit");
			
	        ALMotion motion = new ALMotion(application.session());
	        
	        String joint = "HeadYaw";
	        
	        motion.stiffnessInterpolation(joint, 1.0, 1.0);
	        System.out.println("Stiffness");
	        ArrayList<Float> angles = new ArrayList<Float>();
	        angles.add(-1.5f);
	        angles.add(1.5f);
	        angles.add(0.0f);
	        ArrayList<Float> timeList = new ArrayList<Float>();
	        timeList.add(3.0f);
	        timeList.add(6.0f);
	        timeList.add(9.0f);
	        motion.angleInterpolation(joint, angles, timeList, true);
	        
	        System.out.println("fertisch");
	        
			// Create an ALTextToSpeech object and link it to your current session
	        
	        // Make your robot say something
	        //tts.say("Hello World!");
	        motion.stiffnessInterpolation(joint, 0.0, 1.0f);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}
