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

import hmi.testutil.xml.XSDValidationTest;
import hmi.util.Resources;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test validity of visemebinding resources in /asapresource/AsapHumanoidControlArmandia/resource/Humanoids/armandia/facebinding
 * @author welberge
 * 
 */
public class VisemeToMorphMappingXSDValidationIntegrationTest extends XSDValidationTest
{
    private static final Resources XSD_RES = new Resources("xsd");
    private static final String XSD = "visemebinding.xsd";
    private static final String DIRS[] = { System.getProperty("shared.project.root")
            + "/AsapResource/AsapHumanoidControlArmandia/resource/Humanoids/armandia/facebinding" };

    @Before
    public void setup()
    {
        xsdReader = XSD_RES.getReader(XSD);
    }

    @Parameters
    public static Collection<Object[]> configs()
    {
        Collection<Object[]> objs = configs(DIRS);
        Collection<Object[]> filteredObjs = new ArrayList<Object[]>();
        for (Object[] obj : objs)
        {
            if (obj[0].toString().contains("visemebinding"))
            {
                filteredObjs.add(obj);
            }
        }
        return filteredObjs;
    }

    public VisemeToMorphMappingXSDValidationIntegrationTest(String label, File f)
    {
        super(label, f);
    }
}
