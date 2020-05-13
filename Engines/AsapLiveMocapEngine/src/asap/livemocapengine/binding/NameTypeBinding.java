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
package asap.livemocapengine.binding;

import java.util.HashMap;
import java.util.Map;

/**
 * Creates a map of name+(class)type to an instance of that type
 * @author welberge
 */
public class NameTypeBinding
{
    private Map<String, Map<String,Object>> typeMap = new HashMap<String, Map<String,Object>>();

    @SuppressWarnings("unchecked")
    public <T extends Object> T get(String name, Class<T> type)
    {
        if(typeMap.get(name)!=null)
        {
            return (T) typeMap.get(name).get(type.getName());
        }
        return null;
    }
    
    public <T extends Object> void put(String name, Class<T> type, T value)
    {
        Map<String,Object> map = typeMap.get(name);
        if(map==null)
        {
            map = new HashMap<String,Object>();
            typeMap.put(name, map);
        }
        map.put(type.getName(), value);        
    }
    
    public <T extends Object> void put(String name, String typeName, T value)
    {
        Map<String,Object> map = typeMap.get(name);
        if(map==null)
        {
            map = new HashMap<String,Object>();
            typeMap.put(name, map);
        }
        map.put(typeName, value);        
    }
    
    public void remove(String name)
    {
        typeMap.remove(name);
    }
}
