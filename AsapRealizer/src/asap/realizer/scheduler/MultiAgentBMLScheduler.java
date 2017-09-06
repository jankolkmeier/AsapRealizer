/*******************************************************************************
 *******************************************************************************/
package asap.realizer.scheduler;

import hmi.util.Clock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import saiba.bml.core.Behaviour;
import saiba.bml.core.BehaviourBlock;
import saiba.bml.feedback.BMLBlockPredictionFeedback;
import saiba.bml.feedback.BMLPredictionFeedback;
import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import saiba.bml.feedback.BMLWarningFeedback;
import saiba.bml.parser.BMLParser;
import saiba.bml.parser.InvalidSyncRefException;
import saiba.bml.parser.SyncPoint;
import asap.bml.ext.bmla.feedback.BMLABlockPredictionFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockProgressFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockStatus;
import asap.bml.ext.bmla.feedback.BMLAPredictionFeedback;
import asap.realizer.BehaviorNotFoundException;
import asap.realizer.Engine;
import asap.realizer.SyncPointNotFoundException;
import asap.realizer.TimePegAlreadySetException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizerport.BMLFeedbackListener;

import com.google.common.collect.ImmutableSet;
import com.google.common.primitives.Doubles;

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
    
    //private final Hashtable<String, Set<Engine>> characterEngines;

    public MultiAgentBMLScheduler(BMLParser s, FeedbackManager bfm, Clock c, SchedulingHandler sh, BMLBlockManager bbm,
            PegBoard pb)
    {
    	super("", s, bfm, c, sh, bbm, pb);
    	
        //characterEngines = new Hashtable<String, Set<Engine>>();
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
		
    	//if (!characterEngines.containsKey(vhId)) {
    	//	characterEngines.put(vhId, new HashSet<Engine>());
    	//}
		//characterEngines.get(vhId).add(e);
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
    public Engine getEngine(Class<? extends Behaviour> c, String characterId) // throws NoEngineForBehaviourException
    {
    	if (!characterPlanSelector.containsKey(characterId) || 
    		!characterPlanSelector.get(characterId).containsKey(c)) {
            // throw new NoEngineForBehaviourException(c);
    		return null;
    	}
    	
    	return characterPlanSelector.get(characterId).get(c);
    }
    
    @Override
    public Engine getEngine(Class<? extends Behaviour> c) // throws NoEngineForBehaviourException
    {
    	if (characterPlanSelector.keySet().size() < 1) {
            // throw new NoEngineForBehaviourException(c);
    		return null;
    	}
    	
    	return getEngine(c, characterPlanSelector.keySet().iterator().next());
    }

    @Override
    public double getRigidity(Behaviour beh, String vhId) // throws NoEngineForBehaviourException
    {
    	Engine e = null;
    	if (!characterPlanSelector.containsKey(vhId)) return 0;
        e = characterPlanSelector.get(vhId).get(beh.getClass());
        
        if (e == null) {
            // throw new NoEngineForBehaviourException(beh);
            return 0;
        }
        
        return e.getRigidity(beh);
    }
    
    

    @Override
    public void addBMLBlock(BMLBBlock bbm)
    {
        for (Engine e : getEngines())
        {
        	if (e.getCharacterId().equals(bbm.getCharacterId())) {
        		e.setBMLBlockState(bbm.getBMLId(), TimedPlanUnitState.PENDING);
        	}
        }
    }
    
    
    
}
