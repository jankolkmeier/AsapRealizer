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
package asap.realizer;

/**
 * bmlId:id:syncId is not valid/found on behavior bmlId:id.
 * @author welberge
 */
public class SyncPointNotFoundException extends Exception
{
    private static final long serialVersionUID = 1L;
    private final String syncId;
    private final String id;
    private final String bmlId;
    
    public String getId()
    {
        return id;
    }

    public String getBmlId()
    {
        return bmlId;
    }

    public String getSyncId()
    {
        return syncId;
    }
    
    public SyncPointNotFoundException(String bmlId, String id, String syncId)
    {
        this.id = id;
        this.bmlId = bmlId;
        this.syncId = syncId;
    }
}
