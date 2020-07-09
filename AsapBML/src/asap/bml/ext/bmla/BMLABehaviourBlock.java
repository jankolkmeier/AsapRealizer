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
package asap.bml.ext.bmla;

import hmi.xml.XMLNameSpace;

import java.util.List;
import java.util.Set;

import saiba.bml.core.BMLBehaviorAttributeExtension;
import saiba.bml.core.BehaviourBlock;

/**
 * Enhances BehaviourBlock with some boilerplate code to parse/create BML blocks that contain BMLA elements.
 * @author hvanwelbergen
 *
 */
public class BMLABehaviourBlock extends BehaviourBlock
{
    final BMLABMLBehaviorAttributes bbmlbExt; 
    
    public boolean isPrePlanned()
    {
        return bbmlbExt.isPrePlanned();
    }

    public List<String> getOnStartList()
    {
        return bbmlbExt.getOnStartList();
    }

    public Set<String> getAppendAfterList()
    {
        return bbmlbExt.getAppendAfterList();
    }

    public Set<String> getChunkAfterList()
    {
        return bbmlbExt.getChunkAfterList();
    }

    public Set<String> getPrependBeforeList()
    {
        return bbmlbExt.getPrependBeforeList();
    }

    public Set<String> getChunkBeforeList()
    {
        return bbmlbExt.getChunkBeforeList();
    }

    public List<String> getInterruptList()
    {
        return bbmlbExt.getInterruptList();
    }

    public Set<String> getOtherBlockDependencies()
    {
        return bbmlbExt.getOtherBlockDependencies();
    }

    public BMLABehaviourBlock(BMLBehaviorAttributeExtension... bmlBehaviorAttributeExtensions)
    {
        super(bmlBehaviorAttributeExtensions);
        BMLABMLBehaviorAttributes ext = getBMLAAttribute(bmlBehaviorAttributeExtensions);
        if (ext == null)
        {
            bbmlbExt = new BMLABMLBehaviorAttributes();
            addBMLBehaviorAttributeExtension(bbmlbExt);
        }
        else
        {
            bbmlbExt = ext;
        }        
    }
    
    private BMLABMLBehaviorAttributes getBMLAAttribute(BMLBehaviorAttributeExtension... bmlBehaviorAttributeExtensions)
    {
        for (BMLBehaviorAttributeExtension ext:bmlBehaviorAttributeExtensions)
        {
            if(ext instanceof BMLABMLBehaviorAttributes)
            {
                return (BMLABMLBehaviorAttributes)ext;
            }
        }
        return null;
    }
    
    
    
    @Override
    public String toBMLString(List<XMLNameSpace> xmlNamespaceList)
    {
        return super.toBMLString(BMLAPrefix.insertBMLANamespacePrefix(xmlNamespaceList));
    }    
}
