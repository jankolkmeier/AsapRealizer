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
package asap.animationengine.controller;

import hmi.testutil.xml.XSDValidationTest;
import hmi.util.Resources;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import org.junit.Before;
import org.junit.runners.Parameterized.Parameters;

/**
 * Validates the XML of compound controllers in /HmiResource/HmiHumanoidBodyControl/resource/Humanoids/shared/controllers
 * and /HmiResource/HmiHumanoidBodyControl/resource/Humanoids/armandia/controllers
 * @author welberge
 * 
 */
public class CompoundControllerXSDValidationIntegrationTest extends XSDValidationTest
{
    private static final Resources CC_XSD_RES = new Resources("xsd");
    private static final String CC_XSD = "compoundcontroller.xsd";
    private static final String CC_DIRS[] = {
            System.getProperty("shared.project.root") + "/AsapResource/AsapHumanoidControlShared/resource/Humanoids/shared/controllers",
            System.getProperty("shared.project.root") + "/AsapResource/AsapHumanoidControlArmandia/resource/Humanoids/armandia/controllers" };

    @Before
    public void setup()
    {
        xsdReader = CC_XSD_RES.getReader(CC_XSD);
    }

    @Override
    protected InputStream getXSDStream(String fileName)
    {
        return CC_XSD_RES.getInputStream(fileName);
    }

    @Parameters
    public static Collection<Object[]> configs()
    {
        return configs(CC_DIRS);
    }

    public CompoundControllerXSDValidationIntegrationTest(String label, File f)
    {
        super(label, f);
    }
}
