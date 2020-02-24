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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for ease in ease out manipulator
 * @author hvanwelbergen
 * 
 */
public class EaseInEaseOutManipulatorTest extends AbstractTimeManipulatorTest
{
    @Override
    protected TimeManipulator getManipulator()
    {
        return new EaseInEaseOutManipulator(2,0.5);
    }

    @Test
    public void testLinear()
    {
        EaseInEaseOutManipulator m = new EaseInEaseOutManipulator(0,0);
        assertEquals(1, m.manip(1), MANIP_PRECISION);
        assertEquals(0, m.manip(0), MANIP_PRECISION);
        assertEquals(0.5, m.manip(0.5), MANIP_PRECISION);
    }

}
