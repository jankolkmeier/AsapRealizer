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
package asap.faceengine.lipsync;

import java.util.HashMap;
/**
 * This class sole purpose is to return the numeric difference between two sound-classes to define their disparity.
 * 
 * @author mklemens
 */
public class SoundClassDifferenceCalculator {
	
	// A HashMap containing the sound-classes and their difference-value
    private HashMap<String, Integer> differenceValues = new HashMap<String, Integer>();
	
	public SoundClassDifferenceCalculator() {
        differenceValues.put("vowel", 1);
        differenceValues.put("diphthong", 1);
        differenceValues.put("glide", 2);
        differenceValues.put("liquid", 2);
        differenceValues.put("nasal", 3);
        differenceValues.put("fricative", 4);
        differenceValues.put("stop", 5);
        differenceValues.put("affricate", 6);
	}

    // Calculates the numeric difference between two sound-classes
	public int getDifference(String current, String toCompare) {
		return Math.abs(differenceValues.get(current) - differenceValues.get(toCompare));
	}
}