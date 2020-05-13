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
import hmi.tts.TTSTiming;
import hmi.tts.Visime;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;
import saiba.bml.core.Behaviour;
import asap.faceengine.faceunit.TimedFaceUnit;
import asap.faceengine.viseme.VisemeBinding;
import asap.realizer.lipsync.LipSynchProvider;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnit;

/**
 * Creates TimedFaceUnits for lipsync 
 * @author Herwin
 *
 */
@Slf4j
public class TimedFaceUnitLipSynchProvider implements LipSynchProvider
{
    private final VisemeBinding visimeBinding;
    private final FaceController faceController;
    private final PlanManager<TimedFaceUnit>facePlanManager;
    private final PegBoard pegBoard;
    
    public TimedFaceUnitLipSynchProvider(VisemeBinding visBinding, FaceController fc, PlanManager<TimedFaceUnit>facePlanManager, PegBoard pb)
    {
        visimeBinding = visBinding;
        faceController = fc;
        pegBoard = pb;
        this.facePlanManager= facePlanManager; 
    }
    
    @Override
    public void addLipSyncMovement(BMLBlockPeg bbPeg, Behaviour beh, TimedPlanUnit bs, TTSTiming timing)
    {
        ArrayList<TimedFaceUnit> tfus = new ArrayList<TimedFaceUnit>();
        double totalDuration = 0d;
        double prevDuration = 0d;

        // add null viseme before
        TimedFaceUnit tfu = null;
        HashMap<TimedFaceUnit, Double> startTimes = new HashMap<TimedFaceUnit, Double>();
        HashMap<TimedFaceUnit, Double> endTimes = new HashMap<TimedFaceUnit, Double>();
        
        for (Visime vis : timing.getVisimes())
        {
            // OOK: de visemen zijn nu te kort (sluiten aan op interpolatie 0/0
            // ipv 50/50)
            // make visemeunit, add to faceplanner...
            double start = totalDuration / 1000d - prevDuration / 2000;
            double peak = totalDuration / 1000d + vis.getDuration() / 2000d;
            double end = totalDuration / 1000d + vis.getDuration() / 1000d;
            if(tfu!=null)
            {
                endTimes.put(tfu, peak); // extend previous tfu to the peak of this
            }
            // one!
            tfu = visimeBinding.getVisemeUnit(bbPeg, beh, vis.getNumber(), faceController, pegBoard);

            startTimes.put(tfu, start);
            endTimes.put(tfu, end);
            tfus.add(tfu);
            totalDuration += vis.getDuration();
            prevDuration = vis.getDuration();
        }
        

        for (TimedFaceUnit vfu : tfus)
        {
            vfu.setSubUnit(true);
            facePlanManager.addPlanUnit(vfu);
        }

        // and now link viseme units to the speech timepeg!
        for (TimedFaceUnit plannedFU : tfus)
        {
            TimePeg startPeg = new OffsetPeg(bs.getTimePeg("start"), startTimes.get(plannedFU));

            plannedFU.setTimePeg("start", startPeg);
            TimePeg endPeg = new OffsetPeg(bs.getTimePeg("start"), endTimes.get(plannedFU));
            plannedFU.setTimePeg("end", endPeg);
            log.debug("adding face movement at {}-{}", plannedFU.getStartTime(), plannedFU.getEndTime());
        }        
    }

    
}
