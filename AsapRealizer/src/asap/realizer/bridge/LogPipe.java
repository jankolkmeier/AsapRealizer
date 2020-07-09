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
package asap.realizer.bridge;

import hmi.util.Clock;

import org.slf4j.Logger;

import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;


/**
 * A LogPipe can be put between two bridges to log their communication. 
 * It logs feedback from its output bridge and logs BML requests from its input bridge.
 * On construction, it is provided with a Elckerlyc.SchedulingClock; the timestamps from this clock
 * are logged together with the requests.
 * THIS IS THE NEWER VERSION OF LOGGINGREALIZERBRIDGE
 * @author welberge
 * @author reidsma
 */
public class LogPipe implements RealizerPort, BMLFeedbackListener
{
    private final Logger requestLogger;
    private final Logger feedbackLogger;
    private final RealizerPort outputBridge;
    private final Clock schedulingClock;
    private final boolean logRequests;
    private final boolean logFeedback;
    
    public LogPipe(Logger rl, Logger fl, RealizerPort outBridge, Clock clock)
    {
        this.requestLogger = rl;
        this.feedbackLogger = fl;
        this.outputBridge = outBridge;
        this.schedulingClock = clock;
        this.logRequests = rl!=null;
        this.logFeedback = fl!=null;
        outputBridge.addListeners(this);        
    }

    @Override
    public void addListeners(BMLFeedbackListener... listeners)
    {
        outputBridge.addListeners(listeners);
    }

    @Override
    public void performBML(String bmlString)
    {
      if (logRequests)
      {
        requestLogger.info("<entry name=\"{}\" time=\"{}\">", requestLogger.getName(), schedulingClock.getMediaSeconds());
        requestLogger.info(bmlString);
        requestLogger.info("</entry>");
      }
      outputBridge.performBML(bmlString);
    }

    @Override
    public void removeAllListeners()
    {
        outputBridge.removeAllListeners();        
        outputBridge.addListeners(this); //logger itself needs to remain connected :)
    }

    @Override
    public void removeListener(BMLFeedbackListener l)
    {
        outputBridge.removeListener(l);        
    }  
    
    @Override
    public void feedback(String feedback)
    {
        if(logFeedback)feedbackLogger.info(feedback);
    }

      
}
