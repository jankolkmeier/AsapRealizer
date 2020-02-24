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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.AnimationPlayerMock;
import asap.animationengine.motionunit.MUSetupException;
import asap.animationengine.motionunit.StubAnimationUnit;
import asap.animationengine.motionunit.TMUSetupException;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.motionunit.MUPlayException;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.TimePegAndConstraint;
import asap.realizertestutil.planunit.AbstractTimedPlanUnitTest;
import asap.realizertestutil.util.TimePegUtil;
import hmi.animation.VJoint;
import hmi.math.Vec3f;
import hmi.testutil.animation.HanimBody;
import hmi.worldobjectenvironment.VJointWorldObject;
import hmi.worldobjectenvironment.WorldObject;
import hmi.worldobjectenvironment.WorldObjectManager;
import saiba.bml.core.GazeBehaviour;
import saiba.bml.parser.Constraint;

/**
 * Unit test cases for the GazeTMU
 * @author hvanwelbergen
 */
@PowerMockIgnore({ "javax.management.*", "javax.xml.parsers.*", "com.sun.org.apache.xerces.internal.jaxp.*", "ch.qos.logback.*",
        "org.slf4j.*" })
@RunWith(PowerMockRunner.class)
@PrepareForTest(BMLBlockManager.class)
public class GazeTMUTest extends AbstractTimedPlanUnitTest
{
    private VJoint vCurr = HanimBody.getLOA1HanimBodyWithEyes();
    private VJoint vNext = HanimBody.getLOA1HanimBodyWithEyes();
    private AnimationPlayer mockAnimationPlayer = AnimationPlayerMock.createAnimationPlayerMock(vCurr, vNext);
    private PegBoard pegBoard = new PegBoard();
    private static final double TIME_PRECISION = 0.0001;
    private GazeBehaviour mockBeh = mock(GazeBehaviour.class);

    private class StubGazeMU extends StubAnimationUnit implements GazeMU
    {
        private static final double STAY_DURATION = 1;
        private static final double RELAX_DURATION = 2;
        private static final double READY_DURATION = 3;
        private double stayDuration, relaxDuration, readyDuration;
        private static final double RELATIVE_READY_TIME = 0.25;
        private static final double RELATIVE_RELAX_TIME = 0.75;   
        
        public StubGazeMU()
        {
            stayDuration = STAY_DURATION;
            readyDuration = READY_DURATION;
            relaxDuration = RELAX_DURATION;
            
            KeyPosition ready = new KeyPosition("ready", RELATIVE_READY_TIME, 1);
            KeyPosition relax = new KeyPosition("relax", RELATIVE_RELAX_TIME, 1);
            addKeyPosition(ready);
            addKeyPosition(relax);
            addKeyPosition(new KeyPosition("start", 0, 1));
            addKeyPosition(new KeyPosition("end", 1, 1));
        }

        @Override
        public StubGazeMU copy(AnimationPlayer p)
        {
            super.copy(p);
            return this;
        }
        
        @Override
        public double getPreferedStayDuration()
        {
            return stayDuration;
        }

        @Override
        public double getPreferedRelaxDuration()
        {
            return relaxDuration;
        }

        @Override
        public double getPreferedReadyDuration()
        {
            return readyDuration;
        }

        @Override
        public double getPreferedDuration()
        {
            return stayDuration+relaxDuration+readyDuration;
        }
        
        @Override
        public void setDurations(double prepDur, double relaxDur)
        {
            this.readyDuration = prepDur;
            this.relaxDuration = relaxDur;

        }

        @Override
        public void setEndRotation(float[] gazeDir)
        {
        }

        @Override
        public void setStartPose() throws MUPlayException
        {
        }

        @Override
        public void setTarget()
        {
        }

        @Override
        public GazeInfluence getInfluence()
        {
            return GazeInfluence.EYES;
        }
    }

    private GazeTMU setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId)
            throws TMUSetupException, MUSetupException
    {
        // TweedGazeMU mu = new TweedGazeMU();
        // DynamicGazeMU mu = new DynamicGazeMU();
        //DynamicGazeMU
        StubGazeMU mu = new StubGazeMU();

        WorldObjectManager woManager = new WorldObjectManager();
        VJoint bluebox = new VJoint();
        bluebox.setTranslation(Vec3f.getVec3f(0, 0, 1));
        WorldObject blueBox = new VJointWorldObject(bluebox);
        woManager.addWorldObject("bluebox", blueBox);

        mu = mu.copy(mockAnimationPlayer);
        //mu.target = "bluebox";
        //mu.woManager = woManager;

        RestGaze mockRestGaze = mock(RestGaze.class);
        TimedAnimationMotionUnit mockTMU = mock(TimedAnimationMotionUnit.class);
        when(mockAnimationPlayer.getGazeTransitionToRestDuration(GazeInfluence.EYES)).thenReturn(2d);
        when(mockAnimationPlayer.getRestGaze()).thenReturn(mockRestGaze);
        when(mockRestGaze.createTransitionToRest(any(GazeInfluence.class), any(FeedbackManager.class), any(TimePeg.class), any(TimePeg.class), anyString(),
                anyString(), any(BMLBlockPeg.class), eq(pegBoard))).thenReturn(mockTMU);

        return new GazeTMU(bfm, bbPeg, bmlId, id, mu, pegBoard, mockAnimationPlayer);
    }

    @Override
    protected GazeTMU setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
            throws TMUSetupException
    {
        GazeTMU tmu;
        try
        {
            tmu = setupPlanUnit(bfm, bbPeg, id, bmlId);
        }
        catch (MUSetupException e)
        {
            throw new RuntimeException(e);
        }
        tmu.setTimePeg("start", TimePegUtil.createTimePeg(bbPeg, startTime));
        return tmu;
    }

    @Test
    @Override
    public void testSetStrokePeg()
    {

    }

    @Test
    public void testResolve() throws BehaviourPlanningException, TMUSetupException, MUSetupException
    {
        GazeTMU tmu = setupPlanUnit(fbManager, BMLBlockPeg.GLOBALPEG, "gaze1", "bml1");
        List<TimePegAndConstraint> sacs = new ArrayList<>();
        sacs.add(new TimePegAndConstraint("start", TimePegUtil.createTimePeg(1), new Constraint(), 0));
        sacs.add(new TimePegAndConstraint("ready", TimePegUtil.createTimePeg(TimePeg.VALUE_UNKNOWN), new Constraint(), 0));
        sacs.add(new TimePegAndConstraint("end", TimePegUtil.createTimePeg(TimePeg.VALUE_UNKNOWN), new Constraint(), 0));
        tmu.resolveSynchs(BMLBlockPeg.GLOBALPEG, mockBeh, sacs);
        assertEquals(1, tmu.getStartTime(), TIME_PRECISION);
        assertThat(tmu.getTime("ready"), greaterThan(1d));
        assertThat(tmu.getTime("end"), greaterThan(1d));
    }

    @Test
    public void testStart() throws TMUSetupException, BehaviourPlanningException, TimedPlanUnitPlayException, MUSetupException
    {
        GazeTMU tmu = setupPlanUnit(fbManager, BMLBlockPeg.GLOBALPEG, "gaze1", "bml1");
        List<TimePegAndConstraint> sacs = new ArrayList<>();
        sacs.add(new TimePegAndConstraint("start", TimePegUtil.createTimePeg(1), new Constraint(), 0));
        sacs.add(new TimePegAndConstraint("ready", TimePegUtil.createTimePeg(3), new Constraint(), 0));
        tmu.resolveSynchs(BMLBlockPeg.GLOBALPEG, mockBeh, sacs);
        tmu.setState(TimedPlanUnitState.LURKING);
        tmu.start(1);
        assertEquals(1, tmu.getStartTime(), TIME_PRECISION);
        assertEquals(3, tmu.getTime("ready"), TIME_PRECISION);
        assertThat(tmu.getTime("relax"), greaterThan(3d));
        assertThat(tmu.getTime("end"), greaterThan(tmu.getTime("ready") + 1.95));
    }

    @Test
    public void testStartUnit() throws TMUSetupException, MUSetupException, TimedPlanUnitPlayException
    {
        GazeTMU tmu = setupPlanUnit(fbManager, BMLBlockPeg.GLOBALPEG, "gaze1", "bml1");
        tmu.setTimePeg("start", TimePegUtil.createTimePeg(0));
        tmu.startUnit(0);
        assertEquals(tmu.getTime("ready"), StubGazeMU.READY_DURATION, TIME_PRECISION);
        assertEquals(tmu.getTime("relax"), StubGazeMU.READY_DURATION+StubGazeMU.STAY_DURATION, TIME_PRECISION);
        assertEquals(tmu.getTime("end"), StubGazeMU.READY_DURATION+StubGazeMU.STAY_DURATION+StubGazeMU.RELAX_DURATION, TIME_PRECISION);
    }
    
    @Test
    public void testStartUnitWithRelax() throws TMUSetupException, MUSetupException, TimedPlanUnitPlayException
    {
        GazeTMU tmu = setupPlanUnit(fbManager, BMLBlockPeg.GLOBALPEG, "gaze1", "bml1");
        tmu.setTimePeg("start", TimePegUtil.createTimePeg(0));
        tmu.setTimePeg("relax", TimePegUtil.createTimePeg(10));
        tmu.startUnit(0);
        assertEquals(StubGazeMU.READY_DURATION, tmu.getTime("ready"), TIME_PRECISION);
        assertEquals(10, tmu.getTime("relax"), TIME_PRECISION);
        assertEquals(10+StubGazeMU.RELAX_DURATION, tmu.getTime("end"),  TIME_PRECISION);
    }
}
