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
package asap.ipaacaeventengine;

import ipaaca.LocalMessageIU;
import ipaaca.OutputBuffer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps track of outputbuffers to send messages on the right scope
 * @author herwinvw
 *
 */
public class MessageManager
{
    private Map<String, OutputBuffer> outputbuffers = new ConcurrentHashMap<>();
    private final String id;

    public MessageManager(String id)
    {
        this.id = id;
    }
    
    private OutputBuffer getOutBuffer(String channel)
    {
        return outputbuffers.computeIfAbsent(channel, x -> new OutputBuffer(id, channel));
    }

    public void sendMessage(LocalMessageIU message)
    {
        sendMessage(message, "default");
    }

    public void sendMessage(LocalMessageIU message, String channel)
    {
        getOutBuffer(channel).add(message);
    }

    public void close()
    {
        outputbuffers.forEach((k, v) -> v.close());
    }
}
