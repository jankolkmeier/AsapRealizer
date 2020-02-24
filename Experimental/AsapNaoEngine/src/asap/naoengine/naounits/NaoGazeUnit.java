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

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

public class NaoGazeUnit extends AbstractNaoUnit{

	private String target;
	private String influence = "Head";
	private float offsetAngle;
	private String direction;
	
	private ALMotion motion;
	private ALRobotPosture posture;
	
	public NaoGazeUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId) {
		super(fbm, bmlPeg, bmlId, behId);
		try {
			motion = new ALMotion(application.session());
			posture = new ALRobotPosture(application.session());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public void setInfluence(String influence) {
		this.influence = influence;
	}
	
	public void setOffsetAngle(float offsetAngle) {
		this.offsetAngle = offsetAngle;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	@Override
	protected void startUnit(double time) throws TimedPlanUnitPlayException {
		ArrayList<Float> angles = new ArrayList<Float>();
		try {
	        motion.wakeUp();
	        posture.goToPosture("StandInit", 0.5f);
	        String effectorName = "Head";
	        
	        switch (influence) {
	            case "Head":
					//motion.stiffnessInterpolation(influence, 1.0f,1.0f);
			        ArrayList<Float> timeList = new ArrayList<Float>();
			        timeList.add(3.0f);
			        timeList.add(6.0f);
			        timeList.add(9.0f);
			        
			        angles.add(-1.5f);
			        angles.add(1.5f);
			        angles.add(0.0f);
			        
			        motion.angleInterpolation(effectorName, angles, timeList, true);
			        break;
	            case "Body":
	            	angles.add(0.0f);
	            	angles.add(0.0f);
	            	angles.add(-1.5f);
	            	motion.wbEnableEffectorControl(effectorName, true);
	            	motion.wbSetEffectorControl(effectorName, angles);
	            	break;
	            default:
	            	break;
	        }
			setEnd(startPeg);
		} catch (CallError | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
