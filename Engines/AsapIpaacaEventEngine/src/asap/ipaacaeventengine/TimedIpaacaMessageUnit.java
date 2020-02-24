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
package asap.ipaacaeventengine;

import ipaaca.LocalMessageIU;

import java.util.Map;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedEventUnit;
import asap.realizer.planunit.TimedPlanUnitPlayException;

/**
 * Sends an ipaaca event
 * @author hvanwelbergen
 *
 */
public class TimedIpaacaMessageUnit extends TimedEventUnit
{
    private final MessageManager messageManager; 
    private final LocalMessageIU message;
    
    public TimedIpaacaMessageUnit(FeedbackManager bfm, BMLBlockPeg bmlPeg, String bmlId, String behId, MessageManager messageManager, String category, Map<String,String> payload)
    {
        super(bfm,bmlPeg, bmlId, behId);     
        this.messageManager = messageManager;
        message = new LocalMessageIU(category);
        message.setPayload(payload);
    }

    @Override
    protected void startUnit(double time) throws TimedPlanUnitPlayException
    {
        feedback("start",time);
        messageManager.sendMessage(message);
    }
}
