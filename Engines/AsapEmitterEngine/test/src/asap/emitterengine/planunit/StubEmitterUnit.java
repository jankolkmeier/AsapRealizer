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
package asap.emitterengine.planunit;

import lombok.Delegate;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;

/**
 * Testing stub for the EmitterUnit
 * @author Herwin
 *
 */
public class StubEmitterUnit implements EmitterUnit
{
    @Delegate
    private KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();  
    
    public void cleanup()
    {
        
    }

    public TimedEmitterUnit createTEU(FeedbackManager bfm, BMLBlockPeg bbPeg,String bmlId,String id)
    {
        return new TimedEmitterUnit(bfm,bbPeg,bmlId,id,this);
    }
    
    @Override
    public void setFloatParameterValue(String name, float value)
    {
    }
    
    @Override
    public boolean hasValidParameters()
    {
        return true;
    }

    @Override
    public void setParameterValue(String name, String value)
    {
    }

    @Override
    public String getParameterValue(String name)
    {
        return null;
    }

    @Override
    public void play(double t)
    {

    }    

    @Override
    public double getPreferedDuration()
    {
        return 0;
    }

    @Override
    public float getFloatParameterValue(String name)
    {
        return 0;
    }

    @Override
    public void startUnit(double t)
    {
                
    }
}
