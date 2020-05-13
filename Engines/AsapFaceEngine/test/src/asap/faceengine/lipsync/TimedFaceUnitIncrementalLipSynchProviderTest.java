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
package asap.faceengine.lipsync;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import hmi.faceanimation.FaceController;
import hmi.tts.Visime;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import saiba.bml.core.SpeechBehaviour;
import asap.faceengine.faceunit.TimedFaceUnit;
import asap.faceengine.viseme.MorphVisemeBinding;
import asap.faceengine.viseme.VisemeBinding;
import asap.faceengine.viseme.VisemeToMorphMapping;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.PlanManager;

/**
 * Unit tests for the TimedFaceUnitIncrementalLipSynchProvider
 * @author hvanwelbergen
 *
 */
public class TimedFaceUnitIncrementalLipSynchProviderTest
{
    private PlanManager<TimedFaceUnit> faceManager = new PlanManager<TimedFaceUnit>();
    private VisemeBinding visemeBinding;
    private FaceController mockFaceController = mock(FaceController.class);
    private SpeechBehaviour speechBehavior;
    private BMLBlockPeg bbPeg = BMLBlockPeg.GLOBALPEG;
    private static final double TIMING_PRECISION = 0.0001;
    private TimedFaceUnitIncrementalLipSynchProvider provider; 
    private final PegBoard pegBoard = new PegBoard();
    @Before
    public void before() throws IOException
    {
        String visemeBindingXML = "<VisemeToMorphMapping>" + "<Mapping viseme=\"0\" target=\"\"/>"
                + "<Mapping viseme=\"1\" target=\"morph1\"/>" + "<Mapping viseme=\"2\" target=\"morph2\"/>" + "</VisemeToMorphMapping>";
        VisemeToMorphMapping mapping = new VisemeToMorphMapping();
        mapping.readXML(visemeBindingXML);
        visemeBinding = new MorphVisemeBinding(mapping);
        String str = "<speech xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" " + "id=\"s1\"><text>Hello world</text></speech>";
        speechBehavior = new SpeechBehaviour("bml1", new XMLTokenizer(str));
        provider = new TimedFaceUnitIncrementalLipSynchProvider(visemeBinding, mockFaceController, faceManager, pegBoard);
    }
    
    @Test
    public void test()
    {
        provider.setLipSyncUnit(bbPeg, speechBehavior, 0, new Visime(1, 5000, false), new Object());
        List<TimedFaceUnit> faceUnits = faceManager.getPlanUnits();
        assertEquals(0, faceUnits.get(0).getStartTime(), TIMING_PRECISION);
        assertEquals(5, faceUnits.get(0).getEndTime(), TIMING_PRECISION);
        assertEquals("bml1",faceUnits.get(0).getBMLId());
        assertEquals("s1",faceUnits.get(0).getId());
    }
    
    @Test
    public void testReplace()
    {
        Object identifier = new Object();
        provider.setLipSyncUnit(bbPeg, speechBehavior, 0, new Visime(1, 5000, false), identifier);        
        provider.setLipSyncUnit(bbPeg, speechBehavior, 0, new Visime(1, 4000, false), identifier);
        List<TimedFaceUnit> faceUnits = faceManager.getPlanUnits();        
        assertEquals(1,faceUnits.size());
        assertEquals(0, faceUnits.get(0).getStartTime(), TIMING_PRECISION);
        assertEquals(4, faceUnits.get(0).getEndTime(), TIMING_PRECISION);
        assertEquals("bml1",faceUnits.get(0).getBMLId());
        assertEquals("s1",faceUnits.get(0).getId());
    }
    
    @Test
    public void testCoArticulation()
    {
        provider.setLipSyncUnit(bbPeg, speechBehavior, 0, new Visime(1, 1000, false), new Object());  
        provider.setLipSyncUnit(bbPeg, speechBehavior, 1, new Visime(2, 500, false), new Object());
        provider.setLipSyncUnit(bbPeg, speechBehavior, 1.5, new Visime(2, 750, false), new Object());
        provider.setLipSyncUnit(bbPeg, speechBehavior, 2.25, new Visime(1, 250, false), new Object());
        
        List<TimedFaceUnit> faceUnits = faceManager.getPlanUnits();
        assertEquals(0, faceUnits.get(0).getStartTime(), TIMING_PRECISION);
        assertEquals(2.5, faceUnits.get(3).getEndTime(), TIMING_PRECISION);
        
        assertEquals(1.25, faceUnits.get(0).getEndTime(), TIMING_PRECISION);
        assertEquals(0.5, faceUnits.get(1).getStartTime(), TIMING_PRECISION);
        assertEquals(1.875, faceUnits.get(1).getEndTime(), TIMING_PRECISION);
        assertEquals(1.25, faceUnits.get(2).getStartTime(), TIMING_PRECISION);
        assertEquals(2.375, faceUnits.get(2).getEndTime(), TIMING_PRECISION);
        assertEquals(1.875, faceUnits.get(3).getStartTime(), TIMING_PRECISION); 
    }
}
