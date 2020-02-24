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
package asap.bml.ext.bmla;

import saiba.bml.core.BMLBlockComposition;
import saiba.bml.core.CoreComposition;

/**
 * APPEND_AFTER: start when a selected set of previously sent behavior is finished<br> 
 * @author welberge
 */
public enum BMLASchedulingMechanism implements BMLBlockComposition
{
    UNKNOWN, REPLACE, MERGE, APPEND, APPEND_AFTER;
    
    @Override
    public String getNameStart()
    {
        switch(this)
        {
        case APPEND_AFTER: return "APPEND-AFTER";
        default: return toCoreSchedulingMechanism().getNameStart();
        }        
    }    
    
    private CoreComposition toCoreSchedulingMechanism()
    {
        switch(this)
        {
        case UNKNOWN: return CoreComposition.UNKNOWN;
        case REPLACE: return CoreComposition.REPLACE;
        case MERGE: return CoreComposition.MERGE;
        case APPEND: return CoreComposition.APPEND;
        default: return CoreComposition.UNKNOWN;
        }
    }
    
    public static BMLASchedulingMechanism parse(String input)
    {
        for(BMLASchedulingMechanism mech: BMLASchedulingMechanism.values())
        {
            if(mech.getNameStart().equals(input))
            {
                return mech;
            }
        }
        return UNKNOWN;
    } 
}
