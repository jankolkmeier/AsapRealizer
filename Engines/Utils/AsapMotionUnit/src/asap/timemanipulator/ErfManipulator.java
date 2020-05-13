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

import org.apache.commons.math3.special.Erf;

/**
 * An ease-in ease out manipulator based on the Gaussian error function
 * 
 * As used in:<br>
 * Helena Grillon and Daniel Thalmann, Simulating gaze attention behaviors for crowds (2009), in: Computer Animation and Virtual Worlds, 20 2-3(111-- 119)
 * 
 * @author Herwin 
 */
public class ErfManipulator implements TimeManipulator
{
    private final int N;

    public ErfManipulator(int N)
    {
        this.N = N;
    }

    @Override
    public double manip(double t)
    {
        double x = t*N;
        return 0.5 + Erf.erf(x-(N/2.0))*0.5;
    }
}
