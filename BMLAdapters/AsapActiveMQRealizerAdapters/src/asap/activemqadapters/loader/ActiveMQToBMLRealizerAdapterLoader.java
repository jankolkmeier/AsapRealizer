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
package asap.activemqadapters.loader;

import hmi.util.Clock;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;
import hmi.xml.XMLStructureAdapter;

import java.io.IOException;
import java.util.HashMap;

import javax.jms.JMSException;

import lombok.extern.slf4j.Slf4j;
import asap.activemqadapters.*;
import asap.realizerembodiments.PipeLoader;
import asap.realizerport.RealizerPort;

/**
 * Loader for and ActiveMQToRealizerAdapter
 * @author Dennis Reidsma
 * @author Herwin
 */
@Slf4j
public class ActiveMQToBMLRealizerAdapterLoader implements PipeLoader
{
    private RealizerPort adaptedRealizerPort = null;
    private XMLStructureAdapter adapter = new XMLStructureAdapter();
    
    /**
     * @throws XMLScanException
     */
    @Override
    public void readXML(XMLTokenizer theTokenizer, String id, String vhId, String name, RealizerPort realizerPort, Clock theSchedulingClock)
            throws IOException
    {
        try
        {
            if (theTokenizer.atSTag("Properties"))
            {
                HashMap<String, String> attrMap = theTokenizer.getAttributes();
                String bmlTopic = adapter.getOptionalAttribute("bmlTopic", attrMap, AMQBMLConstants.BML) ;
                String feedbackTopic = adapter.getOptionalAttribute("feedbackTopic", attrMap, AMQBMLConstants.BML_FEEDBACK) ;
                String amqBrokerURI = adapter.getOptionalAttribute("amqBrokerURI", attrMap, "tcp://127.0.0.1:61616") ;
                theTokenizer.takeSTag("Properties");
                theTokenizer.takeETag("Properties");
                new ActiveMQToBMLRealizerAdapter(realizerPort,feedbackTopic,bmlTopic,amqBrokerURI);
            } 
            else {
                new ActiveMQToBMLRealizerAdapter(realizerPort);
            }
        }
        catch (JMSException e)
        {
            log.error("Error registering at the ActiveMQ network: {}", e);
            throw new XMLScanException("Error registering at the ActiveMQ network");
        }
        adaptedRealizerPort = realizerPort;
        if (!theTokenizer.atETag("PipeLoader")) throw new XMLScanException("ActiveMQPipeLoader is an empty element");
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
