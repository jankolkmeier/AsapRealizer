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

import javax.annotation.concurrent.GuardedBy;

import net.jcip.annotations.ThreadSafe;

/**
 * AfterPegs define TimePegs that remains at a minimum time offset from a 'regular' TimePeg.<br>
 * that is: afterPeg.getGlobalTime() &gt;= linkedPeg.getGlobalTime()+offset
 * 
 * if the link's value is TimePeg.VALUE_UNKNOWN, it is ignored.
 * 
 * 
 * @author welberge
 */
@ThreadSafe
public final class AfterPeg extends TimePeg
{
    // XXX Should an AfterPeg be (or OffsetPeg) able to move a link that has an absolute time? Currently it does...

    @GuardedBy("this")
    private TimePeg link;

    @GuardedBy("this")
    private double offset = 0;

    public AfterPeg(double o, BMLBlockPeg bmp)
    {
        super(bmp);
        offset = o;
    }

    public synchronized void setOffset(double o)
    {
        offset = o;
    }

    public AfterPeg(TimePeg l, double o)
    {
        super(l.bmlBlockPeg);
        link = l;
        offset = o;
    }

    public AfterPeg(TimePeg l, double o, BMLBlockPeg bmp)
    {
        super(bmp);
        link = l;
        offset = o;
    }

    public synchronized void setLink(TimePeg p)
    {
        link = p;
    }

    @Override
    public synchronized TimePeg getLink()
    {
        return link;
    }

    @Override
    public synchronized double getLocalValue()
    {
        if (super.getLocalValue() == TimePeg.VALUE_UNKNOWN) return TimePeg.VALUE_UNKNOWN;
        if (link.getLocalValue() == TimePeg.VALUE_UNKNOWN) return super.getLocalValue();
        if (super.getLocalValue() >= link.getValue(bmlBlockPeg) + offset) return super.getLocalValue();
        return link.getLocalValue() + offset;
    }

    @Override
    public synchronized void setLocalValue(double v)
    {
        super.setLocalValue(v);
        if (v != TimePeg.VALUE_UNKNOWN)
        {
            if (link.getLocalValue() != TimePeg.VALUE_UNKNOWN)
            {
                if (v < link.getValue(bmlBlockPeg) + offset)
                {
                    link.setValue(v - offset,bmlBlockPeg);
                }
            }
        }
    }

    @Override
    public synchronized String toString()
    {
        return "AfterTimePeg with value " + getLocalValue() + " ,offset " + offset + " global value: " + getGlobalValue() + "link: "
                + link.toString();
    }
}
