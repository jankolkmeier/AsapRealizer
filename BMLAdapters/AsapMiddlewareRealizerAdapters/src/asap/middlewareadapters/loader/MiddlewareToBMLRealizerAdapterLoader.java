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
package asap.middlewareadapters.loader;

import hmi.util.Clock;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;
import asap.realizerembodiments.PipeLoader;
import asap.realizerport.RealizerPort;
import asap.middlewareadapters.MiddlewareToBMLRealizerAdapter;

/**
 * Loader for a MiddlewareToBMLRealizerAdapter
 
 * @author Daniel
 */
@Slf4j
public class MiddlewareToBMLRealizerAdapterLoader implements PipeLoader
{
	private static Logger logger = LoggerFactory.getLogger(MiddlewareToBMLRealizerAdapterLoader.class.getName());

	private RealizerPort adaptedRealizerPort = null;
    
    /**
     * @throws XMLScanException
     */
    @Override
    public void readXML(XMLTokenizer theTokenizer, String id, String vhId, String name, RealizerPort realizerPort, Clock theSchedulingClock)
            throws IOException
    {
        adaptedRealizerPort = realizerPort;
        if(!theTokenizer.atSTag("MiddlewareOptions"))
        {
            throw new XMLScanException("MiddlewareToBMLRealizerAdapterLoader requires an inner MiddlewareOptions element");            
        }
        
        HashMap<String, String> attrMap = theTokenizer.getAttributes();
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        String loaderclass = adapter.getRequiredAttribute("loaderclass", attrMap, theTokenizer);

        theTokenizer.takeSTag("MiddlewareOptions");

        Properties props = new Properties();
        while (theTokenizer.atSTag("MiddlewareProperty"))
        {
            HashMap<String, String> attrMap2 = theTokenizer.getAttributes();
            XMLStructureAdapter adapter2 = new XMLStructureAdapter();
            props.put(adapter2.getRequiredAttribute("name", attrMap, theTokenizer),adapter2.getRequiredAttribute("value", attrMap, theTokenizer));
            theTokenizer.takeSTag("MiddlewareProperty");
            theTokenizer.takeETag("MiddlewareProperty"); 
        }
        
        new MiddlewareToBMLRealizerAdapter(realizerPort, loaderclass, props);

        
        theTokenizer.takeETag("MiddlewareOptions"); 
        if (!theTokenizer.atETag("PipeLoader")) throw new XMLScanException("MiddlewareToBMLRealizerAdapterLoader can only have one MiddlewareOptions element");
    }

    @Override
    public RealizerPort getAdaptedRealizerPort()
    {
        return adaptedRealizerPort;
    }

    @Override
    public void shutdown()
    {
    }
}
