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
package asap.picture.picturebinding;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.core.Behaviour;
import saiba.bml.core.FaceLexemeBehaviour;
import asap.picture.bml.SetImageBehavior;
import asap.picture.display.PictureDisplay;
import asap.picture.planunit.AddAnimationXMLPU;
import asap.picture.planunit.PictureUnit;
import asap.picture.planunit.SetImagePU;
import asap.picture.planunit.TimedPictureUnit;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.ParameterException;

/**
 * Unit tests for the picturebinding
 * @author Herwin
 * 
 */
public class PictureBindingTest
{
    private PictureDisplay mockPictureDisplay = mock(PictureDisplay.class);
    private PictureBinding binding = new PictureBinding(mockPictureDisplay);
    private FeedbackManager mockFeedbackManager = mock(FeedbackManager.class);
    private double PARAM_PRECISION = 0.001d;
    @Before
    public void setupBinding()
    {
        //@formatter:off
        String bindingStr =
        "<picturebinding>"+
        "<PictureUnitSpec type=\"setImage\" namespace=\"http://hmi.ewi.utwente.nl/pictureengine\">"+
        "        <constraints>"+
        "        </constraints>"+
        "        <parametermap>"+
        "            <parameter src=\"filePath\" dst=\"filePath\"/>"+
        "            <parameter src=\"fileName\" dst=\"fileName\"/>"+
        "        </parametermap>"+
        "        <PictureUnit type=\"SetImagePU\"/>"+
        "    </PictureUnitSpec> "+
        "    <PictureUnitSpec type=\"faceLexeme\">"+
        "        <constraints>"+
        "            <constraint name=\"lexeme\" value=\"smile\"/>"+
        "        </constraints>"+
        "        <parametermap>"+
        "        </parametermap>"+
        "        <parameterdefaults>"+
        "            <parameterdefault name=\"filePath\" value=\"pictureengine/example/animations/\"/>"+
        "            <parameterdefault name=\"fileName\" value=\"smile.xml\"/>"+
        "            <parameterdefault name=\"layer\" value=\"8\"/>"+
        "        </parameterdefaults>"+
        "        <PictureUnit type=\"AddAnimationXMLPU\"/>"+
        "    </PictureUnitSpec>"+
        "</picturebinding>";
        //@formatter:on
        binding.readXML(bindingStr);
    }
    
    @Test
    public void testSetImage() throws IOException, ParameterException
    {
        String bmlString = "<setImage id=\"img1\" xmlns=\"http://hmi.ewi.utwente.nl/pictureengine\" filePath=\"fp\" fileName=\"fn\"/>";
        XMLTokenizer tok = new XMLTokenizer(bmlString);
        SetImageBehavior beh = new SetImageBehavior("bml1", tok);
        List<TimedPictureUnit> tpu = binding.getPictureUnit(mockFeedbackManager, BMLBlockPeg.GLOBALPEG, beh);
        assertEquals(1,tpu.size());
        assertThat(tpu.get(0).pu, instanceOf(SetImagePU.class));
        assertEquals("fp/", tpu.get(0).pu.getParameterValue("filePath"));
        assertEquals("fn", tpu.get(0).pu.getParameterValue("fileName"));
    }
    
    @Test
    public void testFaceLexeme() throws IOException, ParameterException
    {
        String bmlString = "<faceLexeme id=\"face1\" xmlns=\""+Behaviour.BMLNAMESPACE+"\" lexeme=\"smile\" />";
        XMLTokenizer tok = new XMLTokenizer(bmlString);
        FaceLexemeBehaviour beh = new FaceLexemeBehaviour("bml1",tok);
        List<TimedPictureUnit> tpu = binding.getPictureUnit(mockFeedbackManager, BMLBlockPeg.GLOBALPEG, beh);
        assertEquals(1,tpu.size());
        PictureUnit pu = tpu.get(0).pu; 
        assertThat(pu, instanceOf(AddAnimationXMLPU.class));
        assertEquals(8,pu.getFloatParameterValue("layer"),PARAM_PRECISION);        
    }
}
