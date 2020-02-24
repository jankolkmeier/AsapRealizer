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
package asap.animationengine.ace.lmp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import saiba.bml.core.Behaviour;
import asap.animationengine.motionunit.TimedAnimationUnit;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.AfterPeg;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.PegKey;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedAbstractPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.scheduler.TimePegAndConstraint;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Local motor program skeleton implementation
 * @author hvanwelbergen
 * 
 */
public abstract class LMP extends TimedAbstractPlanUnit implements TimedAnimationUnit
{
    protected final PegBoard pegBoard;

    @Getter
    @Setter
    private volatile boolean startMoveable;
    private volatile boolean firstResolve = true;

    @Getter
    private volatile TimePeg startPeg;
    @Getter
    private volatile TimePeg strokeStartPeg;
    @Getter
    private volatile TimePeg strokeEndPeg;

    public LMP(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId, PegBoard localPegBoard)
    {
        super(fbm, bmlPeg, bmlId, behId, true);
        this.pegBoard = localPegBoard;
        startPeg = createPegWhenMissingOnPegBoard("start");
        strokeStartPeg = createPegWhenMissingOnPegBoard("strokeStart");
        strokeEndPeg = createPegWhenMissingOnPegBoard("strokeEnd");
    }    

    @Override
    public List<String> getAvailableSyncs()
    {
        List<String> syncs = new ArrayList<>();
        ImmutableMap<PegKey, TimePeg> pegs = pegBoard.getTimePegs();
        for (PegKey pk : pegs.keySet())
        {
            if (pk.getBmlId().equals(getBMLId()) && pk.getId().equals(getId()))
            {
                syncs.add(pk.getSyncId());
            }
        }
        return syncs;
    }

    // for now just moves the start to attain desired preparation timing.
    protected void updateStartTime()
    {
        double prepDuration = getPreparationDuration();

        ImmutableSet<PegKey> keys = pegBoard.getPegKeys(startPeg);
        if (keys.size() - countInternalSyncs(keys, 0) == 0)
        {
            double localStart = pegBoard.getRelativePegTime(getBMLId(), strokeStartPeg) - prepDuration;
            if (localStart < 0) localStart = 0;
            startPeg.setLocalValue(localStart);
        }
        else if (startPeg instanceof AfterPeg)
        {
            double intendedStart = strokeStartPeg.getGlobalValue() - prepDuration;
            if (intendedStart < startPeg.getLink().getGlobalValue())
            {
                intendedStart = startPeg.getLink().getGlobalValue();
            }
            double localStart = pegBoard.getRelativePegTime(getBMLId(), strokeStartPeg) - prepDuration;
            if (localStart < 0) localStart = 0;
            startPeg.setLocalValue(localStart);
        }
    }

    @Override
    public void updateTiming(double time) throws TimedPlanUnitPlayException
    {
        if (isLurking())
        {
            updateStartTime();
        }
        resolveTimePegs(time);
    }

    protected TimePeg createPegWhenMissingOnPegBoard(String syncId)
    {
        TimePeg tp = pegBoard.getTimePeg(getBMLId(), getId(), syncId);
        if (tp == null)
        {
            tp = new TimePeg(getBMLBlockPeg());
            pegBoard.addTimePeg(getBMLId(), getId(), syncId, tp);
        }
        return tp;
    }

    @Override
    public double getStartTime()
    {
        return startPeg.getGlobalValue();
    }

    public double getStrokeStartTime()
    {
        return strokeStartPeg.getGlobalValue();
    }
    
    public double getStrokeEndTime()
    {
        return strokeEndPeg.getGlobalValue();
    }
    
    @Override
    public double getEndTime()
    {
        return pegBoard.getPegTime(getBMLId(), getId(), "end");
    }

    @Override
    public double getRelaxTime()
    {
        if (pegBoard.getPegTime(getBMLId(), getId(), "relax") != TimePeg.VALUE_UNKNOWN)
        {
            return pegBoard.getPegTime(getBMLId(), getId(), "relax");
        }
        return strokeEndPeg.getGlobalValue();
    }

    @Override
    public TimePeg getTimePeg(String syncId)
    {
        return pegBoard.getTimePeg(getBMLId(), getId(), syncId);
    }

    @Override
    public void setTimePeg(String syncId, TimePeg peg)
    {
        pegBoard.addTimePeg(getBMLId(), getId(), syncId, peg);
        if (syncId.equals("start")) startPeg = peg;
        else if(syncId.equals("strokeStart")) strokeStartPeg = peg;
        else if(syncId.equals("strokeEnd")) strokeEndPeg = peg;
    }

    protected boolean noPegsSet()
    {
        for (TimePeg tp : pegBoard.getTimePegs(getBMLId(), getId()))
        {
            if (tp.getGlobalValue() != TimePeg.VALUE_UNKNOWN)
            {
                return false;
            }
        }
        return true;
    }

    protected void setTpMinimumTime(double time)
    {

        for (TimePeg tp : pegBoard.getTimePegs(getBMLId(), getId()))
        {
            if (tp.getGlobalValue() != TimePeg.VALUE_UNKNOWN && !tp.isAbsoluteTime())
            {
                if (tp.getGlobalValue() + 0.05 < time)
                {
                    tp.setGlobalValue(time);
                }
            }
        }
    }

    @Override
    public void resolveSynchs(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac) throws BehaviourPlanningException
    {

    }

    protected abstract void setInternalStrokeTiming(double time);

    public double getStrokeDuration(double time)
    {
        if (time >= strokeStartPeg.getGlobalValue())
        {
            return strokeEndPeg.getGlobalValue() - strokeStartPeg.getGlobalValue();
        }
        return getStrokeDuration();
    }

    protected int countInternalSyncs(Set<PegKey> pks, int currentCount)
    {
        for (PegKey pk : pks)
        {
            if (pk.getBmlId().equals(getBMLId()) && pk.getId().equals(getId()) && getAvailableSyncs().contains(pk.getSyncId()))
            {
                currentCount++;
            }
        }
        return currentCount;
    }

    protected void resolveTimePegs(double time)
    {

        Set<PegKey> pkStart = pegBoard.getPegKeys(startPeg);

        if (firstResolve)
        {
            setStartMoveable(pkStart.size() - countInternalSyncs(pkStart, 0) == 0);
            firstResolve = false;
        }

        if (isLurking() || isInPrep())
        {
            if (!startPeg.isAbsoluteTime() && isStartMoveable())
            {
                startPeg.setGlobalValue(strokeStartPeg.getGlobalValue() - getPreparationDuration());                
            }
        }

        if (time < getRelaxTime())
        {
            setInternalStrokeTiming(time);
        }

        if (!isPlaying() && !isDone() && !isPending() && !isInPrep())
        {
            setTpMinimumTime(time);
        }
    }

}
