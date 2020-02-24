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
package asap.bml.ext.bmla.feedback;

import hmi.xml.XMLNameSpace;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;
import java.util.List;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import saiba.bml.BMLInfo;

import lombok.Getter;
import saiba.bml.feedback.BMLBlockProgressFeedback;
import asap.bml.ext.bmla.BMLAInfo;
import asap.bml.ext.bmla.BMLAPrefix;

/**
 * BMLBlockProgressFeedback with posixTime
 
 revised parsing by Dennis
 
 * @author hvanwelbergen
 */
public class BMLABlockProgressFeedback extends BMLBlockProgressFeedback
{
    @Getter
    private long posixTime = 0;   
    
    @Getter
    private BMLABlockStatus status = BMLABlockStatus.NONE;
    
    private static final String POSIXTIME_ID = BMLAInfo.BMLA_NAMESPACE+":"+"posixTime";
    private static final String STATUS_ID = BMLAInfo.BMLA_NAMESPACE+":"+"status";
    
    public BMLABlockProgressFeedback()
    {
        super();
    }
    
    public BMLABlockProgressFeedback(String bmlId, String syncId, double globalTime, BMLABlockStatus status)
    {
        this(bmlId, syncId, globalTime, System.currentTimeMillis(), status);
    }
    
    public BMLABlockProgressFeedback(String bmlId, String syncId, double globalTime, long posixTime, BMLABlockStatus status)
    {
        super(bmlId, syncId, globalTime);
        setPosixTime(posixTime);
        setStatus(status);
    }
    
    public static BMLABlockProgressFeedback build(BMLBlockProgressFeedback fb)
    {
        BMLABlockProgressFeedback fbNew = new BMLABlockProgressFeedback();
        fbNew.readXML(fb.toXMLString());
        return fbNew;
    }
    
    private void setStatus(BMLABlockStatus status)
    {
        this.status = status;
        if(status != BMLABlockStatus.NONE)
        {
            addCustomStringParameterValue(STATUS_ID, status.toString());
        }
    }
    
    private void setPosixTime(long time)
    {
        posixTime = time;
        if(posixTime>0)
        {
            addCustomStringParameterValue(POSIXTIME_ID, ""+posixTime);
        }
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        super.decodeAttributes(attrMap, tokenizer);
        if (specifiesCustomStringParameter(POSIXTIME_ID))
        {
            setPosixTime(Long.parseLong(getCustomStringParameterValue(POSIXTIME_ID)));
        }
        if (specifiesCustomStringParameter(STATUS_ID))
        {
            setStatus(BMLABlockStatus.valueOf(getCustomStringParameterValue(STATUS_ID)));
        }
    }
    
    @Override
    public String toBMLFeedbackString(List<XMLNameSpace> xmlNamespaceList)
    {
        return super.toBMLFeedbackString(BMLAPrefix.insertBMLANamespacePrefix(xmlNamespaceList));
    }
}
