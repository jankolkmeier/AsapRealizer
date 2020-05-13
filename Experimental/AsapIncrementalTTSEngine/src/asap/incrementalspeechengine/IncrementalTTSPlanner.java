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
package asap.incrementalspeechengine;

import hmi.tts.util.PhonemeToVisemeMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import saiba.bml.BMLInfo;
import saiba.bml.core.Behaviour;
import saiba.bml.core.SpeechBehaviour;
import asap.realizer.AbstractPlanner;
import asap.realizer.BehaviourPlanningException;
import asap.realizer.SyncAndTimePeg;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.lipsync.IncrementalLipSynchProvider;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.PlanManager;
import asap.realizer.scheduler.LinearStretchResolver;
import asap.realizer.scheduler.TimePegAndConstraint;
import asap.realizer.scheduler.UniModalResolver;

import com.google.common.collect.ImmutableList;

/**
 * Planner for the IncrementalTTSEngine
 * @author hvanwelbergen
 * 
 */
public class IncrementalTTSPlanner extends AbstractPlanner<IncrementalTTSUnit>
{
    private UniModalResolver resolver = new LinearStretchResolver();
    private final PhonemeToVisemeMapping visemeMapping;
    private final Collection<IncrementalLipSynchProvider> lipSynchers;
    private final PhraseIUManager iuManager;
    
    public IncrementalTTSPlanner(FeedbackManager fbm, PlanManager<IncrementalTTSUnit> planManager, PhraseIUManager iuManager,
            PhonemeToVisemeMapping vm, Collection<IncrementalLipSynchProvider> ls)
    {
        super(fbm, planManager);
         
        this.iuManager = iuManager;
        this.visemeMapping = vm;
        this.lipSynchers = ImmutableList.copyOf(ls);
        
        BMLInfo.addCustomStringAttribute(SpeechBehaviour.class, "http://www.asap-project.org/bmlis","generatefiller");
    }

    private IncrementalTTSUnit createTTSUnit(BMLBlockPeg bbPeg, Behaviour b)
    {
        SpeechBehaviour bSpeech = (SpeechBehaviour) b;
        IncrementalTTSUnit ttsUnit = new IncrementalTTSUnit(fbManager, bbPeg, b.getBmlId(), b.id, bSpeech.getContent(), iuManager,
                lipSynchers, visemeMapping, b);
        return ttsUnit;
    }

    @Override
    public List<SyncAndTimePeg> addBehaviour(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac, IncrementalTTSUnit bs)
            throws BehaviourPlanningException
    {
        if (bs == null)
        {
            bs = createTTSUnit(bbPeg, b);
        }

        ArrayList<SyncAndTimePeg> satp = new ArrayList<SyncAndTimePeg>();
        
        for (String sync:bs.getAvailableSyncs())
        {
            satp.add(new SyncAndTimePeg(b.getBmlId(), b.id, sync, bs.getTimePeg(sync)));            
        }
        planManager.addPlanUnit(bs);        
        bs.applyTimeConstraints();
        return satp;
    }

    @Override
    public IncrementalTTSUnit resolveSynchs(BMLBlockPeg bbPeg, Behaviour b, List<TimePegAndConstraint> sac)
            throws BehaviourPlanningException
    {
        IncrementalTTSUnit ttsUnit = createTTSUnit(bbPeg, b);
        resolver.resolveSynchs(bbPeg, b, sac, ttsUnit);
        ttsUnit.applyTimeConstraints();
        return ttsUnit;
    }

    @Override
    public List<Class<? extends Behaviour>> getSupportedBehaviours()
    {
        return new ImmutableList.Builder<Class<? extends Behaviour>>().add(SpeechBehaviour.class).build();
    }

    @Override
    public List<Class<? extends Behaviour>> getSupportedDescriptionExtensions()
    {
        return new ImmutableList.Builder<Class<? extends Behaviour>>().build();
    }

    @Override
    public double getRigidity(Behaviour beh)
    {
        return 0.8;
    }

}
