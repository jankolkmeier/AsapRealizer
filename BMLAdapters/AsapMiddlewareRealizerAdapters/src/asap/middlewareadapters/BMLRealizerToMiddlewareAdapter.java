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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import nl.utwente.hmi.middleware.loader.*;
import nl.utwente.hmi.middleware.MiddlewareListener;
import nl.utwente.hmi.middleware.Middleware;
import nl.utwente.hmi.middleware.loader.GenericMiddlewareLoader;
import lombok.extern.slf4j.Slf4j;
import saiba.bml.feedback.BMLWarningFeedback;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;
import asap.middlewareadapters.communication.FeedbackProcessor;

import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;

/**
 * Submits BML through a Middleware via Data, any feedback will be directly forwarded to registered listeners
 * @quthor Daniel
 */
@Slf4j
public class BMLRealizerToMiddlewareAdapter implements RealizerPort, MiddlewareListener
{
    private List<BMLFeedbackListener> feedbackListeners = Collections.synchronizedList(new ArrayList<BMLFeedbackListener>());
	private FeedbackProcessor feedbackWorker;
	private MiddlewareListener listener;
	private Middleware middleware;
    
    public BMLRealizerToMiddlewareAdapter(String specificMiddlewareLoader, Properties props)//String bmlport, String feedbackport)
    {
    	feedbackWorker = new FeedbackProcessor(this);
    	new Thread(feedbackWorker).start();

        GenericMiddlewareLoader gml = new GenericMiddlewareLoader(specificMiddlewareLoader, props);
        middleware = gml.load();
		middleware.addListener(this);
    }

  	/**
	 * Callback method which is called by the Middleware when a new data package arrives
	 * @param d the recieved data
	 */
	public void receiveData(JsonNode jn)
    {
		feedbackWorker.processData(jn);
    }

    /**
     * Sends the specified feedback to all registered listeners
     * @param feedback the feedback string
     */
    public void sendFeedback(String feedback)
    {
        synchronized (feedbackListeners)
        {
        	try{
	            for (BMLFeedbackListener fbl : feedbackListeners)
	            {
	                fbl.feedback(feedback);
	            }
        	}
            catch (Exception ex)
            {
                // general catch because broken listeners should not crash this component
                //log.warn("Error sending BMLFeedback to Listeners", ex);
            }
        }
    }

    @Override
    public void addListeners(BMLFeedbackListener... listeners)
    {
        feedbackListeners.addAll(ImmutableList.copyOf(listeners));
    }

    @Override
    public void removeAllListeners()
    {
        feedbackListeners.clear();
    }

    @Override
    public void removeListener(BMLFeedbackListener l)
    {
        feedbackListeners.remove(l);        
    }   
    
    @Override
    public void performBML(String bml)
    {
    	try {
			JsonNode value = object("bml", object("content", URLEncoder.encode(bml,"UTF-8"))).end();
            middleware.sendData(value);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
   }    
}