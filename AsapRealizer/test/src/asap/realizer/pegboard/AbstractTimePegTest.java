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
package asap.realizer.pegboard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Superclass for unit tests that test TimePeg implementations
 * @author hvanwelbergen
 */
public abstract class AbstractTimePegTest
{
    public abstract TimePeg createTimePeg(BMLBlockPeg peg);
    
    protected static final double TIME_PRECISION = 0.0001;

    @Test
    public void testGetGlobalValue()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setLocalValue(10);
        assertEquals(10.3, tp.getGlobalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetGlobalValue()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setGlobalValue(10);
        assertEquals(10, tp.getGlobalValue(), TIME_PRECISION);
        assertEquals(9.7, tp.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetGlobalUnknown()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setGlobalValue(TimePeg.VALUE_UNKNOWN);
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetLocalUnknown()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setLocalValue(TimePeg.VALUE_UNKNOWN);
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetValue()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setValue(10, new BMLBlockPeg("bml2", 0.5));
        assertEquals(10.5, tp.getGlobalValue(), TIME_PRECISION);
        assertEquals(10.2, tp.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetUnknownValue()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setValue(10, new BMLBlockPeg("bml2", TimePeg.VALUE_UNKNOWN));
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testGetValue()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setGlobalValue(10);
        assertEquals(9.5, tp.getValue(new BMLBlockPeg("bml2",0.5)), TIME_PRECISION);
    }
    
    @Test
    public void testGetValueUnknown()
    {
        TimePeg tp = createTimePeg(new BMLBlockPeg("bml1", 0.3));
        tp.setGlobalValue(TimePeg.VALUE_UNKNOWN);
        assertEquals(TimePeg.VALUE_UNKNOWN, tp.getValue(new BMLBlockPeg("bml2",0.5)), TIME_PRECISION);
    }        
}
