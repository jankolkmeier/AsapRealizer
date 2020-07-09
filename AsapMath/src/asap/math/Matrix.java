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
package asap.math;

/**
 * Class wrapper for a float array that provides the functions in Mat
 * @author hvanwelbergen
 * 
 */
public class Matrix
{
    private double mat[];
    private final int m;

    public Matrix(int n, int m)
    {
        this.m = m;
        mat = new double[n * m];
    }

    public double get(int i, int j)
    {
        return mat[i * m + j];
    }

    public void set(int i, int j, double val)
    {
        mat[i * m + j] = val;
    }
}
