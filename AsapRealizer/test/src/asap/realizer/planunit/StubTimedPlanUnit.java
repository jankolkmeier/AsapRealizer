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
package asap.realizer.planunit;

import lombok.Delegate;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;

/**
 * Testing stub for TimedPlanUnits
 * @author hvanwelbergen
 * 
 */
public class StubTimedPlanUnit extends TimedAbstractPlanUnit
{
    private double prefDuration;
    public static final double READY_RELATIVE_TIME = 0.25;
    public static final double STROKE_RELATIVE_TIME = 0.5;
    public static final double RELAX_RELATIVE_TIME = 0.75;
    
    public StubTimedPlanUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId, double pd)
    {
        super(fbm, bmlPeg, bmlId, behId);
        prefDuration = pd;

        KeyPositionManagerImpl kp = new KeyPositionManagerImpl();
        puTimeManager = new PlanUnitTimeManager(kp);
        kp.addKeyPosition(new KeyPosition("ready",READY_RELATIVE_TIME));
        kp.addKeyPosition(new KeyPosition("stroke",STROKE_RELATIVE_TIME));
        kp.addKeyPosition(new KeyPosition("relax",RELAX_RELATIVE_TIME));
        resolveGestureKeyPositions();        
    }

    @Delegate
    protected final PlanUnitTimeManager puTimeManager;

    @Override
    public double getPreferedDuration()
    {
        return prefDuration;
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
