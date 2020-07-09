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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.core.GazeShiftBehaviour;
import asap.animationengine.gaze.DynamicRestGaze;

/**
 * unit tests for the RestGazeAssembler
 * @author hvanwelbergen
 *
 */
public class RestGazeSpecTest
{
    @Test
    public void test() throws IOException
    {
        String xmlString = 
                // @formatter:off
                "<RestGazeSpec>"+
                        "<constraints>"+
                            "<constraint name=\"influence\" value=\"EYES\"/>"+
                        "</constraints>"+
                        "<parametermap>"+
                            "<parameter src=\"target\" dst=\"target\"/>" +
                        "</parametermap>"+
                        "<parameterdefaults>"+
                            "<parameterdefault name=\"param1\" value=\"value1\"/>"+
                        "</parameterdefaults>"+
                        "<RestGaze type=\"class\" class=\"asap.animationengine.gaze.DynamicRestGaze\"/>"+    
                "</RestGazeSpec>";
                //@formatter:on
        RestGazeSpec spec = new RestGazeSpec();
        spec.readXML(xmlString);
        
        String gazeBehaviorXML = 
             // @formatter:off
                "<gazeShift target=\"target1\" id=\"gaze1\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" influence=\"EYES\"/>";
              //@formatter:on
        GazeShiftBehaviour b = new GazeShiftBehaviour("bml1",new XMLTokenizer(gazeBehaviorXML));
        assertTrue(spec.satisfiesConstraints(b));
        
        assertThat(spec.getRestGaze(),instanceOf(DynamicRestGaze.class));
    }
}
