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
package asap.middlewareadapters.communication;

import asap.realizerport.RealizerPort;
import asap.middlewareadapters.BMLRealizerToMiddlewareAdapter;
import nl.utwente.hmi.middleware.worker.AbstractWorker;

import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;

import com.fasterxml.jackson.databind.JsonNode;

public class FeedbackProcessor extends AbstractWorker {
	private static Logger logger = LoggerFactory.getLogger(FeedbackProcessor.class.getName());

	private BMLRealizerToMiddlewareAdapter rp;

	public FeedbackProcessor(BMLRealizerToMiddlewareAdapter rp){
		this.rp = rp;
	}
	
	//TODO: empty bottle might cause a problem here
	@Override
	public void processData(JsonNode jn) {
        try{
            String feedback = jn.path("feedback").path("content").asText();
            if("".equals(feedback)){
            	logger.error("Got malformed feedback through middleware, dropping feedback: {}", jn.toString());
            } else {
            	rp.sendFeedback(URLDecoder.decode(feedback,"UTF-8"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            //System.exit(0);
        }
	}

}
