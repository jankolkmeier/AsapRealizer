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
package asap.speechengine;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import hmi.tts.Bookmark;
import hmi.tts.Phoneme;
import hmi.tts.TTSException;
import hmi.tts.TimingInfo;
import hmi.tts.Visime;
import hmi.tts.WordDescription;

import java.util.ArrayList;
import java.util.List;

import saiba.bml.core.SpeechBehaviour;
import asap.realizer.pegboard.BMLBlockPeg;

/**
 * Unit test cases for a DirectTTSPlanner
 * @author welberge
 *
 */
public class DirectTTSPlannerTest extends TTSPlannerTest
{
    
    
    @Override
    protected void mockTTSUnitFactoryExpectations() throws TTSException
    {
        final TimedDirectTTSUnit ttsUnit = new TimedDirectTTSUnit(mockFeedbackManager,bbPeg,
                SPEECHTEXT, BMLID, SPEECHID, mockTTSBinding,
                SpeechBehaviour.class);
        Phoneme p = new Phoneme(0,(int)(SPEECH_DURATION*1000),false);
        List<Phoneme> ps = new ArrayList<Phoneme>();
        ps.add(p);
        WordDescription wd2 = new WordDescription("world", ps, new ArrayList<Visime>());
        final List<Bookmark> bms = new ArrayList<Bookmark>();
        final Bookmark bm = new Bookmark("s1", wd2, 500);
        bms.add(bm);
        final List<WordDescription> wds = new ArrayList<WordDescription>();
        wds.add(wd2);
        TimingInfo tInfo = new TimingInfo(wds,bms,new ArrayList<Visime>());
        
        when(mockTTSUnitFactory.createTimedTTSUnit(
                (BMLBlockPeg)any(), 
                anyString(),
                anyString(),
                eq(BMLID), 
                eq(SPEECHID), 
                eq(mockTTSBinding), 
                eq(SpeechBehaviour.class))).thenReturn(ttsUnit);  
        when(mockTTSBinding.getTiming(SpeechBehaviour.class, SPEECHTEXT)).thenReturn(tInfo);        
    }
}
