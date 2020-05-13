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
import nl.utwente.hmi.middleware.stomp.STOMPMiddleware;
import nl.utwente.hmi.middleware.worker.AbstractWorker;

import java.net.URLEncoder;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class BMLProcessor extends AbstractWorker {
	private static Logger logger = LoggerFactory.getLogger(BMLProcessor.class.getName());

	private RealizerPort rp;

	public BMLProcessor(RealizerPort rp){
		this.rp = rp;
	}
	
	@Override
	public void processData(JsonNode jn) {
        try{
            String bml = jn.path("bml").path("content").asText();
            if("".equals(bml)){
            	logger.error("Got malformed bml request through middleware, dropping behavior: {}",jn.toString());
            } else {
            	rp.performBML(URLDecoder.decode(bml,"UTF-8"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            //System.exit(0);
        }
	}

}
