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
 * Submits rsb BML messages to a RealizerPort; submits RealizerPort feedbacks to the rsb. 
 * Assumes that the connected realizerport is threadsafe (or at least that its performBML function is).
 * @author Herwin
 */
@Slf4j
public class RsbToBMLRealizerAdapter implements BMLFeedbackListener
{
    private final RealizerPort realizerPort;
    private final Informer<String> informer;
    private final Listener listener;
    
    public RsbToBMLRealizerAdapter(RealizerPort port)
    {
        realizerPort = port;
        realizerPort.addListeners(this);
        Factory factory = Factory.getInstance();


        try
        {
            // setup feedback sender
            informer = factory.createInformer(RsbAdapterConstants.BML_FEEDBACK_SCOPE);
            
            // setup BML receiver
            listener = factory.createListener(RsbAdapterConstants.BML_SCOPE);
            listener.addHandler(new AbstractEventHandler()
            {
                @Override
                public void handleEvent(Event event)
                {
                    realizerPort.performBML(event.getData().toString());
                }
            }, true);
        }
        catch (InitializeException e)
        {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
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

    /**
     * Submit feedback over rsb
     */
    @Override
    public void feedback(String feedback)
    {
        try
        {
            informer.send(feedback);
        }
        catch (RSBException e)
        {
            log.warn("Could not submit feedback over RSB.", e);
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
            log.warn("",e);
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
            log.warn("",e);
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }
    }
}
