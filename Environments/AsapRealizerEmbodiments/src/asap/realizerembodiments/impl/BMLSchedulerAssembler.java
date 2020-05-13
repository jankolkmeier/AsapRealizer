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
package asap.realizerembodiments.impl;

import hmi.util.Clock;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import saiba.bml.parser.BMLParser;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.scheduler.BMLASchedulingHandler;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizer.scheduler.SchedulingHandler;
import asap.realizer.scheduler.SortedSmartBodySchedulingStrategy;

/**
 * Constructs a BMLScheduler from an xml description.
 * @author hvanwelbergen
 * 
 */
public class BMLSchedulerAssembler extends XMLStructureAdapter
{
    private SchedulingHandler schedulingHandler;
    private final BMLParser parser;
    private final FeedbackManager feedbackManager;
    private final String characterId;
    private final Clock schedulingClock;
    private final BMLBlockManager bmlBlockManager;
    private BMLScheduler bmlScheduler;
    private final PegBoard pegBoard;

    public BMLScheduler getBMLScheduler()
    {
        return bmlScheduler;
    }

    public BMLSchedulerAssembler(String characterId, BMLParser parser, FeedbackManager feedbackManager, BMLBlockManager bmlBlockManager,
            Clock schedulingClock, PegBoard pb)
    {
        this.parser = parser;
        this.characterId = characterId;
        this.feedbackManager = feedbackManager;
        this.schedulingClock = schedulingClock;
        this.bmlBlockManager = bmlBlockManager;
        this.pegBoard = pb;
        schedulingHandler = new BMLASchedulingHandler(new SortedSmartBodySchedulingStrategy(pb), pb);

    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (!tokenizer.atETag())
        {
            if (tokenizer.atSTag("SchedulingHandler"))
            {
                SchedulingHandlerAssembler asm = new SchedulingHandlerAssembler(pegBoard);
                asm.readXML(tokenizer);
                schedulingHandler = asm.getBMLSchedulingHandler();
            }
        }
        bmlScheduler = new BMLScheduler(characterId, parser, feedbackManager, schedulingClock, schedulingHandler, bmlBlockManager, pegBoard);
    }

    private static final String XMLTAG = "BMLScheduler";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to see if a given String equals
     * the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time xml tag of an object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
