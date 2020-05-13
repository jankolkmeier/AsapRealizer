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
package asap.activemqadapters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import lombok.extern.slf4j.Slf4j;
import saiba.bml.feedback.BMLWarningFeedback;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;
import java.io.IOException;
import java.util.HashMap;

import com.google.common.collect.ImmutableList;

/**
 * Submits BML through activemq messages; submits received feedback to registered listeners.
 * @author Dennis Reidsma
 * @author Herwin
 */
@Slf4j
public class BMLRealizerToActiveMQAdapter implements RealizerPort, AMQConnectionListener
{
    private List<BMLFeedbackListener> feedbackListeners = Collections.synchronizedList(new ArrayList<BMLFeedbackListener>());

    private AMQConnection amqConnection = null;

    private String feedbackTopic;
    private String bmlTopic;

    public BMLRealizerToActiveMQAdapter()
    {
        this(AMQBMLConstants.BML,
             AMQBMLConstants.BML_FEEDBACK,
             "tcp://127.0.0.1:61616"
             );
    }
    public BMLRealizerToActiveMQAdapter(String sendTopic, String receiveTopic, String amqBrokerURI)
    {
        bmlTopic=sendTopic;
        feedbackTopic=receiveTopic;
        amqConnection = new AMQConnection("BMLRealizerToActiveMQAdapter", new String[] { bmlTopic },
                new String[] { feedbackTopic }, amqBrokerURI);
        amqConnection.addListeners(this);
    }

    /* the message will be bml feedback... so send it on to any listeners */
    @Override
    public void onMessage(TextMessage m)
    {
        // TODO: figure out whether it is guaranteed that onMessage is called in-order on one thread
        // (this, because we want to be sure that the messages are also redirected to the listeners in-order)
        String msg = "";
        try
        {
            msg = m.getText();
            try
            {
                sendFeedback(msg);
            }
            catch (Exception ex)
            {
                // general catch because broken listeners should not crash this component
                log.warn("Error sending BMLFeedback to Listeners", ex);
            }
        }
        catch (JMSException ex)
        {
            throw new RuntimeException("Cannot get text from message");
        }
    }

    private void sendFeedback(String feedback)
    {
        synchronized (feedbackListeners)
        {
            for (BMLFeedbackListener fbl : feedbackListeners)
            {
                fbl.feedback(feedback);
            }
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
            amqConnection.sendMessage(bmlTopic, bmlString);
        }
        catch (JMSException e)
        {
            // log failure; send feedback to BML Listeners
            BMLWarningFeedback feedback = new BMLWarningFeedback("no id", "CANNOT_SEND", "Failure to send BML over ActiveMQ.");
            try
            {
                sendFeedback(feedback.toXMLString());
            }
            catch (Exception ex)
            {
                // general catch because broken listeners should not crash this component
                log.warn("Error sending BMLFeedback to Listeners", ex);
            }
        }
    }

     
}
