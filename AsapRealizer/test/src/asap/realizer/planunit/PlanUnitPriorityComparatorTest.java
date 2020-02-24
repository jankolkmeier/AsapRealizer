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
package asap.realizer.planunit;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;


/**
 * Unit test cases for the PlanUnitPriorityComparator
 * @author hvanwelbergen
 *
 */
public class PlanUnitPriorityComparatorTest
{
    private List<TimedPlanUnit> tmus = new ArrayList<TimedPlanUnit>();
    private TimedPlanUnit mockTmu1 = mock(TimedPlanUnit.class);
    private TimedPlanUnit mockTmu2 = mock(TimedPlanUnit.class);
    private TimedPlanUnit mockTmu3 = mock(TimedPlanUnit.class);
    private TimedPlanUnit mockTmu4 = mock(TimedPlanUnit.class);
    
    @Test
    public void testPrioritySort()
    {
        tmus.add(mockTmu1);
        tmus.add(mockTmu2);
        tmus.add(mockTmu3);
        tmus.add(mockTmu4);
        when(mockTmu1.getPriority()).thenReturn(1);
        when(mockTmu2.getPriority()).thenReturn(2);
        when(mockTmu3.getPriority()).thenReturn(4);
        when(mockTmu4.getPriority()).thenReturn(3);
        
        Collections.sort(tmus,new PlanUnitPriorityComparator());
        assertThat(tmus,contains(mockTmu3,mockTmu4,mockTmu2,mockTmu1));
    }
    
    @Test
    public void testPriorityAndStartTimeSort()
    {
        tmus.add(mockTmu1);
        tmus.add(mockTmu2);
        tmus.add(mockTmu3);
        tmus.add(mockTmu4);
        when(mockTmu1.getPriority()).thenReturn(1);
        when(mockTmu2.getPriority()).thenReturn(2);
        when(mockTmu2.getStartTime()).thenReturn(2.0d);
        when(mockTmu3.getPriority()).thenReturn(2);
        when(mockTmu3.getStartTime()).thenReturn(1.0d);
        when(mockTmu4.getPriority()).thenReturn(3);
        
        
        Collections.sort(tmus,new PlanUnitPriorityComparator());
        assertThat(tmus,contains(mockTmu4,mockTmu2,mockTmu3,mockTmu1));        
    }
}
