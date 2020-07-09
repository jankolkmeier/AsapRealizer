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

/**
 * A simple class serving as an extended data type to store all parameters for a dominance function.
 * 
 * @author mklemens
 */
public class DominanceParameters{
	
	private double magnitude;
	private double stretchLeft;
	private double stretchRight;
	private double rateLeft;
	private double rateRight;
	private double peak;
	private double startOffsetMultiplicator;
	private double endOffsetMultiplicator;
	
	public DominanceParameters(Double m, Double sl, Double sr, Double rl, Double rr, Double p, Double tol, Double tor) {
		setDominanceParameters(m, sl, sr, rl, rr, p, tol, tor);
	}
	
	// Store the passed parameters
	public void setDominanceParameters(Double m, Double sl, Double sr, Double rl, Double rr, Double p, Double tol, Double tor) {
		magnitude = m;
		stretchLeft = sl;
		stretchRight = sr;
		rateLeft = rl;
		rateRight = rr;
		peak = p;
		startOffsetMultiplicator = tol;
		endOffsetMultiplicator = tor;
	}
	
	public double getMagnitude() {
		return magnitude;
	}

	public double getStretchLeft() {
		return stretchLeft;
	}

	public double getStretchRight() {
		return stretchRight;
	}

	public double getRateLeft() {
		return rateLeft;
	}

	public double getRateRight() {
		return rateRight;
	}

	public double getPeak() {
		return peak;
	}
	
	public double getStartOffsetMultiplicator() {
		return startOffsetMultiplicator;
	}

	public double getEndOffsetMultiplicator() {
		return endOffsetMultiplicator;
	}
}