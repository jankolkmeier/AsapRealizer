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
package asap.middlewareadapters;
import static nl.utwente.hmi.middleware.helpers.JsonNodeBuilders.array;
import static nl.utwente.hmi.middleware.helpers.JsonNodeBuilders.object;

import java.net.URLEncoder;
import java.net.URLDecoder;

import nl.utwente.hmi.middleware.MiddlewareListener;
import nl.utwente.hmi.middleware.worker.AbstractWorker;
import nl.utwente.hmi.middleware.Middleware;
import nl.utwente.hmi.middleware.loader.*;
import lombok.extern.slf4j.Slf4j;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;
import asap.middlewareadapters.communication.BMLProcessor;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Implements an adapter between a RealizerPort and a middleware
 * The adapter receives bml requests from the middleware through a configured port 
 * passes the requests on to the RealizerPort set at construction time; and sends any feedback
 * that it receives from the RealizerPort back over the middleware through the specified channel 
 * @author Daniel
 */
@Slf4j
public class MiddlewareToBMLRealizerAdapter implements BMLFeedbackListener, MiddlewareListener
{
    /** The RealizerPort to which the BML requests must be redirected. */
    private final RealizerPort realizerPort;
    
    private BMLProcessor bmlWorker;

	private MiddlewareListener listener;

	private Middleware middleware;

    public MiddlewareToBMLRealizerAdapter(RealizerPort rp, String specificMiddlewareLoader, Properties props) // String externalbmlport, String internalbmlport, String internalfeedbackport)
    {
    	bmlWorker = new BMLProcessor(rp);
    	new Thread(bmlWorker).start();
    	
        
        GenericMiddlewareLoader gml = new GenericMiddlewareLoader(specificMiddlewareLoader, props);
        middleware = gml.load();
		middleware.addListener(this);
				
        realizerPort = rp;
        realizerPort.addListeners(this);        
       
    }

  	/**
	 * Callback method which is called by the Middleware when a new data package arrives
	 * @param jn the recieved data in JSON format
	 */
	public void receiveData(JsonNode jn)
    {
		bmlWorker.addDataToQueue(jn);
        //bmlWorker.processData(d);
    }

    @Override
    public void feedback(String feedback)
    {
        try {
	    	JsonNode jn = object("feedback",object("content", URLEncoder.encode(feedback,"UTF-8"))).end();
	    	
	    	middleware.sendData(jn);

        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }
}