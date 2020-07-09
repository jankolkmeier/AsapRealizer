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
package asap.bml.ext.murml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import org.junit.Test;

import saiba.bml.BMLInfo;
import saiba.bml.core.AbstractBehaviourTest;
import saiba.bml.core.Behaviour;
import saiba.bml.core.GestureBehaviour;
import asap.bml.ext.bmlt.BMLTInfo;

/**
 * Unit test cases for the MURML gesture behaviour
 * @author hvanwelbergen
 * 
 */
public class MURMLGestureBehaviourTest extends AbstractBehaviourTest
{
    static
    {
        BMLTInfo.init();
    }

    @Override
    protected Behaviour createBehaviour(String bmlId, String extraAttributeString) throws IOException
    {
        String bmlString = "<murmlgesture xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" " + extraAttributeString
                + "id=\"a1\" start=\"nod1:end\">" + "<murml-description><dynamic><keyframing><phase><frame ftime=\"0\"><posture>Humanoid "
                + "(l_shoulder 3 70 0 0)</posture></frame></phase></keyframing></dynamic></murml-description>" + "</murml:murmlgesture>";
        return new MURMLGestureBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    @Override
    protected Behaviour parseBehaviour(String bmlId, String bmlString) throws IOException
    {
        return new MURMLGestureBehaviour(bmlId, new XMLTokenizer(bmlString));
    }

    private static final double FRAME_PRECISION = 0.0001;

    @Test
    public void testReadKeyframe() throws IOException
    {
        String bmlString = "<murmlgesture xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" " + "id=\"a1\" start=\"nod1:end\">"
                + "<murml-description><dynamic><keyframing><phase><frame ftime=\"0\"><posture>Humanoid "
                + "(l_shoulder 3 70 0 0)</posture></frame></phase></keyframing></dynamic></murml-description>" + "</murml:murmlgesture>";
        MURMLGestureBehaviour beh = new MURMLGestureBehaviour("bmla", new XMLTokenizer(bmlString));
        assertEquals("bmla", beh.getBmlId());
        assertEquals("a1", beh.id);

        assertEquals(0, beh.getMurmlDescription().getDynamic().getKeyframing().getPhases().get(0).getFrames().get(0).getFtime(),
                FRAME_PRECISION);
    }

    @Test
    public void testExtension() throws IOException
    {
        BMLInfo.addDescriptionExtension(MURMLGestureBehaviour.xmlTag(), MURMLGestureBehaviour.class);
        BMLInfo.supportedExtensions.add(MURMLGestureBehaviour.class);
        String murmlString = "<murmlgesture xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" " + "id=\"a1\">"
                + "<murml-description><dynamic><keyframing><phase><frame ftime=\"0\"><posture>Humanoid "
                + "(dB_Smile 3 70 0 0)</posture></frame></phase></keyframing></dynamic></murml-description>" + "</murml:murmlgesture>";
        String bmlString = "<gesture xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"a1\" " + "lexeme=\"BEAT\">"
                + "<description priority=\"1\" type=\"murmlgesture\">" + murmlString + "</description></gesture>";
        GestureBehaviour f = new GestureBehaviour("bmla", new XMLTokenizer(bmlString));
        assertThat(f.descBehaviour, instanceOf(MURMLGestureBehaviour.class));
        MURMLGestureBehaviour beh = (MURMLGestureBehaviour) f.descBehaviour;
        assertEquals("bmla", beh.getBmlId());
        assertEquals("a1", beh.id);

        assertEquals(0, beh.getMurmlDescription().getDynamic().getKeyframing().getPhases().get(0).getFrames().get(0).getFtime(),
                FRAME_PRECISION);
    }

    @Test
    public void testPriority() throws IOException
    {
        String bmlString = "<murmlgesture xmlns:bmla=\"http://www.asap-project.org/bmla\" bmla:priority=\"10\" xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" "
                + "id=\"a1\" start=\"nod1:end\">"
                + "<murml-description><dynamic><keyframing><phase><frame ftime=\"0\"><posture>Humanoid "
                + "(l_shoulder 3 70 0 0)</posture></frame></phase></keyframing></dynamic></murml-description>" + "</murml:murmlgesture>";
        MURMLGestureBehaviour beh = new MURMLGestureBehaviour("bmla", new XMLTokenizer(bmlString));
        assertEquals(10, beh.getFloatParameterValue("http://www.asap-project.org/bmla:priority"), FRAME_PRECISION);
        assertEquals(10, beh.getMurmlDescription().getPriority(), FRAME_PRECISION);
    }
    
    @Test
    public void testWriteXML() throws IOException
    {
        String bmlString = "<murmlgesture xmlns:bmla=\"http://www.asap-project.org/bmla\" bmla:priority=\"10\" xmlns=\"http://www.techfak.uni-bielefeld.de/ags/soa/murml\" "
                + "id=\"a1\" start=\"nod1:end\">"
                + "<murml-description><dynamic><keyframing><phase><frame ftime=\"0\"><posture>Humanoid "
                + "(l_shoulder 3 70 0 0)</posture></frame></phase></keyframing></dynamic></murml-description>" + "</murml:murmlgesture>";
        MURMLGestureBehaviour behIn = new MURMLGestureBehaviour("bml1", new XMLTokenizer(bmlString));
        StringBuilder buf = new StringBuilder();
        behIn.appendXML(buf, new XMLFormatting(), "bmla", "http://www.asap-project.org/bmla");
        System.out.println(buf);
        
        MURMLGestureBehaviour behOut = new MURMLGestureBehaviour("bml1", new XMLTokenizer(buf.toString()));
        assertEquals("bml1", behOut.getBmlId());
        assertEquals("a1", behOut.id);
        assertNotNull(behOut.getMurmlDescription());
        assertEquals(0,behOut.getMurmlDescription().getDynamic().getKeyframing().getPhases().get(0).getFrames().get(0).getFtime(),FRAME_PRECISION);
    }
}
