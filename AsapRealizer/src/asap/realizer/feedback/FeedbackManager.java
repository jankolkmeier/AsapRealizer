/*******************************************************************************
 *******************************************************************************/
package asap.realizer.feedback;

import java.util.List;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import saiba.bml.feedback.BMLWarningFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockProgressFeedback;
import asap.bml.ext.bmla.feedback.BMLAPredictionFeedback;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizerport.BMLFeedbackListener;

import com.google.common.collect.ImmutableSet;

/**
 * Interface for classes that manage and keep track of BML feedback
 * @author Herwin
 *
 */
public interface FeedbackManager
{
    void addFeedbackListener(BMLFeedbackListener fb);

    void feedback(BMLSyncPointProgressFeedback fb);
    
    void feedback(BMLSyncPointProgressFeedback fb, String vhId);
    
    /**
     * Send a list of feedback (in list-order) to the BMLFeedbackListeners. The listeners will
     * receive all feedbacks in the list before any subsequent feedback is sent using the feedback
     * functions.
     */
    void feedback(List<BMLSyncPointProgressFeedback> fbs);
    
    void feedback(List<BMLSyncPointProgressFeedback> fbs, String vhId);
    
    void removeAllFeedbackListeners();
    
    void removeFeedbackListener(BMLFeedbackListener fb);
    
    ImmutableSet<String> getSyncsPassed(String bmlId, String behaviorId);
    
    ImmutableSet<String> getSyncsPassed(String bmlId, String behaviorId, String vhId);

    void blockProgress(BMLABlockProgressFeedback psf);
    
    void blockProgress(BMLABlockProgressFeedback psf, String vhId);
    
    void puException(TimedPlanUnit timedMU, String message, double time);
    
    void puException(TimedPlanUnit timedMU, String message, double time, String vhId);
    
    void prediction(BMLAPredictionFeedback bpsf);    

    void warn(BMLWarningFeedback w, double time);
    
    void warn(BMLWarningFeedback w, double time, String vhId);
}
