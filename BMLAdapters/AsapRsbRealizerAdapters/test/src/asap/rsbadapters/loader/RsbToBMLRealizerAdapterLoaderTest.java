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
package asap.rsbadapters.loader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import hmi.util.Clock;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * Unit tests for the RsbToBMLRealizerAdapterLoader
 * @author Herwin
 *
 */
public class RsbToBMLRealizerAdapterLoaderTest
{
    private RealizerPort mockRealizerPort = mock(RealizerPort.class);
    private Clock mockSchedulingClock = mock(Clock.class);
    
    @Test
    public void testReadFromXML() throws IOException
    {
        String pipeLoaderStr = "<PipeLoader id=\"id1\" loader=\"x\"/>";
        XMLTokenizer tok = new XMLTokenizer(pipeLoaderStr);
        tok.takeSTag("PipeLoader");
        RsbToBMLRealizerAdapterLoader loader = new RsbToBMLRealizerAdapterLoader();
        loader.readXML(tok, "id1", "vh1", "name", mockRealizerPort, mockSchedulingClock);
        verify(mockRealizerPort, times(1)).addListeners(any(BMLFeedbackListener[].class));
        assertEquals(mockRealizerPort, loader.getAdaptedRealizerPort());
    }
}
