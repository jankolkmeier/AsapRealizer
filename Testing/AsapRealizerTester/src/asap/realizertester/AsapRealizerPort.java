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
package asap.realizertester;

import saiba.realizertestport.XMLRealizerTestPort;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * Maps RealizerPort feedback to RealizerTestPort feedback
 * @author welberge
 */
public class AsapRealizerPort extends XMLRealizerTestPort implements RealizerPort
{
    private final RealizerPort realizerPort;

    public AsapRealizerPort(RealizerPort port)
    {
        realizerPort = port;
        realizerPort.addListeners(new MyListener());
    }

    @Override
    public void performBML(String bmlString)
    {
        realizerPort.performBML(bmlString);
    }

    @Override
    public void addListeners(asap.realizerport.BMLFeedbackListener... listeners)
    {
        realizerPort.addListeners(listeners);
    }

    @Override
    public void removeAllListeners()
    {
        realizerPort.removeAllListeners();
    }
    
    @Override
    public void removeListener(BMLFeedbackListener l)
    {
        realizerPort.removeListener(l);
    }
    
    private class MyListener implements BMLFeedbackListener
    {
        @Override
        public void feedback(String feedback)
        {
            AsapRealizerPort.this.feedback(feedback);
        }        
    }

    
}
