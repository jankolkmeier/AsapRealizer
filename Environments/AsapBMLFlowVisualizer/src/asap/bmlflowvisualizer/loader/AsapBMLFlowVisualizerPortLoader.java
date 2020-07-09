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
package asap.bmlflowvisualizer.loader;

import hmi.util.Clock;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import saiba.bmlflowvisualizer.BMLFlowVisualizerPort;
import asap.realizerembodiments.PipeLoader;
import asap.realizerport.RealizerPort;

/**
 * Loads the AsapBMLFlowVisualizer in a new JFrame
 * @author hvanwelbergen
 */
public class AsapBMLFlowVisualizerPortLoader implements PipeLoader
{
    private BMLFlowVisualizerPort vis;
    
    @Override
    public void readXML(XMLTokenizer theTokenizer, String id, String vhId, String name, RealizerPort realizerPort, Clock theSchedulingClock)
            throws IOException
    {
        vis = new BMLFlowVisualizerPort(realizerPort);        
    }

    @Override
    public BMLFlowVisualizerPort getAdaptedRealizerPort()
    {
        return vis;
    }

    @Override
    public void shutdown()
    {
                
    }

}
