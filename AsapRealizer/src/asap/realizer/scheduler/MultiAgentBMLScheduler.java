/*******************************************************************************
 *******************************************************************************/
package asap.realizer.scheduler;

import hmi.util.Clock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.parser.BMLParser;
import asap.bml.ext.bmla.feedback.BMLABlockProgressFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockStatus;
import asap.realizer.Engine;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitState;

/**
 * MultiAgentBMLScheduler, handles BML block states, feedback listeners and maintains engines and
 * anticipators. A scheduling strategy needs to be provided to hand off the actual scheduling to.
 * Supports Multiple Agents
 * 
 * @author Jan Kolkmeier
 */
@Slf4j
public final class MultiAgentBMLScheduler extends BMLScheduler
{
	
    private final Map<String, Map<Class<? extends Behaviour>, Engine>> characterPlanSelector;

    public MultiAgentBMLScheduler(BMLParser s, FeedbackManager bfm, Clock c, SchedulingHandler sh, BMLBlockManager bbm,
            PegBoard pb)
    {
    	super("", s, bfm, c, sh, bbm, pb);
        characterPlanSelector = new HashMap<String, Map<Class<? extends Behaviour>, Engine>>();
    }

    /**
     * Schedules the behaviors provided to this scheduler (e.g. by grabbing them from the parser)
     * This implementation takes care that it 
     */
    public void schedule()
    {
        for (BehaviourBlock bb : parser.getBehaviourBlocks())
        {
        	String bbCharacterId = bb.getCharacterId();
        	if (bbCharacterId == null || !characterPlanSelector.containsKey(bbCharacterId)) {
                log.warn("Ignored BehaviorBlock "+bb.id+ " in MultiAgentScheduler. characterId in block was set to: "+bbCharacterId);
        	} else {
                bmlBlockMap.put(bb.id, bb);
                schedulingHandler.schedule(bb, this, getSchedulingTime());
        	}
        }
        parser.clear();
    }
    
    /**
     * Adds an engine that can plan Behaviour class c
     * 
     * @param c
     *            behaviour class the engine can plan
     * @param e
     *            the engine
     */
    public void addEngine(Class<? extends Behaviour> c, Engine e)
    {
    	String vhId = e.getCharacterId();
    	if (!characterPlanSelector.containsKey(vhId)) {
    		characterPlanSelector.put(vhId, new HashMap<Class<? extends Behaviour>, Engine>());
    	}
    	
		characterPlanSelector.get(vhId).put(c, e);
		engines.add(e);
    }
    
    /**
     * Get the engine that can plan Behaviour class c
     * 
     * @param c
     *            behaviour class the engine can plan
     *            
     * @param characterId
     * 			  character for which the behavior is intended
     */
    @Override
    public Engine getEngine(Class<? extends Behaviour> c, String characterId)
    {
    	if (!characterPlanSelector.containsKey(characterId) || 
    		!characterPlanSelector.get(characterId).containsKey(c)) {
    		return null;
    	}
    	
    	Engine res = characterPlanSelector.get(characterId).get(c);
    	return res;
    }
    
    @Override
    public Engine getEngine(Class<? extends Behaviour> c)
    {
    	if (characterPlanSelector.keySet().size() < 1) {
    		return null;
    	}
    	
    	return getEngine(c, characterPlanSelector.keySet().iterator().next());
    }

    @Override
    public double getRigidity(Behaviour beh, String vhId) 
    {
    	Engine e = null;
    	if (!characterPlanSelector.containsKey(vhId)) return 0;
        e = characterPlanSelector.get(vhId).get(beh.getClass());
        
        if (e == null) return 0;
        return e.getRigidity(beh);
    }
    
    

    @Override
    public void addBMLBlock(BMLBBlock bbm)
    {
    	String vhId = bbm.getCharacterId();
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
        	e.setBMLBlockState(bbm.getBMLId(), TimedPlanUnitState.PENDING);
        }
    }
    
    @Override
    public void blockStopFeedback(String bmlId, BMLABlockStatus status, double time)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        fbManager.blockProgress(new BMLABlockProgressFeedback(bmlId, "end", time, status), vhId);
    }

    @Override
    public void blockStartFeedback(String bmlId, double time)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        fbManager.blockProgress(new BMLABlockProgressFeedback(bmlId, "start", time, BMLABlockStatus.IN_EXEC), vhId);
    }
    
    @Override
    public double predictSubsidingTime(String bmlId)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        List<Double> subsidingTimes = new ArrayList<Double>();
        subsidingTimes.add(schedulingClock.getMediaSeconds());
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            subsidingTimes.add(e.getBlockSubsidingTime(bmlId));
        }
        return Collections.max(subsidingTimes);
    }

    @Override
    public void interruptBehavior(String bmlId, String behaviourId, double time)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            e.interruptBehaviour(bmlId, behaviourId, schedulingClock.getMediaSeconds());
        }
        bmlBlocksManager.updateBlocks(time);
    }

    @Override
    public void interruptBlock(String bmlId, double time)
    {
        if (!bmlBlocksManager.getBMLBlocks().contains(bmlId))
        {
            log.debug("Attempting to stop non existing bml block {}", bmlId);
            return;
        }

    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
    	
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            e.interruptBehaviourBlock(bmlId, schedulingClock.getMediaSeconds());
        }
        bmlBlocksManager.interruptBlock(bmlId, time);
        bmlBlocksManager.removeBMLBlock(bmlId, time);
    }

    @Override
    public void removeBehaviour(String bmlId, String behaviourId)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            e.stopBehaviour(bmlId, behaviourId, schedulingClock.getMediaSeconds());
        }
        pegBoard.removeBehaviour(bmlId, behaviourId);
    }

    @Override
    public void stopBehavior(String bmlId, String behaviourId, double time)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            e.stopBehaviour(bmlId, behaviourId, schedulingClock.getMediaSeconds());
        }
        bmlBlocksManager.updateBlocks(time);
    }
    
    @Override
    public void updateTiming(String bmlId)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            e.updateTiming(bmlId);
        }
    }

    @Override
    public double getEndTime(String bmlId, String behId)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            double endTime = e.getEndTime(bmlId, behId);
            if (endTime != TimePeg.VALUE_UNKNOWN)
            {
                return endTime;
            }
        }
        return TimePeg.VALUE_UNKNOWN;
    }

    @Override
    public Set<String> getBehaviours(String bmlId)
    {
    	String vhId = bmlBlocksManager.getCharacterId(bmlId);
        HashSet<String> behaviours = new HashSet<String>();
        for (Engine e : getEngines())
        {
        	if (!e.getCharacterId().equals(vhId)) continue;
            behaviours.addAll(e.getBehaviours(bmlId));
        }
        return behaviours;
    }
    
}
