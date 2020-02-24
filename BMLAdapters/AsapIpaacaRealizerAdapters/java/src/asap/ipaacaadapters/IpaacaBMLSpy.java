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
package asap.ipaacaadapters;

import ipaaca.AbstractIU;
import ipaaca.HandlerFunctor;
import ipaaca.IUEventHandler;
import ipaaca.IUEventType;
import ipaaca.Initializer;
import ipaaca.InputBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * Listens in on both Ipaaca feedback and bml messages
 * @author hvanwelbergen
 * 
 */
public class IpaacaBMLSpy implements RealizerPort
{
    static
    {
        Initializer.initializeIpaacaRsb();
    }

    private final InputBuffer inBuffer;
    private List<BMLFeedbackListener> feedbackListeners = Collections.synchronizedList(new ArrayList<BMLFeedbackListener>());
    private List<RealizerPort> realizerPorts = Collections.synchronizedList(new ArrayList<RealizerPort>());

    public IpaacaBMLSpy(String characterId)
    {
        if (characterId != null)
        {
            inBuffer = new InputBuffer("IpaacaBMLSpy", ImmutableSet.of(IpaacaBMLConstants.BML_CATEGORY,
                    IpaacaBMLConstants.BML_FEEDBACK_CATEGORY), characterId);
        }
        else
        {
            inBuffer = new InputBuffer("IpaacaBMLSpy", ImmutableSet.of(IpaacaBMLConstants.BML_CATEGORY,
                    IpaacaBMLConstants.BML_FEEDBACK_CATEGORY));
        }

        EnumSet<IUEventType> types = EnumSet.of(IUEventType.ADDED, IUEventType.MESSAGE);
        inBuffer.registerHandler(new IUEventHandler(new HandlerFunctor()
        {
            @Override
            public void handle(AbstractIU iu, IUEventType type, boolean local)
            {
                synchronized (feedbackListeners)
                {
                    for (BMLFeedbackListener l : feedbackListeners)
                    {
                        l.feedback(iu.getPayload().get(IpaacaBMLConstants.BML_FEEDBACK_KEY));
                    }
                }
            }
        }, types, ImmutableSet.of(IpaacaBMLConstants.BML_FEEDBACK_CATEGORY)));

        inBuffer.registerHandler(new IUEventHandler(new HandlerFunctor()
        {
            @Override
            public void handle(AbstractIU iu, IUEventType type, boolean local)
            {
                for (RealizerPort realizerPort : realizerPorts)
                {
                    realizerPort.performBML(iu.getPayload().get(IpaacaBMLConstants.BML_KEY));
                }
            }
        }, types, ImmutableSet.of(IpaacaBMLConstants.BML_CATEGORY)));
    }

    public IpaacaBMLSpy()
    {
        this(null);
    }

    public void addRealizerPort(RealizerPort rp)
    {
        realizerPorts.add(rp);
    }

    public void removeAllRealizerPorts()
    {
        realizerPorts.clear();
    }

    @Override
    public void addListeners(BMLFeedbackListener... listeners)
    {
        feedbackListeners.addAll(ImmutableList.copyOf(listeners));
    }

    @Override
    public void removeListener(BMLFeedbackListener l)
    {
        feedbackListeners.remove(l);

    }

    @Override
    public void removeAllListeners()
    {
        feedbackListeners.clear();

    }

    @Override
    public void performBML(String bmlString)
    {

    }

    public void close()
    {
        inBuffer.close();
    }
}
