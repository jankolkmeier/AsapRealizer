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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Default implementation of the KeyPositionManager
 * @author Herwin
 */
public class KeyPositionManagerImpl implements KeyPositionManager
{
    private List<KeyPosition> keys = Collections.synchronizedList(new ArrayList<KeyPosition>());

    @Override
    public void addKeyPosition(KeyPosition kp)
    {
        keys.add(kp);
        synchronized (keys)
        {
            Collections.sort(keys);
        }
    }

    @Override
    public List<KeyPosition> getKeyPositions()
    {
        synchronized (keys)
        {
            Collections.sort(keys);
        }
        return ImmutableList.copyOf(keys);
    }

    @Override
    public void setKeyPositions(List<KeyPosition> p)
    {
        keys = p;
        synchronized (keys)
        {
            Collections.sort(keys);
        }
    }

    @Override
    public KeyPosition getKeyPosition(String kid)
    {
        synchronized (keys)
        {
            for (KeyPosition kp : getKeyPositions())
            {
                if (kp.id.equals(kid)) return kp;
            }
        }
        return null;
    }

    @Override
    public void removeKeyPosition(String id)
    {
        KeyPosition removePos = null;

        synchronized (keys)
        {
            for (KeyPosition kp : getKeyPositions())
            {
                if (kp.id.equals(id))
                {
                    removePos = kp;
                    break;
                }
            }
        }
        if (removePos != null)
        {
            keys.remove(removePos);
        }
    }
}
