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
package asap.animationengine.ace.lmp;

import java.util.Set;

import lombok.Setter;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.TimedPlanUnitPlayException;

/**
 * LMP testing stub
 * @author hvanwelbergen
 * 
 */
public class StubLMP extends LMP
{
    private final Set<String> kinematicJoints;
    private final Set<String> physicalJoints;

    @Setter
    private double prepDuration;

    @Setter
    private double retrDuration;
    
    @Setter
    private double strokeDuration;    
    
    public StubLMP(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId, PegBoard pegBoard, Set<String> kinematicJoints,
            Set<String> physicalJoints, double prepDuration, double retrDuration, double strokeDuration)
    {
        super(fbm, bmlPeg, bmlId, behId, pegBoard);
        this.kinematicJoints = kinematicJoints;
        this.physicalJoints = physicalJoints;
        this.prepDuration = prepDuration;
        this.retrDuration = retrDuration;
        this.strokeDuration = strokeDuration;        
    }

   

    @Override
    public Set<String> getKinematicJoints()
    {
        return kinematicJoints;
    }

    @Override
    public Set<String> getPhysicalJoints()
    {
        return physicalJoints;
    }

    @Override
    public double getPreparationDuration()
    {
        return prepDuration;
    }

    @Override
    public double getRetractionDuration()
    {
        return retrDuration;
    }

    @Override
    public double getStrokeDuration()
    {
        return strokeDuration;
    }

    @Override
    public boolean hasValidTiming()
    {
        return true;
    }

    @Override
    protected void setInternalStrokeTiming(double time)
    {

    }

    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException
    {

    }

    @Override
    protected void stopUnit(double time) throws TimedPlanUnitPlayException
    {

    }
}
