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
package asap.murml;

import hmi.testutil.LabelledParameterized;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test reading of all gestures in test/resource/gestures
 * @author hvanwelbergen
 *
 */
@RunWith(LabelledParameterized.class)
public class MURMLDescriptionIntegrationTest
{
    private final File file;
    private static String GESTURE_PATH = System.getProperty("shared.project.root")+"/AsapRealizer/Engines/Animation/AsapMURML/test/resource/gestures"; 
    
    @Parameters
    public static Collection<Object[]> configs()
    {
        Collection<Object[]> objs = new ArrayList<Object[]>();
        File dir = new File(GESTURE_PATH);
        
        for (File f : dir.listFiles())
        {
            if (f.isFile() && f.getName().endsWith(".xml"))
            {
                Object obj[] = new Object[2];
                obj[0] = f.getAbsolutePath();
                obj[1] = f;
                objs.add(obj);                
            }
        }
        return objs;
    }
    
    public MURMLDescriptionIntegrationTest(String label, File f)
    {
        file = f;        
    }    
    
    @Test
    public void test() throws IOException
    {
        MURMLDescription desc = new MURMLDescription();
        desc.readXML(file);
    }
}
