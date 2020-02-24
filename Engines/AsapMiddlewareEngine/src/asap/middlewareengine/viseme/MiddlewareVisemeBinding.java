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
package asap.middlewareengine.viseme;

import asap.middlewareengine.embodiment.MiddlewareEmbodiment;
import asap.middlewareengine.planunit.SendJsonDataMessageMU;
import asap.middlewareengine.planunit.TimedMiddlewareUnit;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.feedback.NullFeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.ParameterException;
import lombok.extern.slf4j.Slf4j;
import saiba.bml.core.Behaviour;

/**
 * The MiddlewareVisemeBinding maps from visemes to TimedMiddlewareUnits. 
 * 
 * @author Dennis Reidsma
 */
@Slf4j
public class MiddlewareVisemeBinding
{
    VisemeToJsonMapping mapping;
    
    public MiddlewareVisemeBinding(VisemeToJsonMapping mapping)
    {
        this.mapping = mapping;
    }
    
    /**
     * Get a viseme unit for viseme viseme. If the viseme is not found, an 'empty' TimedMiddlewareUnit is returned.
     */
    public TimedMiddlewareUnit getVisemeUnit(FeedbackManager bfm,BMLBlockPeg bbPeg, Behaviour b, int viseme,
    		MiddlewareEmbodiment mwe, PegBoard pb)
    { 
        SendJsonDataMessageMU visemeMWU = new SendJsonDataMessageMU();
        if (viseme == -1) viseme = 0;
        String json = mapping.getJsonForViseme(viseme);
        
        if (json != null)
        {
        	try
        	{
        		visemeMWU.setParameterValue("content", json);
                //log.debug("viseme: {} pose: {}",viseme, pose);
        	}
        	catch (ParameterException ex)
        	{
        		log.warn("could not load json content {} for viseme {}",json, viseme);
        	}
        }
        else
        {
            log.warn("no json available for viseme {}",viseme);
        }

        TimedMiddlewareUnit tmwu = visemeMWU.copy(mwe).createTMU(bfm, bbPeg, b.getBmlId(), b.id);// from other examples, there was also one more parameter, pb);
        //TODO: DENNIS at this point, we can also set some syncpoints

        return tmwu;
    }
    
    /**
     * Get a viseme unit that is not hooked up to the feedbackmanager
     * If the viseme is not found, an 'empty' TimedMiddlewareUnit is returned.
     */
    public TimedMiddlewareUnit getVisemeUnit(BMLBlockPeg bbPeg, Behaviour b, int viseme,            
    		MiddlewareEmbodiment mwe, PegBoard pb)
    {
        return getVisemeUnit(NullFeedbackManager.getInstance(), bbPeg, b, viseme, mwe, pb);
    }
            
}
