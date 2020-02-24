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
package asap.murml;

import java.util.Arrays;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Value class for a joint + DoF values
 * @author hvanwelbergen
 *
 */
@EqualsAndHashCode
public final class JointValue
{
    @Getter
    public final String jointSid;
    
    public final float[] dofs;
    
    public float[] getDofs()
    {
        return Arrays.copyOf(dofs, dofs.length);
    }
    
    public JointValue(String id, float[] dofs)
    {
        this.dofs = Arrays.copyOf(dofs, dofs.length);
        jointSid = id;
    }
    
    public String toString()
    {
        return jointSid+":"+Arrays.toString(dofs);
    }
}
