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
    private final Hashtable<String, Set<Engine>> characterEngineMap;

    public MultiAgentBMLScheduler(BMLParser s, FeedbackManager bfm, Clock c, SchedulingHandler sh, BMLBlockManager bbm,
            PegBoard pb)
    {
    	super("", s, bfm, c, sh, bbm, pb);
    	
        characterEngineMap = new Hashtable<String, Set<Engine>>();
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
        	if (bbCharacterId == null || characterEngineMap.containsKey(bbCharacterId)) {
                bmlBlockMap.put(bb.id, bb);
                schedulingHandler.schedule(bb, this, getSchedulingTime());
        	} else {
                log.warn("Ignored BehaviorBlock "+bb.id+ " in MultiAgentScheduler. characterId in block was set to: "+bb.getCharacterId());
        	}
        }
        parser.clear();
    }
}
