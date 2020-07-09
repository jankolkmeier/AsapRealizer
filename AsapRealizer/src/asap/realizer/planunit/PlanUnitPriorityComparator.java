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


import java.io.Serializable;
import java.util.Comparator;

import asap.realizer.pegboard.TimePeg;

/**
 * Compares two TimedPlanUnits by priority, then by start time
 * @author hvanwelbergen
 */
public class PlanUnitPriorityComparator implements Comparator<TimedPlanUnit>, Serializable
{
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(TimedPlanUnit pu1, TimedPlanUnit pu2)
    {
        if (pu1.getPriority() < pu2.getPriority()) return 1;
        if (pu1.getPriority() > pu2.getPriority()) return -1;
        if (pu1.getStartTime() == TimePeg.VALUE_UNKNOWN || pu1.getStartTime() == TimePeg.VALUE_UNKNOWN) return 0;
        if (pu1.getStartTime() > pu2.getStartTime()) return -1;
        if (pu1.getStartTime() < pu2.getStartTime()) return 1;
        return 0;
    }
}
