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
package asap.livemocapengine.inputs.loader;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.environmentbase.SensorLoader;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import asap.livemocapengine.inputs.KeyboardInput;

/**
 * Loads the KeyboardInput
 * @author Herwin
 * 
 */
public class KeyboardInputLoader implements SensorLoader
{
    private String id = "";
    private KeyboardInput sensor;

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;        
        sensor = new KeyboardInput(id);
    }

    @Override
    public void unload()
    {

    }

    @Override
    public KeyboardInput getSensor()
    {
        return sensor;
    }
}
