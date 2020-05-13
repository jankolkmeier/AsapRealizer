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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit testcases for LinearInterpolator
 * @author hvanwelbergen
 *
 */
public class LinearInterpolatorTest
{
    private static final double INTERPOLATION_PRECISION = 0.0001;
    
    @Test
    public void testInterpolateStart()
    {
        double pval[][]={{0,1},{1,2},{2,3}};
        LinearInterpolator in = new LinearInterpolator(pval);
        assertEquals(1,in.interpolate(0),INTERPOLATION_PRECISION);
    }
    
    @Test
    public void testInterpolateEnd()
    {
        double pval[][]={{0,1},{1,2},{2,3}};
        LinearInterpolator in = new LinearInterpolator(pval);
        assertEquals(3,in.interpolate(2),INTERPOLATION_PRECISION);
    }
    
    @Test
    public void testInterpolateMiddle()
    {
        double pval[][]={{0,1},{1,2},{2,3}};
        LinearInterpolator in = new LinearInterpolator(pval);
        assertEquals(2,in.interpolate(1),INTERPOLATION_PRECISION);
    }
    
    @Test
    public void testInterpolateMiddle2()
    {
        double pval[][]={{0,1},{1,2},{2,3}};
        LinearInterpolator in = new LinearInterpolator(pval);
        assertEquals(1.5,in.interpolate(0.5),INTERPOLATION_PRECISION);
    }
    
    @Test
    public void testInterpolateOneValue()
    {
        double pval[][]={{0,1}};
        LinearInterpolator in = new LinearInterpolator(pval);
        assertEquals(1,in.interpolate(0),INTERPOLATION_PRECISION);
    }
}
