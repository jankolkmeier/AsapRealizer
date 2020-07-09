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
package asap.timemanipulator;

/**
 * From MURML/ACE ease in ease out in divmath.cc
 * @author hvanwelbergen
 * 
 */
public class EaseInEaseOutManipulator implements TimeManipulator
{
    private final double p;
    private final double scale;
    
    public EaseInEaseOutManipulator()
    {
        this(0,0);
    }
    
    public EaseInEaseOutManipulator(double scale, double p)
    {
        this.p = p;
        this.scale = scale;
    }

    /**
     * @param t  Time [0..1]    
     */
    public double manip(double t)
    {
        // Note: The function is linearly distorted to meet the
        // f(0)=0 and f(1)=1 constraints :-)
        double dist = (2 * t - 1) / (1 + Math.exp(scale * p));
        return ((1 / (1 + Math.exp(-scale * (t - p)))) + dist);
    }

    public double easeDiff(double t)
    {
        return ((scale * Math.exp(-scale * (t - p))) / (Math.pow(1 + Math.exp(-scale * (t - p)), 2)));
    }    
}
