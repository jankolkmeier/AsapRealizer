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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Test;

import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;


/**
 * Integration tests for the BMLToIpaaca and IpaacaToBML adapters.
 * Requires a running spread daemon.
 * @author Herwin
 *
 */
public class AdaptersIntegrationTest
{
    private RealizerPort mockRealizerPort = mock(RealizerPort.class);
    private BMLFeedbackListener mockFeedbackListener = mock(BMLFeedbackListener.class);
    private BMLRealizerToIpaacaAdapter bmlToIpaaca = new BMLRealizerToIpaacaAdapter();
    private IpaacaToBMLRealizerAdapter ipaacaToBML = new IpaacaToBMLRealizerAdapter(mockRealizerPort);
    
    @After
    public void tearDown()
    {
        bmlToIpaaca.close();
        ipaacaToBML.close();
    }
    
    @Test
    public void testPerformBML() throws InterruptedException
    {
        bmlToIpaaca.performBML("bmltest");
        Thread.sleep(500);
        verify(mockRealizerPort).performBML("bmltest");
    }
    
    @Test
    public void testPerformBMLCharacter() throws InterruptedException
    {
        bmlToIpaaca = new BMLRealizerToIpaacaAdapter("Fred");
        ipaacaToBML = new IpaacaToBMLRealizerAdapter(mockRealizerPort,"Fred");
        bmlToIpaaca.performBML("bmltest");
        Thread.sleep(500);
        verify(mockRealizerPort).performBML("bmltest");
    }
    
    @Test
    public void testPerformBMLDifferentCharacter() throws InterruptedException
    {
        bmlToIpaaca = new BMLRealizerToIpaacaAdapter("Fred");
        ipaacaToBML = new IpaacaToBMLRealizerAdapter(mockRealizerPort,"Wilma");
        bmlToIpaaca.performBML("bmltest");
        Thread.sleep(500);
        verify(mockRealizerPort,times(0)).performBML("bmltest");
    }
    
    @Test
    public void testFeedback() throws InterruptedException
    {
        bmlToIpaaca.addListeners(mockFeedbackListener);
        ipaacaToBML.feedback("bmlfeedback");
        Thread.sleep(500);
        verify(mockFeedbackListener).feedback("bmlfeedback");
    }
    
    @Test
    public void testFeedbackCharacter() throws InterruptedException
    {
        bmlToIpaaca = new BMLRealizerToIpaacaAdapter("Fred");
        ipaacaToBML = new IpaacaToBMLRealizerAdapter(mockRealizerPort,"Fred");
        bmlToIpaaca.addListeners(mockFeedbackListener);
        ipaacaToBML.feedback("bmlfeedback");
        Thread.sleep(500);
        verify(mockFeedbackListener).feedback("bmlfeedback");
    }
    
    @Test
    public void testNoFeedbackDifferentCharacter() throws InterruptedException
    {
        bmlToIpaaca = new BMLRealizerToIpaacaAdapter("Fred");
        ipaacaToBML = new IpaacaToBMLRealizerAdapter(mockRealizerPort,"Wilma");
        bmlToIpaaca.addListeners(mockFeedbackListener);
        ipaacaToBML.feedback("bmlfeedback");
        Thread.sleep(500);
        verify(mockFeedbackListener, times(0)).feedback("bmlfeedback");
    }
}
