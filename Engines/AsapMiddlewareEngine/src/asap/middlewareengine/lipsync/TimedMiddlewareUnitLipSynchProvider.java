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
package asap.middlewareengine.lipsync;

import java.util.ArrayList;
import java.util.HashMap;

import asap.middlewareengine.embodiment.MiddlewareEmbodiment;
import asap.middlewareengine.planunit.TimedMiddlewareUnit;
import asap.middlewareengine.viseme.MiddlewareVisemeBinding;
import asap.realizer.lipsync.LipSynchProvider;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.TimedPlanUnit;
import hmi.tts.TTSTiming;
import hmi.tts.Visime;
import lombok.extern.slf4j.Slf4j;
import saiba.bml.core.Behaviour;

/**
 * Creates TimedMiddlewareUnits for lipsync 
 * @author Dennis Reidsma
 *
 */
@Slf4j
public class TimedMiddlewareUnitLipSynchProvider implements LipSynchProvider
{
    private final MiddlewareVisemeBinding visemeBinding;
    private final MiddlewareEmbodiment mwe;
    private final PlanManager<TimedMiddlewareUnit> mwPlanManager;
    private final PegBoard pegBoard;
    
    //TODO: make this configurable
    //this value controls how much a viseme is offset when sending.. set a negative value if viseme should be sent before its being spoken
    private static final double VISEME_OFFSET = -0.2d;
    
    public TimedMiddlewareUnitLipSynchProvider(MiddlewareVisemeBinding visBinding, MiddlewareEmbodiment mwe, PlanManager<TimedMiddlewareUnit>mwPlanManager, PegBoard pb)
    {
        visemeBinding = visBinding;
        this.mwe = mwe;
        pegBoard = pb;
        this.mwPlanManager= mwPlanManager; 
    }
    
    @Override
    public void addLipSyncMovement(BMLBlockPeg bbPeg, Behaviour beh, TimedPlanUnit bs, TTSTiming timing)
    {
        ArrayList<TimedMiddlewareUnit> tmwus = new ArrayList<TimedMiddlewareUnit>();
        double totalDuration = 0d;
        double prevDuration = 0d;

        // add null viseme before
        TimedMiddlewareUnit tmwu = null;
        HashMap<TimedMiddlewareUnit, Double> startTimes = new HashMap<TimedMiddlewareUnit, Double>();
        HashMap<TimedMiddlewareUnit, Double> endTimes = new HashMap<TimedMiddlewareUnit, Double>();
        
        for (Visime vis : timing.getVisimes())
        {
            // visemes lopen nu vanaf de peak van de vorige viseme tot aan de peak van deze viseme 
            double start = (totalDuration / 1000d - prevDuration / 2000);
            double peak = (totalDuration / 1000d + vis.getDuration() / 2000d);
            double end = (totalDuration / 1000d + vis.getDuration() / 1000d);

            tmwu = visemeBinding.getVisemeUnit(bbPeg, beh, vis.getNumber(), mwe, pegBoard);

            startTimes.put(tmwu, start);
            endTimes.put(tmwu, end);
            tmwus.add(tmwu);
            totalDuration += vis.getDuration();
            prevDuration = vis.getDuration();
        }
        

        for (TimedMiddlewareUnit vmwu : tmwus)
        {
            vmwu.setSubUnit(true);
            mwPlanManager.addPlanUnit(vmwu);
        }

        // and now link viseme units to the speech timepeg!
        for (TimedMiddlewareUnit plannedMWU : tmwus)
        {
            TimePeg startPeg = new OffsetPeg(bs.getTimePeg("start"), startTimes.get(plannedMWU) + VISEME_OFFSET);

            plannedMWU.setTimePeg("start", startPeg);
            TimePeg endPeg = new OffsetPeg(bs.getTimePeg("start"), endTimes.get(plannedMWU) + VISEME_OFFSET);
            plannedMWU.setTimePeg("end", endPeg);
            log.debug("adding mw movement at {}-{}", plannedMWU.getStartTime(), plannedMWU.getEndTime());
        }        
    }

    
}
