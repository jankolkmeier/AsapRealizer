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
package asap.bml.ext.bmlt.builder;

import saiba.bml.builder.BehaviourBuilder;
import saiba.bml.core.Behaviour;
import asap.bml.ext.bmlt.BMLTBehaviour;
import asap.bml.ext.bmlt.BMLTProcAnimationBehaviour;

/**
 * Builds a BMLTProcAnimationBehaviour
 * @author Herwin
 *
 */
public class BMLTProcAnimationBehaviourBuilder
{
    private final BehaviourBuilder builder;
    
    public BMLTProcAnimationBehaviourBuilder(String bmlId, String id, String name)
    {
        builder = new BehaviourBuilder(BMLTProcAnimationBehaviour.xmlTag(), bmlId, id);
        builder.param("name",name);
        builder.namespace(BMLTBehaviour.BMLTNAMESPACE);        
    }
    
    public Behaviour build()
    {
        return builder.build();
    }
}
