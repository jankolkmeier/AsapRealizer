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
package asap.faceengine.viseme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;
/**
 * Unit tests for VisemeToMorphMapping
 * @author welberge
 *
 */
public class VisemeToMorphMappingTest
{
    private VisemeToMorphMapping map = new VisemeToMorphMapping();
    private static final double PARAMETER_PRECISION = 0.0001;
    @Test
    public void testReadXML()
    {
        map.readXML("<VisemeToMorphMapping><Mapping viseme=\"10\" target=\"visemetest\"/></VisemeToMorphMapping>");
        assertThat(map.getMorphTargetForViseme(10).getMorphNames(), IsIterableContainingInAnyOrder.containsInAnyOrder("visemetest"));
        assertEquals(1,map.getMorphTargetForViseme(10).intensity,PARAMETER_PRECISION);        
    }
    
    @Test
    public void testReadXML2()
    {
        map.readXML("<VisemeToMorphMapping><Mapping viseme=\"10\" intensity=\"0.5\" target=\"visemetest\"/></VisemeToMorphMapping>");
        assertThat(map.getMorphTargetForViseme(10).getMorphNames(), IsIterableContainingInAnyOrder.containsInAnyOrder("visemetest"));
        assertEquals(0.5,map.getMorphTargetForViseme(10).intensity,PARAMETER_PRECISION);
    }
    
    @Test
    public void testReadXMLMultipleMorphs()
    {
        map.readXML("<VisemeToMorphMapping><Mapping viseme=\"10\" target=\"visemetest,visemetest2\"/></VisemeToMorphMapping>");
        assertThat(map.getMorphTargetForViseme(10).getMorphNames(), IsIterableContainingInAnyOrder.containsInAnyOrder("visemetest","visemetest2"));
        assertEquals(1,map.getMorphTargetForViseme(10).intensity,PARAMETER_PRECISION);
    }
    
    @Test
    public void testGetUsedMorphs()
    {
        map.readXML("<VisemeToMorphMapping>"
                + "<Mapping viseme=\"10\" target=\"vis10a,vis10b\"/>"
                + "<Mapping viseme=\"11\" target=\"vis11\"/>"
                + "<Mapping viseme=\"12\" target=\"vis11\"/>"
                + "</VisemeToMorphMapping>");
        assertThat(map.getUsedMorphs(), IsIterableContainingInAnyOrder.containsInAnyOrder("vis10a","vis10b","vis11"));
        map.getUsedMorphs();
    }
    @Test
    public void testGetNonExistingTarget()
    {
        assertNull(map.getMorphTargetForViseme(1));
    }
}
