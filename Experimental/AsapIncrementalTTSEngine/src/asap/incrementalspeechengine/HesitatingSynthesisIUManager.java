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

import inpro.audio.DispatchStream;
import inpro.incremental.unit.IU;
import inpro.incremental.unit.WordIU;
import lombok.Getter;
import done.inpro.system.carchase.HesitatingSynthesisIU;

/**
 * Manages the construction and concatenation of HesitatingSynthesisIUs
 * @author hvanwelbergen
 * 
 */
public class HesitatingSynthesisIUManager
{
    private final DispatchStream dispatcher;
    private HesitatingSynthesisIU currentIU = null;
    private IncrementalTTSUnit currentTTSUnit;
    @Getter
    private final String voiceName;
    
    private static final double MERGE_TIME = 0.001d;

    public HesitatingSynthesisIUManager(DispatchStream dispatcher, String voiceName)
    {
        this.dispatcher = dispatcher;
        this.voiceName = voiceName;
    }

    private int getRemainingPhonemes(WordIU word)
    {
        int i = 0;
        for (IU phonemeIU : word.groundedIn())
        {
            if (phonemeIU.isCompleted())
            {
                i++;
            }
        }
        return word.groundedIn().size() - i;
    }

    /**
     * Appends synthesisIU to the currentIU if currentIU is ongoing, but finishes or relaxes within two phonemes AND
     * synthesisIU is supposed to start at either the relax time or the end time of the currentIU.
     */
    public boolean justInTimeAppendIU(HesitatingSynthesisIU synthesisIU, IncrementalTTSUnit ttsCandidate)
    {
        if (currentIU == null || currentIU.isCompleted())
        {
            return false;
        }
        double timeDiffRelax = Math.abs(ttsCandidate.getStartTime() - currentTTSUnit.getRelaxTime());
        double timeDiffEnd = Math.abs(ttsCandidate.getStartTime() - currentTTSUnit.getEndTime());
        
        WordIU lastWord = currentIU.getWords().get(currentIU.getWords().size() - 1);
        
        boolean merge = false;
        if (lastWord.toPayLoad().equals("<hes>")&& timeDiffRelax < MERGE_TIME)
        {
            lastWord = currentIU.getWords().get(currentIU.getWords().size() - 2);
            merge = true;
        }
        else if(timeDiffEnd < MERGE_TIME && !lastWord.toPayLoad().equals("<hes>"))
        {
            merge = true;
        }

        if (merge)
        {
            if (getRemainingPhonemes(lastWord) <= 2)
            {
                currentIU.appendContinuation(synthesisIU.getWords());
                currentTTSUnit = ttsCandidate;
                return true;
            }
        }
        return false;
    }

    public void playIU(HesitatingSynthesisIU synthesisIU, IncrementalTTSUnit ttsUnit)
    {
        if (currentTTSUnit == ttsUnit) return;// already added with appendIU

        
        currentTTSUnit = ttsUnit;
        if (currentIU == null || currentIU.isCompleted())
        {
            dispatcher.playStream(synthesisIU.getAudio(), true);
            currentIU = synthesisIU;
        }
        else
        {
            currentIU.appendContinuation(synthesisIU.getWords());
        }
    }
}
