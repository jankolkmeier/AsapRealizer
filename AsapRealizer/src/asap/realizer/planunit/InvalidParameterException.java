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
 * Generic parameter exception (e.g. in the combination of paramId and value or in the value of value) 
 * @author welberge
 */
public class InvalidParameterException extends ParameterException
{
    private static final long serialVersionUID = -8642635217080796975L;
    private final String paramId;
    private final String value;
    public String getParamId()
    {
        return paramId;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public InvalidParameterException(String param, String value)
    {
        super("InvalidParameter "+param+"="+value);
        this.paramId = param;
        this.value = value;
    }
    
    public InvalidParameterException(String param, String value, Exception e)
    {
        this(param,value);
        initCause(e);
    }
    
    public InvalidParameterException(String message, String param, String value)
    {
        super(message);
        this.paramId = param;
        this.value = value;
    }
}
