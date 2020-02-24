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
package asap.animationengine.gaze;

import static org.mockito.Mockito.mock;
import hmi.animation.Hanim;
import hmi.animation.VJoint;
import hmi.math.Vec3f;
import hmi.testutil.animation.HanimBody;
import hmi.worldobjectenvironment.VJointWorldObject;
import hmi.worldobjectenvironment.WorldObject;
import hmi.worldobjectenvironment.WorldObjectManager;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.AnimationPlayerMock;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitSetupException;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;
import asap.realizertestutil.util.TimePegUtil;

@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
"org.slf4j.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class GazeShiftTest extends AbstractTimedPlanUnitTest
{
    private VJoint vCurr = HanimBody.getLOA1HanimBody();
    private VJoint vNext = HanimBody.getLOA1HanimBody();
    private PegBoard pegBoard = new PegBoard();
    private AnimationPlayer mockAnimationPlayer = AnimationPlayerMock.createAnimationPlayerMock(vCurr, vNext);
    
    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
            throws TimedPlanUnitSetupException
    {
        TweedGazeMU mu = new TweedGazeMU();
        WorldObjectManager woManager = new WorldObjectManager();
        VJoint bluebox = new VJoint();
        bluebox.setTranslation(Vec3f.getVec3f(1, 1, 1));
        WorldObject blueBox = new VJointWorldObject(bluebox);
        woManager.addWorldObject("bluebox", blueBox);
        mu.player = mockAnimationPlayer;
        mu.target = "bluebox";
        mu.woManager = woManager;
        mu.neck = vNext.getPartBySid(Hanim.skullbase);
        
        RestGaze mockRestGaze = mock(RestGaze.class);
        GazeShiftTMU tmu = new GazeShiftTMU(bfm, bbPeg, bmlId, id, mu, pegBoard, mockRestGaze,mockAnimationPlayer);
        tmu.setTimePeg("start", TimePegUtil.createTimePeg(bbPeg, startTime));
        return tmu;
    }

    @Test
    @Ignore
    public void testSetStrokePeg() throws TimedPlanUnitSetupException
    {
        
    }
}
