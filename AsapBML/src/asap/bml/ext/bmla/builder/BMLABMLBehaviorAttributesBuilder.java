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
package asap.bml.ext.bmla.builder;

import asap.bml.ext.bmla.BMLABMLBehaviorAttributes;

/**
 * Builder for BMLABMLBehaviorAttributes
 * @author Herwin
 *
 */
public class BMLABMLBehaviorAttributesBuilder
{
    BMLABMLBehaviorAttributes behAttr = new BMLABMLBehaviorAttributes();
    
    public BMLABMLBehaviorAttributesBuilder setPreplanned(boolean preplanned)
    {
        behAttr.setPrePlan(preplanned);
        return this;
    }
    
    public BMLABMLBehaviorAttributesBuilder addToChunkAfter(String ...bmlIds)
    {
        behAttr.addToChunkAfter(bmlIds);
        return this;
    }
    
    public BMLABMLBehaviorAttributesBuilder addToAppendAfter(String ...bmlIds)
    {
        behAttr.addToAppendAfter(bmlIds);
        return this;
    }
    
    public BMLABMLBehaviorAttributesBuilder addToChunkBefore(String ...bmlIds)
    {
        behAttr.addToChunkBefore(bmlIds);
        return this;
    }
    
    public BMLABMLBehaviorAttributesBuilder addToPrependBefore(String ...bmlIds)
    {
        behAttr.addToPrependBefore(bmlIds);
        return this;
    }
    
    public BMLABMLBehaviorAttributesBuilder addToInterrupt(String ...bmlIds)
    {
        behAttr.addToInterrupt(bmlIds);
        return this;
    }
    
    public BMLABMLBehaviorAttributesBuilder addToOnStart(String ...bmlIds)
    {
        behAttr.addToOnStart(bmlIds);
        return this;
    }
    
    public BMLABMLBehaviorAttributes build()
    {
        return behAttr;
    }
}
