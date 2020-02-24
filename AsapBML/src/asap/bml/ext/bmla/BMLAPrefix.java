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

import com.google.common.collect.ImmutableList;

/**
 * Utility class to insert a bmla prefix for the bmla namespace
 * @author hvanwelbergen
 *
 */
public final class BMLAPrefix
{
    private final static boolean findPrefix(String prefix, List<XMLNameSpace> xmlNamespaceList)
    {
        for(XMLNameSpace ns:xmlNamespaceList)
        {
            if(ns.getPrefix().equals(prefix))
            {
                return true;
            }
        }
        return false;
    } 
    
    private final static boolean findNameSpace(String nameSpace, List<XMLNameSpace> xmlNamespaceList)
    {
        for(XMLNameSpace ns:xmlNamespaceList)
        {
            if(ns.getNamespace().equals(nameSpace))
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Inserts a bmla namespace prefix for the BMLA namespace if it is not yet in xmlNamespaceList.
     * @return the new namespacelist with the bmla namespace.
     */
    public final static List<XMLNameSpace> insertBMLANamespacePrefix(List<XMLNameSpace> xmlNamespaceList)
    {
        if(!findPrefix("bmla", xmlNamespaceList) && !findNameSpace(BMLAInfo.BMLA_NAMESPACE, xmlNamespaceList))
        {
            xmlNamespaceList = new ImmutableList.Builder<XMLNameSpace>().addAll(xmlNamespaceList).add(new XMLNameSpace("bmla",BMLAInfo.BMLA_NAMESPACE)).build();            
        }
        return xmlNamespaceList;
    }
}
