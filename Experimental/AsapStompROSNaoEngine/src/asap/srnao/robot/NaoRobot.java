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
package asap.srnao.robot;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for the Nao robot, provides access to up-to-date joint states
 * @author davisond
 *
 */
public class NaoRobot implements Robot {

	private Map<String,NaoJoint> naoJoints = new HashMap<String,NaoJoint>();
	
	@Override
	public NaoJoint getJointState(String id) {
		return (naoJoints.get(id));
	}

	@Override
	public void updateJointState(String id, NaoJoint naoJoint) {
		naoJoints.put(id, naoJoint);
	}
	
	public String toString()
	{
		String ret = "";
		
		for(NaoJoint joint : naoJoints.values())
		{
			ret += "Jointname: "+joint.getId()+" - Angle: "+joint.getAngle()+"\r\n";
		}
		
		return ret;
	}

}
