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
package asap.picture.planunit;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;
import asap.realizertestutil.util.KeyPositionMocker;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Unit tests for the TimedPictureUnit
 * @author Herwin
 *
 */
@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*",
    "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
    "org.slf4j.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest({BMLBlockManager.class})
public class TimedPictureUnitTest extends AbstractTimedPlanUnitTest
{
    private PictureUnit mockPictureUnit = mock(PictureUnit.class);
    
    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        KeyPositionMocker.stubKeyPositions(mockPictureUnit, new KeyPosition("start", 0, 1), new KeyPosition("end", 1, 1));
        TimedPictureUnit tpu = new TimedPictureUnit(bfm, bbPeg, bmlId, id, mockPictureUnit);
        tpu.setTimePeg("start", TimePegUtil.createTimePeg(bbPeg, startTime));
        return tpu;
    }
    
    @Override
    @Test
    public void testSubsiding()
    {
        
    }
    
    @Override
    @Test
    public void testSetStrokePeg()
    {
        
    }
}
