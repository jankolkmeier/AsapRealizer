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
package asap.realizertestutil.planunit;


import java.util.ArrayList;
import java.util.List;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanUnitFloatParameterNotFoundException;
import asap.realizer.planunit.PlanUnitParameterNotFoundException;
import asap.realizer.planunit.TimedAbstractPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;

/**
 * Stub implementation of a TimedAbstractPlanUnit
 * @author Herwin
 */
public class StubPlanUnit extends TimedAbstractPlanUnit
{
    private final double endTime,startTime;
    private TimePeg startPeg;
    private TimePeg endPeg;
    
    public StubPlanUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId,double startTime, double endTime)
    {
        super(fbm, bmlPeg, bmlId, behId);
        this.startTime = startTime;
        this.endTime = endTime;
        startPeg = new TimePeg(bmlPeg);startPeg.setGlobalValue(startTime);
        endPeg = new TimePeg(bmlPeg);endPeg.setGlobalValue(endTime);
    }

    public StubPlanUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId)
    {
        this(fbm,bmlPeg,bmlId,behId,0,1);
    }
    
    @Override
    protected void playUnit(double time) throws TimedPlanUnitPlayException{}
    
    @Override
    protected void stopUnit(double time) throws TimedPlanUnitPlayException{}
    
    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException{}
    
    @Override
    public double getEndTime(){return endTime;}
    
    @Override 
    public double getRelaxTime(){return getEndTime();}
    
    @Override
    public double getStartTime(){return 0;}

    @Override
    public boolean hasValidTiming()
    {
        return true;
    }

    @Override
    public double getTime(String syncId)
    {
        return startTime;
    }

    @Override
    public TimePeg getTimePeg(String syncId)
    {
        if(syncId.equals("start"))return startPeg;
        if(syncId.equals("end"))return endPeg;
        return null;
    }

    @Override
    public List<String> getAvailableSyncs()
    {
        return new ArrayList<String>();
    }

    @Override
    public void setTimePeg(String syncId, TimePeg peg)
    {
                    
    }

    @Override
    public void setParameterValue(String paramId, String value){}
    
    
    @Override
    public void setFloatParameterValue(String paramId, float value)
            throws PlanUnitFloatParameterNotFoundException
    {
    }

    @Override
    public float getFloatParameterValue(String paramId) throws PlanUnitFloatParameterNotFoundException
    {
        return 0;
    }

    @Override
    public String getParameterValue(String paramId) throws PlanUnitParameterNotFoundException
    {
        return null;
    }        
}