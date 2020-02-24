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
package asap.faceengine.lipsync;

import hmi.faceanimation.FaceController;
import hmi.tts.Visime;

import java.util.HashMap;
import java.util.Map;

import saiba.bml.core.Behaviour;
import asap.faceengine.faceunit.TimedFaceUnit;
import asap.faceengine.viseme.VisemeBinding;
import asap.realizer.lipsync.IncrementalLipSynchProvider;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnitState;

/**
 * Provides incremental lipsync using TimedFaceUnits.
 * @author hvanwelbergen
 * 
 */
public class TimedFaceUnitIncrementalLipSynchProvider implements IncrementalLipSynchProvider
{
    private final VisemeBinding visimeBinding;
    private final FaceController faceController;
    private final PlanManager<TimedFaceUnit> facePlanManager;

    private Map<Object, TimedFaceUnit> tfuMap = new HashMap<>();
    private Map<TimedFaceUnit, Visime> tfuToVisimeMap = new HashMap<>();
    private final PegBoard pegBoard;

    public TimedFaceUnitIncrementalLipSynchProvider(VisemeBinding visBinding, FaceController fc,
            PlanManager<TimedFaceUnit> facePlanManager, PegBoard pb)
    {
        visimeBinding = visBinding;
        faceController = fc;
        this.facePlanManager = facePlanManager;
        pegBoard = pb;
    }

    private TimedFaceUnit getPrevious(double start, TimedFaceUnit tfuCur)
    {
        TimedFaceUnit previous = null;
        for (TimedFaceUnit tfu : tfuToVisimeMap.keySet())
        {
            if (tfu.getStartTime() != TimePeg.VALUE_UNKNOWN && tfu.getStartTime() < start && tfu != tfuCur)
            {
                if (previous == null || tfu.getStartTime() > previous.getStartTime())
                {
                    previous = tfu;
                }
            }
        }
        return previous;
    }

    @Override
    public synchronized void setLipSyncUnit(BMLBlockPeg bbPeg, Behaviour beh, double start, Visime vis, Object identifier)
    {
        TimedFaceUnit tfu = tfuMap.get(identifier);
        if (tfu == null)
        {
            tfu = visimeBinding.getVisemeUnit(bbPeg, beh, vis.getNumber(), faceController, pegBoard);
            tfu.setTimePeg("start", new TimePeg(bbPeg));
            tfu.setTimePeg("end", new TimePeg(bbPeg));
            tfu.setSubUnit(true);
            facePlanManager.addPlanUnit(tfu);
            tfuMap.put(identifier, tfu);
        }
        TimedFaceUnit tfuPrevious = getPrevious(start, tfu);

        if (tfuPrevious != null)
        {
            Visime prevVis = tfuToVisimeMap.get(tfuPrevious);
            double prevDuration = (double) prevVis.getDuration() / 1000d;
            tfu.getTimePeg("start").setGlobalValue(start - prevDuration * 0.5);
            tfuPrevious.getTimePeg("end").setGlobalValue(start + (double) vis.getDuration() / 1000d * 0.5);
        }
        else
        {
            tfu.getTimePeg("start").setGlobalValue(start);
        }
        tfuToVisimeMap.put(tfu, vis);
        tfu.getTimePeg("end").setGlobalValue(start + (double) vis.getDuration() / 1000d);
        tfu.setState(TimedPlanUnitState.LURKING);
    }
}
