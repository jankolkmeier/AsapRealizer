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
package asap.rsbadapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.extern.slf4j.Slf4j;
import rsb.AbstractEventHandler;
import rsb.Event;
import rsb.Factory;
import rsb.Informer;
import rsb.InitializeException;
import rsb.Listener;
import rsb.RSBException;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * Submits BML through rsb messages; submits received feedback (from rsb) to registered listeners.
 * @author Herwin
 */
@Slf4j
public class BMLRealizerToRsbAdapter implements RealizerPort
{
    private final Informer<String> informer;
    private final Listener listener;
    private List<BMLFeedbackListener> feedbackListeners = Collections.synchronizedList(new ArrayList<BMLFeedbackListener>());
    
    public BMLRealizerToRsbAdapter()
    {
        Factory factory = Factory.getInstance();

        try
        {
            // setup bml sender
            informer = factory.createInformer(RsbAdapterConstants.BML_SCOPE);

            // setup feedback receiver
            listener = factory.createListener(RsbAdapterConstants.BML_FEEDBACK_SCOPE);
            listener.addHandler(new AbstractEventHandler()
            {
                @Override
                public void handleEvent(Event event)
                {
                    synchronized (feedbackListeners)
                    {
                        for (BMLFeedbackListener fbl : feedbackListeners)
                        {
                            fbl.feedback(event.getData().toString());
                        }
                    }
                }
            }, true);
        }
        catch (InitializeException ex)
        {
            throw new RuntimeException(ex);
        }
        catch (InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }

        try
        {
            listener.activate();
            informer.activate();
        }
        catch (InitializeException e)
        {
            throw new RuntimeException(e);
        }
        catch (RSBException e)
        {
            throw new RuntimeException(e);
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
    public void performBML(String bmlString)
    {
        try
        {
            informer.send(bmlString);
        }
        catch (RSBException e)
        {
            log.warn("Cannot send BML ", e);
        }
    }

    public void close()
    {
        try
        {
            listener.deactivate();
        }
        catch (RSBException e)
        {
            log.warn("", e);
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }

        try
        {
            informer.deactivate();
        }
        catch (RSBException e)
        {
            log.warn("", e);
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }

    }

}
