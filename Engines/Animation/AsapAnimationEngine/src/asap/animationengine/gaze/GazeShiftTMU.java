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

import lombok.extern.slf4j.Slf4j;
import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.motionunit.MUPlayException;
import asap.motionunit.TMUPlayException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

/**
 * Runs the TimedAnimationUnit, ends with setting the new rest pose gaze. 
 * @author Herwin
 */
@Slf4j
public class GazeShiftTMU extends TimedAnimationMotionUnit
{
    private final RestGaze restGaze;
    private final AnimationPlayer aniPlayer;
    private final GazeMU gmu;
    
    public GazeShiftTMU(FeedbackManager bfm, BMLBlockPeg bmlBlockPeg, String bmlId, String id, GazeMU m, PegBoard pb,
            RestGaze restGaze, AnimationPlayer ap)
    {
        super(bfm, bmlBlockPeg, bmlId, id, m, pb, ap);
        this.restGaze = restGaze;
        this.aniPlayer = ap;
        this.gmu = m;
    }
    
    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {
        double t = (time-getStartTime())/(getEndTime()-getStartTime());
        double tReady = getMotionUnit().getKeyPosition("ready").time;
        t *= tReady;
        try
        {
            log.debug("Timed Motion Unit play {}", time);
            getMotionUnit().play(t);
        }
        catch (MUPlayException ex)
        {
            throw new TMUPlayException(ex.getLocalizedMessage(), this, ex);
        }
        sendProgress(t, time);
    }
    
    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException
    {
        try
        {
            double endTime = getTime("end");
            gmu.setStartPose();

            double duration;
            if (endTime != TimePeg.VALUE_UNKNOWN)
            {
                duration = endTime - getStartTime();
            }
            else
            {
                duration = gmu.getPreferedReadyDuration();
                if (getEndTime() != TimePeg.VALUE_UNKNOWN && getStartTime() != TimePeg.VALUE_UNKNOWN)
                {
                    duration = getEndTime() - getStartTime();                    
                }
            }            
            gmu.setDurations(duration, duration);
        }
        catch (MUPlayException ex)
        {
            throw new TMUPlayException(ex.getLocalizedMessage(), this, ex);
        }
        super.startUnit(time);        
    }    
    
    @Override
    public double getPreferedDuration()
    {
        return gmu.getPreferedReadyDuration();
    }
    
    @Override
    protected void stopUnit(double time)
    {
        super.stopUnit(time);
        aniPlayer.setRestGaze(restGaze);
    }

}
