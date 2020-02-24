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
package asap.animationengine.motionunit;

import lombok.Getter;
import asap.realizer.planunit.TimedPlanUnitSetupException;

/**
 * A failure occurred when constructing tmu
 * @author hvanwelbergen
 *
 */
public class TMUSetupException extends TimedPlanUnitSetupException
{
    private static final long serialVersionUID = 1L;
    
    @Getter
    private final TimedAnimationUnit tmu;
    
    public TMUSetupException(String str, TimedAnimationUnit m)
    {
        super(str, m);
        tmu = m;        
    }
    
    
    public TMUSetupException(String str, TimedAnimationUnit m, Exception ex)
    {
        super(str,m,ex);
        tmu = m;        
    }
}