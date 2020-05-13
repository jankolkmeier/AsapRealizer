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
package asap.animationengine.gesturebinding;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import hmi.util.Resources;

import org.junit.Test;

import asap.animationengine.motionunit.StubAnimationUnit;

class NotAMotionUnit {}

/**
 * Unit test cases for the MotionUnitAssembler
 * @author welberge
 */
public class MotionUnitAssemblerTest
{
    private Resources mockResources = mock(Resources.class); 
    private MotionUnitAssembler mu = new MotionUnitAssembler(mockResources);
    
    
    
    @Test
    public void testEmpty()
    {
        assertNull(mu.getMotionUnit());
    }
    
    @Test
    public void testMotionUnitFromClass()
    {
        mu.readXML("<MotionUnit type=\"class\" class=\"asap.animationengine.motionunit.StubAnimationUnit\"/>");
        assertThat(mu.getMotionUnit(), instanceOf(StubAnimationUnit.class));        
    }
    
    @Test
    public void testMotionUnitFromClassThatDoesNotImplementMotionUnit()
    {
        mu.readXML("<MotionUnit type=\"class\" class=\"asap.animationengine.gesturebinding.NotAMotionUnit\"/>");   
        assertNull(mu.getMotionUnit());
    }
    
    @Test
    public void testMotionUnitFromInterface()
    {
        mu.readXML("<MotionUnit type=\"class\" class=\"asap.animationengine.motionunit.MotionUnit\"/>");   
        assertNull(mu.getMotionUnit());
    }
}
