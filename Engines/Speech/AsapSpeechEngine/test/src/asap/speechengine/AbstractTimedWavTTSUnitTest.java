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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertEquals;
import hmi.audioenvironment.LWJGLJoalSoundManager;
import hmi.audioenvironment.SoundManager;
import hmi.tts.Bookmark;

import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import saiba.bml.core.SpeechBehaviour;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;
import asap.realizer.planunit.TimedPlanUnitState;
import asap.realizerport.util.ListBMLFeedbackListener;
import asap.speechengine.ttsbinding.TTSBinding;


/**
 * Generic integration tests for all TimedWavTTSUnits.
 * @author welberge
 */
public abstract class AbstractTimedWavTTSUnitTest extends AbstractTTSUnitTest
{
    protected TTSBinding ttsBinding;
    private ListBMLFeedbackListener feedbackListener = new ListBMLFeedbackListener.Builder().feedBackList(fbList).build();
    protected static final SoundManager soundManager = new LWJGLJoalSoundManager();
    
    @BeforeClass
    public static void beforeClass()
    {
        soundManager.init();
    }
    
    @AfterClass
    public static void afterClass()
    {
        soundManager.shutdown();
    }
    
    @Before
    public void setup()
    {
        
    }

    @After
    public void tearDown()
    {
        if(ttsBinding!=null)
        {
            ttsBinding.cleanup();            
        }                
    }
    
    @Override
    protected TimedTTSUnit getTTSUnit(BMLBlockPeg bbPeg, String text, String id, String bmlId)
    {
        return new TimedWavTTSUnit(fbManager,soundManager, bbPeg, text, "voice1", bmlId, id, ttsBinding, SpeechBehaviour.class);
    } 
    
    protected TimedWavTTSUnit setupPlanUnit(String text, FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        TimedWavTTSUnit ttsUnit = new TimedWavTTSUnit(bfm, soundManager, bbPeg, text, "voice1", bmlId, id, ttsBinding, SpeechBehaviour.class);
        try
        {
            ttsUnit.setup();
        }
        catch (SpeechUnitPlanningException e)
        {
            throw new RuntimeException(e);
        }

        TimePeg start = new TimePeg(bbPeg);
        start.setGlobalValue(startTime);
        ttsUnit.setTimePeg("start", start);

        return ttsUnit;
    }

    @Override
    protected TimedPlanUnit setupPlanUnit(FeedbackManager bfm, BMLBlockPeg bbPeg, String id, String bmlId, double startTime)
    {
        return setupPlanUnit("Hello world", bfm, bbPeg, id, bmlId, startTime);
    } 

    @Test
    public void testBookmark()
    {
        TimedWavTTSUnit ttsUnit = setupPlanUnit("Hello <sync id=\"s1\"/> world", fbManager, BMLBlockPeg.GLOBALPEG, "id1", "bml1", 0);
        fbManager.addFeedbackListener(new ListBMLFeedbackListener.Builder().feedBackList(fbList).build());
        Bookmark bm = ttsUnit.getBookmarks().get(0);
        assertEquals("s1", bm.getName());
        assertEquals("world", bm.getWord().getWord().trim());
        assertThat(bm.getOffset(), greaterThan(0));
    }

    @Test
    public void testBookmarkFeedbackAtStop() throws TimedPlanUnitPlayException
    {
        TimedWavTTSUnit ttsUnit = setupPlanUnit("Blah blah blah hello <sync id=\"s1\"/> world", fbManager, BMLBlockPeg.GLOBALPEG, "id1",
                "bml1", 0);
        fbManager.addFeedbackListener(feedbackListener);
        ttsUnit.setState(TimedPlanUnitState.LURKING);
        Bookmark bm = ttsUnit.getBookmarks().get(0);
        ttsUnit.start(0);
        ttsUnit.stop(bm.getOffset() * 0.001d);
        assertThat(feedbackListener.getFeedbackSyncIds("bml1", "id1"),
                IsIterableContainingInOrder.contains("start", "s1","end"));
    }
}
