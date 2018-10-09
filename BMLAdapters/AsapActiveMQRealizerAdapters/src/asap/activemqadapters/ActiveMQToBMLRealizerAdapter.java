/*******************************************************************************
 *******************************************************************************/
package asap.activemqadapters;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * This component is the adaptor between a RealizerPort and the ActiveMQ middleware.
 * The adapter receives bml requests from the middleware through the channel "asap.bml.request";
 * passes the requests on to the RealizerPort set at construction time; and sends any feedback
 * that it receives from the RealizerPort back over the middleware through the channel "asap.bml.feedback".
 * @author Reidsma
 * @author welberge
 */
@Slf4j
public class ActiveMQToBMLRealizerAdapter implements BMLFeedbackListener, AMQConnectionListener
{
    /** Used to receive BML requests over the activemq network, and to send feedback */
    private final AMQConnection amqConnection;

    /** The RealizerPort to which the BML requests must be redirected. */
    private final RealizerPort realizerPort;

    private String feedbackTopic;
    private String bmlTopic;
    
    public ActiveMQToBMLRealizerAdapter(RealizerPort rp) throws JMSException
    {
        this(rp,
             AMQBMLConstants.BML_FEEDBACK ,
             AMQBMLConstants.BML,
             "tcp://127.0.0.1:61616"
             );
    }
    public ActiveMQToBMLRealizerAdapter(RealizerPort rp, String sendTopic, String receiveTopic, String amqBrokerURI) throws JMSException
    {
        bmlTopic=receiveTopic;
        feedbackTopic=sendTopic;
        amqConnection = new AMQConnection("BMLRealizerToActiveMQAdapter", new String[] {sendTopic}, new String[] {receiveTopic}, amqBrokerURI);
        amqConnection.addListeners(this);
        realizerPort = rp;
        realizerPort.addListeners(this);        
    }

    @Override
    public void feedback(String feedback)
    {
        try
        {
            amqConnection.sendMessage(feedbackTopic, feedback);
        }
        catch (JMSException e)
        {
            log.error("error sending feedback over activemq");
        }
    }

    @Override
    public void onMessage(TextMessage m)
    {
        // TODO: figure out whether it is guaranteed that onMessage is called in-order on one thread
        // (this, because we want to be sure that the BML messages are also redirected to the RealizerPort in-order)
        String msg = "";
        try
        {
            msg = m.getText();
            try
            {
                realizerPort.performBML(msg);
            }
            catch (Exception ex)
            {
                log.error("Error sending BML to realizer", ex);
            }
        }
        catch (JMSException ex)
        {
            throw new RuntimeException("Cannot get text from input message in activemqtobmlrealizeradapter");
        }

    }
}
