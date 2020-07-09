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

/**
 * Float parameter with was not found
 * @author Herwin
 *
 */
public class PlanUnitFloatParameterNotFoundException extends ParameterException
{
    private static final long serialVersionUID = 1L;
    private final String behaviorId,bmlId,parameterId;
    public PlanUnitFloatParameterNotFoundException(String bmlId, String behId, String paramId)
    {
        super("Float parameter "+paramId+" not found on "+bmlId+":"+behId);
        behaviorId = behId;
        this.bmlId = bmlId;
        parameterId = paramId;        
    }
    
    public PlanUnitFloatParameterNotFoundException(String bmlId, String behId, String paramId, Exception cause)
    {
        this(bmlId,behId,paramId);
        this.initCause(cause);
    }
    public String getBehaviorId()
    {
        return behaviorId;
    }
    public String getBmlId()
    {
        return bmlId;
    }
    public String getParameterId()
    {
        return parameterId;
    }
}
