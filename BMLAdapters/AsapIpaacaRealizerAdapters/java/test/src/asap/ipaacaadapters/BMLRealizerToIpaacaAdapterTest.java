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

import static org.hamcrest.Matchers.allOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import ipaaca.Initializer;
import ipaaca.InputBuffer;
import ipaaca.LocalIU;
import ipaaca.OutputBuffer;
import ipaaca.util.ComponentNotifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit test for the BMLRealizerToIpaacaAdapter
 * @author hvanwelbergen
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ BMLRealizerToIpaacaAdapter.class, Initializer.class, InputBuffer.class, OutputBuffer.class })
public class BMLRealizerToIpaacaAdapterTest
{
    private final InputBuffer mockInputBuffer = mock(InputBuffer.class);
    private final OutputBuffer mockOutputBuffer = mock(OutputBuffer.class);
    private BMLRealizerToIpaacaAdapter adapter;
    
    
    @Before
    public void setup() throws Exception
    {
        whenNew(InputBuffer.class).withArguments(any(String.class), any(Set.class)).thenReturn(mockInputBuffer);
        whenNew(OutputBuffer.class).withArguments(any(String.class)).thenReturn(mockOutputBuffer);
        PowerMockito.mockStatic(Initializer.class);
        adapter = new BMLRealizerToIpaacaAdapter();
    }

    @Test
    public void testInit() throws Exception
    {
        Map<String,String> expectedPayload = new HashMap<>();
        expectedPayload.put(ComponentNotifier.RECEIVE_CATEGORIES,IpaacaBMLConstants.BML_FEEDBACK_KEY);
        expectedPayload.put(ComponentNotifier.SEND_CATEGORIES,IpaacaBMLConstants.BML_KEY);
        expectedPayload.put(ComponentNotifier.STATE, "new");
        expectedPayload.put(ComponentNotifier.NAME, "BMLToIpaacaRealizerAdapter");
        expectedPayload.put(ComponentNotifier.FUNCTION, "bmlprovider");
        verify(mockOutputBuffer, times(1)).add(argThat(allOf(new IUCategoryMatcher<LocalIU>(ComponentNotifier.NOTIFY_CATEGORY), 
                new IUPayloadMatcher<LocalIU>(expectedPayload))));
    }
    
    @Test
    public void testPerformBML()
    {
        String bmlString = "<bml id=\"bml1\"/>";
        adapter.performBML(bmlString);
        Map<String,String> expectedPayload = new HashMap<>();
        expectedPayload.put(IpaacaBMLConstants.BML_KEY,bmlString);
        verify(mockOutputBuffer,times(1)).add(argThat(allOf(new IUCategoryMatcher<LocalIU>(IpaacaBMLConstants.BML_CATEGORY)
                ,new IUPayloadMatcher<LocalIU>(expectedPayload))));
    }
}
