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
package asap.realizerembodiments;

import hmi.util.Clock;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.realizer.bridge.LogPipe;
import asap.realizerport.RealizerPort;

/**
 * LogPipeLoader has one element as child: <Log> with optional attributes requestlog and feedbacklog
 */
public class LogPipeLoader implements PipeLoader
{

    private LogPipe adaptedRealizerPort = null;

    /**
     * @throws XMLScanException
     */
    @Override
    public void readXML(XMLTokenizer theTokenizer, String id, String vhId, String name, RealizerPort realizerPort,
            Clock theSchedulingClock) throws IOException
    {
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        HashMap<String, String> attrMap = null;

        if (!theTokenizer.atSTag("Log")) throw new XMLScanException("LogPipeLoader can have only one <Log> child element");

        attrMap = theTokenizer.getAttributes();
        String requestLog = adapter.getOptionalAttribute("requestlog", attrMap);
        String feedbackLog = adapter.getOptionalAttribute("feedbacklog", attrMap);
        Logger rl = null;
        Logger fl = null;
        if (requestLog != null)
        {
            rl = LoggerFactory.getLogger(requestLog);
        }
        if (feedbackLog != null)
        {
            fl = LoggerFactory.getLogger(feedbackLog);
        }
        adaptedRealizerPort = new LogPipe(rl, fl, realizerPort, theSchedulingClock);
        theTokenizer.takeSTag("Log");
        theTokenizer.takeETag("Log");
        if (!theTokenizer.atETag("PipeLoader")) throw new XMLScanException("LogPipeLoader can have only one <Log> child element");
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
