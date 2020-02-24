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
package asap.animationengine.gaze;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.motionunit.MUPlayException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.InvalidParameterException;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;
import hmi.animation.VJoint;
import hmi.math.Quat4f;
import hmi.math.Vec3f;
import hmi.neurophysics.EyeSaturation;
import hmi.neurophysics.ListingsLaw;
import hmi.util.StringUtil;
import hmi.worldobjectenvironment.WorldObject;
import hmi.worldobjectenvironment.WorldObjectManager;
import lombok.Getter;
import lombok.Setter;
import saiba.bml.core.OffsetDirection;

public abstract class AbstractGazeMU implements GazeMU
{
    protected static final double TARGET_IMPORTANCE = 8;
    protected static final double NECK_VELOCITY = 2 * Math.PI;
    protected static final double RELATIVE_READY_TIME = 0.25;
    protected static final double RELATIVE_RELAX_TIME = 0.75;    
    
    @Getter
    @Setter
    protected GazeInfluence influence = GazeInfluence.NECK;
    
    protected boolean isLocal = false;
    protected VJoint rEye;
    protected VJoint lEye;
    protected VJoint rEyeCurr;
    protected VJoint lEyeCurr;
    protected float[] localGaze = new float[3];
    
    protected double offsetAngle = 0;
    protected OffsetDirection offsetDirection = OffsetDirection.NONE;
    
    protected AnimationPlayer player;
    protected float qGaze[] = Quat4f.getIdentity();
    protected double preparationDuration;
    protected String target = "";
    protected WorldObject woTarget;
    protected KeyPosition ready;
    protected KeyPosition relax;
    protected WorldObjectManager woManager;
    
    private KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();
    
    protected void setupKeyPositions()
    {
        ready = new KeyPosition("ready", RELATIVE_READY_TIME, 1);
        relax = new KeyPosition("relax", RELATIVE_RELAX_TIME, 1);
        addKeyPosition(ready);
        addKeyPosition(relax);
        addKeyPosition(new KeyPosition("start", 0, 1));
        addKeyPosition(new KeyPosition("end", 1, 1));
    }
    
    protected float[] getOffsetRotation()
    {
        return GazeUtils.getOffsetRotation(offsetDirection, offsetAngle);        
    }
    
    protected void setEndEyeRotation(VJoint eye, float qEye[]) throws MUPlayException
    {
        float[] q = getUnsaturizedEyeRotation(eye);
        EyeSaturation.sat(q, Quat4f.getIdentity(), qEye);
    }
    
    protected float[] getUnsaturizedEyeRotation(VJoint eye) throws MUPlayException
    {
        float gazeDir[] = Vec3f.getVec3f();
        if(isLocal)
        {
            Vec3f.set(gazeDir, localGaze);//just parallel for now
        }
        else
        {
            woTarget.getTranslation2(gazeDir, eye);                
        }
        Quat4f.transformVec3f(getOffsetRotation(), gazeDir);
        Vec3f.normalize(gazeDir);
        float q[] = Quat4f.getQuat4f();
        ListingsLaw.listingsEye(gazeDir, q);
        return q;
    }
    
    public void setTarget(String target) throws MUPlayException
    {
        if (woManager == null)
        {
            throw new MUPlayException("Gaze target not found, no WorldObjectManager set up.", this);
        }
        woTarget = woManager.getWorldObject(target);
        if (woTarget == null)
        {
            throw new MUPlayException("Gaze target not found", this);
        }
        setTarget();
    }

    
    
    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {
        if (name.equals("target"))
        {
            target = value;
        }
        else if (name.equals("offsetdirection"))
        {
            offsetDirection = OffsetDirection.valueOf(value);
        }
        else if (name.equals("influence"))
        {
            setInfluence(GazeInfluence.valueOf(value));
        }
        else if (StringUtil.isNumeric(value))
        {
            setFloatParameterValue(name, Float.parseFloat(value));
        }
        else throw new InvalidParameterException(name, value);
    }

    @Override
    public String getParameterValue(String name) throws ParameterNotFoundException
    {
        if (name.equals("target")) return target;
        if (name.equals("influence")) return influence.toString();
        if (name.equals("offsetdirection")) return "" + offsetDirection;
        return "" + getFloatParameterValue(name);
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterNotFoundException
    {
        if (name.equals("offsetangle")) return (float) offsetAngle;
        throw new ParameterNotFoundException(name);
    }

    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterNotFoundException
    {
        if (name.equals("offsetangle"))
        {
            offsetAngle = value;
        }
        else
        {
            throw new ParameterNotFoundException(name);
        }
    }
    
    @Override
    public void addKeyPosition(KeyPosition kp)
    {
        keyPositionManager.addKeyPosition(kp);
    }

    @Override
    public List<KeyPosition> getKeyPositions()
    {
        return keyPositionManager.getKeyPositions();
    }

    @Override
    public void setKeyPositions(List<KeyPosition> p)
    {
        keyPositionManager.setKeyPositions(p);
    }

    @Override
    public KeyPosition getKeyPosition(String name)
    {
        return keyPositionManager.getKeyPosition(name);
    }

    @Override
    public void removeKeyPosition(String id)
    {
        keyPositionManager.removeKeyPosition(id);
    }
    
    @Override
    public TimedAnimationMotionUnit createTMU(FeedbackManager bfm, BMLBlockPeg bmlBlockPeg, String bmlId, String id, PegBoard pb)
    {
        return new GazeTMU(bfm, bmlBlockPeg, bmlId, id, this, pb, player);
    }
    
    @Override
    public Set<String> getPhysicalJoints()
    {
        return ImmutableSet.of();
    }
    
    @Override
    public double getPreferedDuration()
    {
        return getPreferedReadyDuration() + getPreferedRelaxDuration() + getPreferedStayDuration();
    }
    
    protected void setGazeDirection(float[] direction)
    {
        isLocal = true;
        Vec3f.set(localGaze,direction);
    }
    
    @Override
    public double getPreferedRelaxDuration()
    {
        return getPreferedReadyDuration();
    }
    
    @Override
    public void startUnit(double time) throws MUPlayException
    {
        
    }
}
