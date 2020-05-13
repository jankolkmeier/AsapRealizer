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
package asap.realizertestutil.util;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.KeyPositionManager;

/**
 * Mocks up the getKeyPosition(id) and getKeyPositions functionality of a KeyPositionManager
 * @author welberge
 */
public final class KeyPositionMocker
{
    private KeyPositionMocker(){}
    public static void stubKeyPositions(KeyPositionManager muKpm, KeyPosition... keyPositions)
    {
        List<KeyPosition> kps = new ArrayList<KeyPosition>();
        for(KeyPosition keyPos: keyPositions)
        {
            when(muKpm.getKeyPosition(keyPos.id)).thenReturn(keyPos);
            kps.add(keyPos);
        }
        when(muKpm.getKeyPositions()).thenReturn(kps);
    }
}
