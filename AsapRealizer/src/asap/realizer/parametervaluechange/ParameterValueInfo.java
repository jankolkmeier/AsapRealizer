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
package asap.realizer.parametervaluechange;

import net.jcip.annotations.Immutable;

/**
 * Provides information on the parameter id and desired values of a parameter value change
 */
@Immutable
public final class ParameterValueInfo
{
    private final String targetId;    
    private final String targetBmlId;
    private final String paramId;
    private final float initialValue;
    private final float targetValue;
    private final boolean hasInitialValue;
    
    private ParameterValueInfo(String targetBmlId, String targetId, String paramId, float initialValue, float targetValue,boolean hasInitialValue)
    {
        this.targetId = targetId;
        this.paramId = paramId;
        this.targetValue = targetValue;
        this.targetBmlId = targetBmlId;
        this.initialValue = initialValue;
        this.hasInitialValue = hasInitialValue;
    }
    
    public ParameterValueInfo(String targetBmlId, String targetId, String paramId, float targetValue)
    {
        this(targetBmlId, targetId, paramId, 0, targetValue,false);        
    }
    
    public ParameterValueInfo(String targetBmlId, String targetId, String paramId, float initialValue, float targetValue)
    {
        this(targetBmlId, targetId, paramId, initialValue, targetValue,true);        
    }

    public String getTargetId()
    {
        return targetId;
    }

    public String getTargetBmlId()
    {
        return targetBmlId;
    }

    public String getParamId()
    {
        return paramId;
    }

    public float getInitialValue()
    {
        return initialValue;
    }

    public float getTargetValue()
    {
        return targetValue;
    }

    public boolean hasInitialValue()
    {
        return hasInitialValue;
    }
}
