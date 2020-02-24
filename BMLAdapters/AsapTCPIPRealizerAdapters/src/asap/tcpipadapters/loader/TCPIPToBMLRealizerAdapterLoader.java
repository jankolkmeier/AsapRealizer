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
package asap.tcpipadapters.loader;

import hmi.util.Clock;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;
import asap.realizerembodiments.PipeLoader;
import asap.realizerport.RealizerPort;
import asap.tcpipadapters.TCPIPToBMLRealizerAdapter;

/**
 * Loads a TCPIPToBMLRealizerAdapterLoader from XML
 * @author Herwin
 *
 */
public class TCPIPToBMLRealizerAdapterLoader implements PipeLoader
{
    private RealizerPort adaptedRealizerPort = null;
    
    @Getter
    private TCPIPToBMLRealizerAdapter tcpIpAdapter;
    
    @Override
    /**
     * @throws XMLScanException on invalid loader XML
     */
    public void readXML(XMLTokenizer theTokenizer, String id, String vhId, String name, RealizerPort realizerPort, Clock theSchedulingClock)
            throws IOException
    {
        adaptedRealizerPort = realizerPort;
        if(!theTokenizer.atSTag("ServerOptions"))
        {
            throw new XMLScanException("TCPIPToBMLRealizerAdapterLoader requires an inner ServerOptions element");            
        }
        
        HashMap<String, String> attrMap = theTokenizer.getAttributes();
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        String requestPort = adapter.getRequiredAttribute("bmlport", attrMap, theTokenizer);
        String feedbackPort = adapter.getRequiredAttribute("feedbackport", attrMap, theTokenizer);
        tcpIpAdapter = new TCPIPToBMLRealizerAdapter(realizerPort, Integer.parseInt(requestPort), Integer.parseInt(feedbackPort));
        theTokenizer.takeSTag("ServerOptions");
        theTokenizer.takeETag("ServerOptions");        
    }

    @Override
    public RealizerPort getAdaptedRealizerPort()
    {
        return adaptedRealizerPort;
    }

    @Override
    public void shutdown()
    {
        tcpIpAdapter.shutdown();        
    }
}
