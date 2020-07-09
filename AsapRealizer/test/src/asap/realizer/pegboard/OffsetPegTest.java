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
 * Unit tests for the OffsetPeg
 * @author hvanwelbergen
 *
 */
public class OffsetPegTest extends AbstractTimePegTest
{
    @Override
    public TimePeg createTimePeg(BMLBlockPeg peg)
    {
        TimePeg link = new TimePeg(peg);
        return new OffsetPeg(link,10);
    }
    
    @Test
    public void testSetOffsetPegGlobal()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2);
        peg.setGlobalValue(10);
        assertEquals(10, peg.getGlobalValue(), TIME_PRECISION);
        assertEquals(8,link.getGlobalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetOffsetPegLinkGlobal()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2);
        link.setGlobalValue(10);
        assertEquals(12, peg.getGlobalValue(), TIME_PRECISION);
        assertEquals(10,link.getGlobalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetOffsetPegLocal()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2);
        peg.setLocalValue(10);
        assertEquals(10, peg.getLocalValue(), TIME_PRECISION);
        assertEquals(8,link.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetOffsetPegLinkLocal()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2);
        link.setLocalValue(10);
        assertEquals(12, peg.getLocalValue(), TIME_PRECISION);
        assertEquals(10,link.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetOffsetPegGlobalValueLinkInDifferentBlock()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2,new BMLBlockPeg("bml2",0.5));
        peg.setGlobalValue(10);
        assertEquals(10, peg.getGlobalValue(), TIME_PRECISION);
        assertEquals(8,link.getGlobalValue(), TIME_PRECISION);
        assertEquals(7.7, link.getLocalValue(),TIME_PRECISION);
        assertEquals(9.5, peg.getLocalValue(),TIME_PRECISION);
    }
    
    @Test
    public void testSetOffsetPegLocalValueLinkInDifferentBlock()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2,new BMLBlockPeg("bml2",0.5));
        peg.setLocalValue(10);
        assertEquals(10.5, peg.getGlobalValue(), TIME_PRECISION);
        assertEquals(8.5,link.getGlobalValue(), TIME_PRECISION);
        assertEquals(8.2, link.getLocalValue(),TIME_PRECISION);
        assertEquals(10, peg.getLocalValue(),TIME_PRECISION);
    }
    
    @Test
    public void testSetOffsetPegToUnknown()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2);
        link.setGlobalValue(2);
        peg.setGlobalValue(TimePeg.VALUE_UNKNOWN);
        assertEquals(TimePeg.VALUE_UNKNOWN, link.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, peg.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, link.getLocalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, peg.getLocalValue(), TIME_PRECISION);
    }
    
    @Test
    public void testSetLinkToUnknown()
    {
        TimePeg link = new TimePeg(new BMLBlockPeg("bml1",0.3));
        OffsetPeg peg = new OffsetPeg(link,2);
        peg.setGlobalValue(2);
        link.setGlobalValue(TimePeg.VALUE_UNKNOWN);
        assertEquals(TimePeg.VALUE_UNKNOWN, link.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, peg.getGlobalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, link.getLocalValue(), TIME_PRECISION);
        assertEquals(TimePeg.VALUE_UNKNOWN, peg.getLocalValue(), TIME_PRECISION);
    }
    
}
