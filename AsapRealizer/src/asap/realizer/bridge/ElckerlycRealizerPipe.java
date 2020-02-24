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
package asap.realizer.bridge;

import asap.realizer.AsapRealizer;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/** Access to an Elckerlyc VH through the RealizerBridge interface. */
public class ElckerlycRealizerPipe implements RealizerPort
{
    private AsapRealizer realizer;

    public ElckerlycRealizerPipe(AsapRealizer realizer)
    {
        this.realizer = realizer;
    }

    @Override
    public void performBML(String bmlString)
    {
        realizer.scheduleBML(bmlString);
    }

    @Override
    public void removeAllListeners()
    {
        realizer.getScheduler().removeAllFeedbackListeners();
    }

    @Override
    public void removeListener(BMLFeedbackListener l)
    {
        realizer.getScheduler().removeFeedbackListener(l);
        
    }
    
    @Override
    public void addListeners(BMLFeedbackListener... listeners)
    {
        for (BMLFeedbackListener listener : listeners)
        {
            realizer.addFeedbackListener((BMLFeedbackListener) listener);
        }
    }
}
