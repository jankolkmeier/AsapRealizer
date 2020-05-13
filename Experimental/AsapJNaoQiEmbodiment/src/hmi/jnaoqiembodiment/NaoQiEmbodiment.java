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
package hmi.jnaoqiembodiment;

import hmi.environmentbase.Embodiment;

import com.aldebaran.proxy.ALBehaviorManagerProxy;
import com.aldebaran.proxy.ALLedsProxy;
import com.aldebaran.proxy.DCMProxy;

/**
 * Manages all NaoQi Proxies.
 * Some things one should know:
 * You can load behaviors onto the Nao using the behavior manager from choregraphe.
 * These behaviors can be played/activated through the behaviormanagerproxy. It takes several seconds
 * for a behavior to become activated. You can circumvent this by preloading a behavior. Preloading takes a few seconds; but then the behavior can be activated immediately.
 * However. After playing the behavior, it should be preloaded *again*.
 * @author welberge
 * @author Dennis Reidsma
 */
public class NaoQiEmbodiment implements Embodiment
{
    private final String ip;
    private final int port;
    private DCMProxy dcmProxy;
    private ALLedsProxy ledsProxy;
    private ALBehaviorManagerProxy behaviorProxy;
    private String id = "";

    static
    {
        System.loadLibrary("JNaoQi");
    }

    public NaoQiEmbodiment(String id, String ip, int port)
    {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public NaoQiEmbodiment(String ip, int port)
    {
        this("", ip, port);
    }

    public DCMProxy getDCMProxy()
    {
        if (dcmProxy == null)
        {
            dcmProxy = new DCMProxy(ip, port);
        }
        return dcmProxy;
    }

    public ALLedsProxy getLedsProxy()
    {
        if (ledsProxy == null)
        {
            ledsProxy = new ALLedsProxy(ip, port);
        }
        return ledsProxy;
    }

    public ALBehaviorManagerProxy getBehaviorManagerProxy()
    {
        if (behaviorProxy == null)
        {
            behaviorProxy = new ALBehaviorManagerProxy(ip, port);
        }
        return behaviorProxy;
    }

    @Override
    public String getId()
    {
        return id;
    }
}
