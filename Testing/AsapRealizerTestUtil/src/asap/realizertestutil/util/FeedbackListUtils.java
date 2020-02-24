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
package asap.realizertestutil.util;

import java.util.ArrayList;
import java.util.List;

import saiba.bml.feedback.BMLSyncPointProgressFeedback;
/**
 * Test utilities acting on a list of BMLSyncPointProgressFeedback
 * @author welberge
 */
public final class FeedbackListUtils
{
    private FeedbackListUtils(){}
    
    public static List<String> getSyncs(List<BMLSyncPointProgressFeedback> fbList)
    {
        List<String> syncs = new ArrayList<String>();
        for(BMLSyncPointProgressFeedback fb:fbList)
        {
            syncs.add(fb.getSyncId());
        }
        return syncs;
    }
    
    public static List<String> getIds(List<BMLSyncPointProgressFeedback> fbList)
    {
        List<String> ids = new ArrayList<String>();
        for(BMLSyncPointProgressFeedback fb:fbList)
        {
            ids.add(fb.getCharacterId());
        }
        return ids;
    }
    
    public static List<String> getBmlIds(List<BMLSyncPointProgressFeedback> fbList)
    {
        List<String> bmlIds = new ArrayList<String>();
        for(BMLSyncPointProgressFeedback fb:fbList)
        {
            bmlIds.add(fb.getBMLId());
        }
        return bmlIds;
    }
}
