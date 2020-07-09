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

import static asap.testutil.bml.feedback.FeedbackAsserts.assertOneFeedback;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import hmi.util.Clock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;
import asap.realizerport.util.BMLFeedbackManager;
import asap.realizerport.util.ListBMLFeedbackListener;
/**
 * Unit tests for the LoggingRealizerBridge
 * @author Herwin
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(LoggerFactory.class)
public class LoggingRealizerBridgeTest
{
    private LoggingRealizerBridge logBridge;
    private StubInputBridge inputBridge;
    private StubOutputBridge outputBridge;
    private List<BMLSyncPointProgressFeedback> fbList;
    private Clock mockSchedulingClock = mock(Clock.class);
    Logger mockLogger = mock(Logger.class);
    
    
    private static class StubOutputBridge implements RealizerPort
    {
        private BMLFeedbackManager feedbackManager = new BMLFeedbackManager();
        private String performedBML;
        
        public void sendFeedback(String feedback)
        {
            feedbackManager.sendFeedback(feedback);
        }
        
        public String getPerformedBML()
        {
            return performedBML;
        }

        @Override
        public void addListeners(BMLFeedbackListener... bmlListeners)
        {
            feedbackManager.addListeners(bmlListeners);
        }

        @Override
        public void performBML(String bmlString)
        {
            performedBML = bmlString;
        }

        @Override
        public void removeAllListeners()
        {
            feedbackManager.removeAllListeners();
        }

        @Override
        public void removeListener(BMLFeedbackListener l)
        {
            feedbackManager.removeListener(l);            
        }
    }
    
    private static class StubInputBridge implements RealizerPort
    {
        private RealizerPort outBridge;
        public StubInputBridge(RealizerPort outBridge)
        {
            this.outBridge = outBridge;
        }
        
        @Override
        public void addListeners(BMLFeedbackListener... listeners)
        {
            outBridge.addListeners(listeners);            
        }

        @Override
        public void performBML(String bmlString)
        {
            outBridge.performBML(bmlString);            
        }

        @Override
        public void removeAllListeners()
        {
            outBridge.removeAllListeners();            
        }

        @Override
        public void removeListener(BMLFeedbackListener l)
        {
            outBridge.removeListener(l);            
        }        
    }
    
    
    @Before
    public void setup()
    {
        PowerMockito.mockStatic(LoggerFactory.class);
        when(LoggerFactory.getLogger(anyString())).thenReturn(mockLogger);
        
        outputBridge = new StubOutputBridge();
        logBridge = new LoggingRealizerBridge(mockLogger, outputBridge, mockSchedulingClock);
        inputBridge = new StubInputBridge(logBridge);
        fbList = new ArrayList<BMLSyncPointProgressFeedback>();
        inputBridge.addListeners(new ListBMLFeedbackListener.Builder().feedBackList(fbList).build());        
    }
    
    @Test
    public void testLogBMLRequest()
    {
        final String bml = "<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml1\"/>";
        inputBridge.performBML(bml);
        assertEquals(bml,outputBridge.getPerformedBML());
        
        // XXX: this is a bit too specific, any .info function could be called 
        //(also one with different arguments)
        verify(mockLogger,times(1)).info(contains(bml));        
    }
    
    @Test
    public void testLogFeedback()
    {
        BMLSyncPointProgressFeedback spp = new BMLSyncPointProgressFeedback("bml1", "beh1", "sync1", 0, 0);
        outputBridge.sendFeedback(spp.toXMLString());
        assertOneFeedback(spp,fbList);
        
        final String logString = spp.toXMLString();
        verify(mockLogger,times(1)).info(logString);        
    }
}
