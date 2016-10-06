/*******************************************************************************
 *******************************************************************************/
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
            double start = totalDuration / 1000d - prevDuration / 2000;
            double peak = totalDuration / 1000d + vis.getDuration() / 2000d;
            double end = totalDuration / 1000d + vis.getDuration() / 1000d;

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
            TimePeg startPeg = new OffsetPeg(bs.getTimePeg("start"), startTimes.get(plannedMWU));

            plannedMWU.setTimePeg("start", startPeg);
            TimePeg endPeg = new OffsetPeg(bs.getTimePeg("start"), endTimes.get(plannedMWU));
            plannedMWU.setTimePeg("end", endPeg);
            log.debug("adding mw movement at {}-{}", plannedMWU.getStartTime(), plannedMWU.getEndTime());
        }        
    }

    
}
