/*******************************************************************************
 *******************************************************************************/
package asap.animationengine.gaze;

import asap.animationengine.motionunit.AnimationUnit;
import asap.motionunit.MUPlayException;

/**
 * Gaze motion unit
 * @author hvanwelbergen
 */
public interface GazeMU extends AnimationUnit
{
    /**
     * On target duration
     */
    double getPreferedStayDuration();
    
    double getPreferedRelaxDuration();
    
    double getPreferedReadyDuration();
    
    void setDurations(double prepDur, double relaxDur);
    
    void setEndRotation(float[] gazeDir);
    
    void setStartPose() throws MUPlayException;
    
    void setTarget();
    
    GazeInfluence getInfluence();
}
