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
package asap.bml.ext.ssml.builder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.BMLInfo;
import saiba.bml.core.Behaviour;
import saiba.bml.core.SpeechBehaviour;
import asap.bml.ext.bmlt.BMLTInfo;
import asap.bml.ext.ssml.SSMLBehaviour;

/**
 * Unit tests for SSMLBehaviourBuilder
 * @author hvanwelbergen
 *
 */
public class SSMLBehaviourBuilderTest
{
    @Before
    public void setup()
    {
        BMLTInfo.init();
        BMLInfo.supportedExtensions.add(SSMLBehaviour.class);
    }
    
    @Test
    public void test()
    {
        
        SSMLBehaviourBuilder bb = new SSMLBehaviourBuilder("bml1","beh1");
        bb.ssmlContent("hello world");
        Behaviour beh = bb.build();
        assertThat(beh, instanceOf(SpeechBehaviour.class));
        assertThat(beh.descBehaviour, instanceOf(SSMLBehaviour.class));
        SpeechBehaviour sb = (SpeechBehaviour)beh;
        SSMLBehaviour ssb = (SSMLBehaviour)beh.descBehaviour;
        assertEquals("hello world", sb.getContent());
        assertEquals("hello world", ssb.getContent());
    }
    
    @Test
    public void testWithMark()
    {
        SSMLBehaviourBuilder bb = new SSMLBehaviourBuilder("bml1","beh1");
        bb.ssmlContent("hello <mark name=\"s1\"/> world");
        Behaviour beh = bb.build();
        assertThat(beh, instanceOf(SpeechBehaviour.class));
        assertThat(beh.descBehaviour, instanceOf(SSMLBehaviour.class));
        SpeechBehaviour sb = (SpeechBehaviour)beh;
        SSMLBehaviour ssb = (SSMLBehaviour)beh.descBehaviour;
        assertEquals("hello <sync id=\"s1\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"/>world", sb.getContent());
        assertEquals("hello <mark name=\"s1\"/> world", ssb.getContent());
    }
    
    @Test
    public void testWithMarkAndProsody()
    {
        SSMLBehaviourBuilder bb = new SSMLBehaviourBuilder("bml1","beh1");
        bb.ssmlContent("hello <mark name=\"s1\"/> <prosody rate=\"fast\"/> world");
        Behaviour beh = bb.build();
        assertThat(beh, instanceOf(SpeechBehaviour.class));
        assertThat(beh.descBehaviour, instanceOf(SSMLBehaviour.class));
        SpeechBehaviour sb = (SpeechBehaviour)beh;
        SSMLBehaviour ssb = (SSMLBehaviour)beh.descBehaviour;
        assertEquals("hello <sync id=\"s1\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"/>world", sb.getContent());
        assertEquals("hello <mark name=\"s1\"/> <prosody rate=\"fast\"/> world", ssb.getContent());
    }
}
