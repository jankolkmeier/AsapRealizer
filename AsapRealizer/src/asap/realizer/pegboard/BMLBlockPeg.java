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
package asap.realizer.pegboard;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * Peg that refers to the absolute time of the start of BMLBlock with id id.
 * @author welberge
 */
@ThreadSafe
public final class BMLBlockPeg
{
    public static final String GLOBAL_PEG_ID = "global";
    public static final String ANTICIPATOR_PEG_ID = "anticipators";
    public static final BMLBlockPeg GLOBALPEG = new BMLBlockPeg(GLOBAL_PEG_ID, 0);  
    
    private final String id;
    
    @GuardedBy("this")
    private double value;
    
    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    public BMLBlockPeg(String id, double v)
    {
        this.id = id;
        value = v;
    }
    
    /**
     * get the value of the SynchronisationPoint
     * @return the value of the SynchronisationPoint
     */
    public synchronized double getValue()
    {
        return value;
    }
    
    /**
     * Set the value of the SynchronisationPoint.
     * @param value the new value
     */
    public synchronized void setValue(double value)
    {
        this.value = value;
    }
    
    @Override
    public synchronized String toString()
    {
        return "Time peg with value "+getValue();
    }
}
