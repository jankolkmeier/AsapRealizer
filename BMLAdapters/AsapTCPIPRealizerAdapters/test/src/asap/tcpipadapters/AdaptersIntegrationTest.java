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
package asap.tcpipadapters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * Integration tests for the BMLToTCPIP and TCPIPToBML adapters.
 * @author Herwin
 *
 */
@Ignore //somehow these tests do not work reliably on the Linux server
public class AdaptersIntegrationTest
{
    private RealizerPort mockRealizerPort = mock(RealizerPort.class);
    private BMLFeedbackListener mockFeedbackListener = mock(BMLFeedbackListener.class);
    private BMLRealizerToTCPIPAdapter bmlToTCPIP = new BMLRealizerToTCPIPAdapter();
    private TCPIPToBMLRealizerAdapter tcpIpToBML = new TCPIPToBMLRealizerAdapter(mockRealizerPort,6500,6501);
    
    @After
    public void tearDown()
    {
        bmlToTCPIP.shutdown();
        tcpIpToBML.shutdown();   
        while(bmlToTCPIP.isConnected()){}
        while(tcpIpToBML.isConnectedToClient()){}
    }
    
    @Before
    public void before()
    {
        bmlToTCPIP.connect(new ServerInfo("localhost",6500,6501));
        while(!bmlToTCPIP.isConnected());
    }
    
    @Test
    public void testPerformBML() throws InterruptedException
    {
        String bmlString = "<bml id=\"bml1\"></bml>";
        bmlToTCPIP.performBML(bmlString);        
        Thread.sleep(500);
        verify(mockRealizerPort).performBML(bmlString);        
    }
    
    @Test
    public void testFeedback() throws InterruptedException
    {
        bmlToTCPIP.addListeners(mockFeedbackListener);
        String fbString = "<blockProgress id=\"bml1:end\" globalTime=\"15\" characterId=\"doctor\"></blockProgress>";
        Thread.sleep(500);        
        tcpIpToBML.feedback(fbString);
        Thread.sleep(500);
        verify(mockFeedbackListener).feedback(fbString);
    }
}
