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

import hmi.testutil.xml.XSDValidationTest;
import hmi.util.Resources;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

/**
 * Validates all gesturebindings in 
 * asapresource/AsapHumanoidControlShared/resource/Humanoids/shared/gesturebinding and  
 * asapresource/AsapHumanoidControlArmandia/resource/Humanoids/armandia/gesturebinding
 * using the xsd
 * @author welberge
 *
 */
public class GestureBindingXSDValidationTest extends XSDValidationTest
{
    private static final Resources GB_XSD_RES = new Resources("xsd");
    private static final String GB_XSD = "gesturebinding.xsd";
    private static final String GB_DIRS[] = {
        System.getProperty("shared.project.root")+"/AsapResource/AsapHumanoidControlShared/resource/Humanoids/shared/gesturebinding",
        System.getProperty("shared.project.root")+"/AsapResource/AsapHumanoidControlArmandia/resource/Humanoids/armandia/gesturebinding"
    };
        
    
    @Before
    public void setup()
    {
        xsdReader = GB_XSD_RES.getReader(GB_XSD);        
    }
    
    @Parameters
    public static Collection<Object[]> configs()
    {
        return configs(GB_DIRS);        
    }
    
    public GestureBindingXSDValidationTest(String label, File f)
    {
        super(label, f);        
    }
}
