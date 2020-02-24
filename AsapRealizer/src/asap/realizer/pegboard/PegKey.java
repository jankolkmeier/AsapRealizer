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
package asap.realizer.pegboard;

import lombok.Getter;

import com.google.common.base.Objects;

/**
 * Stores sync point information, e.g. for quick lookups in the pegboard. 
 * @author hvanwelbergen
 */
public final class PegKey
{
    public PegKey(String bmlId, String id, String syncId)
    {
        this.syncId = syncId;
        this.id = id;
        this.bmlId = bmlId;
    }
    @Override 
    public boolean equals(Object o)
    {
        if(!(o instanceof PegKey))return false;
        PegKey pk = (PegKey)o;
        return pk.bmlId.equals(bmlId)&&pk.id.equals(id)&&pk.syncId.equals(syncId);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(syncId,id,bmlId);            
    }
    
    @Override
    public String toString()
    {
        return "PegKey("+bmlId+":"+id+":"+syncId+")";
    }
    
    @Getter
    final String syncId;
    @Getter
    final String id;
    @Getter
    final String bmlId;
}
