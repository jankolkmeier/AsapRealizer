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
package asap.murml.testutil;

import static org.junit.Assert.fail;

import java.util.List;

import asap.murml.Dynamic;
import asap.murml.JointValue;
import asap.murml.Slot;
import asap.murml.Static;

/**
 * Test Utilities for AsapMURML
 * @author hvanwelbergen
 *
 */
public final class MURMLTestUtil 
{
    private MURMLTestUtil(){}
    public static JointValue createJointValue(String id, float ... dofs)
    {
        return new JointValue(id,dofs);
    }
    
    public static Dynamic getDynamic(Slot slot, List<Dynamic> dynamics)
    {
        for(Dynamic dyn:dynamics)
        {
            if(dyn.getSlot().equals(slot))return dyn;
        }
        fail("dynamic with slot "+slot +"not found in "+dynamics);
        return null;
    }
    
    
    public static Static getStatic(Slot slot, List<Static> statics)
    {
        for(Static stat:statics)
        {
            if(stat.getSlot().equals(slot))return stat;
        }
        fail("static with slot "+slot +"not found in "+statics);
        return null;
    }
}
