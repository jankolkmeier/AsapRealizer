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

/**
 * Interface for robot objects, provides access to joint states
 * @author davisond
 *
 */
public interface Robot {

	/**
	 * Returns the most recent state of the specified joint. No guarantees can be made on the correctness and actuality of the joint states.
	 * This function returns null if the joint is not found
	 * @param id the name of the joint
	 * @return the joint state, or null if id is not found
	 */
	public NaoJoint getJointState(String id);
	public void updateJointState(String id, NaoJoint naoJoint);
	
}
