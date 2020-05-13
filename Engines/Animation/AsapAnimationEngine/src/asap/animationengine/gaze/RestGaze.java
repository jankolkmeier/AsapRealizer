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

import java.util.Set;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.AnimationUnit;
import asap.animationengine.motionunit.MUSetupException;
import asap.animationengine.motionunit.TMUSetupException;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.ParameterException;

/**
 * The restgaze is a dynamic controller that handles all animation
 * related to the gaze to and from the rest position. Only one restgaze is active at a time, 
 * and it should normally be played after all other motion units are executed
 * @author hvanwelbergen
 */
public interface RestGaze
{
    RestGaze copy(AnimationPlayer player);

    void setAnimationPlayer(AnimationPlayer player);
    
    /**
     * Play the rest pose at time time, given the kinematicJoints and physicalJoint that are in use     
     */
    void play(double time, Set<String>kinematicJoints, Set<String>physicalJoints);
    
    /**
     * Create a transition TMU that moves the joints from their current position 
     * to a position dictated by this resting pose.  
     */
    TimedAnimationMotionUnit createTransitionToRest(GazeInfluence influence, FeedbackManager fbm, double startTime, 
            String bmlId, String id, BMLBlockPeg bmlBlockPeg, PegBoard pb) throws TMUSetupException;
    
    /**
     * Create a transition TMU that moves the joints from their current position 
     * to a position dictated by this resting pose.  
     */
    TimedAnimationMotionUnit createTransitionToRest(GazeInfluence influence, FeedbackManager fbm, double startTime, double duration, 
            String bmlId, String id, BMLBlockPeg bmlBlockPeg, PegBoard pb) throws TMUSetupException;
    
    /**
     * Create a transition TMU that moves the joints from their current position 
     * to a position dictated by this resting pose.  
     */
    TimedAnimationMotionUnit createTransitionToRest(GazeInfluence influence, FeedbackManager fbm, TimePeg startPeg, TimePeg endPeg,
            String bmlId, String id, BMLBlockPeg bmlBlockPeg, PegBoard pb) throws TMUSetupException;
    
    double getTransitionToRestDuration(GazeInfluence influence);
    
    /**
     * Create a MotionUnit that moves the joints from their current position 
     * to a position dictated by this resting pose.  
     */
    AnimationUnit createTransitionToRest(GazeInfluence influence) throws MUSetupException;
    
    void setParameterValue(String name, String value) throws ParameterException;
    
    void setFloatParameterValue(String name, float value) throws ParameterException;
    
    GazeShiftTMU createGazeShiftTMU(FeedbackManager bbf, BMLBlockPeg bmlBlockPeg, 
            String bmlId, String id, PegBoard pb) throws MUSetupException;
    
    Set<String> getKinematicJoints();    
}
