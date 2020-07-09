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
import hmi.xml.XMLScanException;


import saiba.bml.BMLInfo;

import lombok.Getter;
import saiba.bml.feedback.BMLBlockPredictionFeedback;
import asap.bml.ext.bmla.BMLAInfo;
import asap.bml.ext.bmla.BMLAPrefix;

/**
 * BMLBlockPredictionFeedback extension to provide posix timestamps
 modified parsing by Dennis Reidsma
 * @author hvanwelbergen
 * 
 */
public class BMLABlockPredictionFeedback extends BMLBlockPredictionFeedback
{
    private static final String POSIXSTARTTIME_ID = BMLAInfo.BMLA_NAMESPACE + ":" + "posixStartTime";
    private static final String POSIXENDTIME_ID = BMLAInfo.BMLA_NAMESPACE + ":" + "posixEndTime";
    private static final String STATUS_ID = BMLAInfo.BMLA_NAMESPACE+":"+"status";
    
    @Getter
    private long posixStartTime;

    @Getter
    private long posixEndTime;

    @Getter
    private BMLABlockStatus status;
    
    public BMLABlockPredictionFeedback()
    {
        super();
    }

    public static BMLABlockPredictionFeedback build(BMLBlockPredictionFeedback fb)
    {
        BMLABlockPredictionFeedback fbNew = new BMLABlockPredictionFeedback();
        fbNew.readXML(fb.toXMLString());
        return fbNew;
    }

    public BMLABlockPredictionFeedback(String id, double globalStart, double globalEnd, BMLABlockStatus status, long posixStart, long posixEnd)
    {
        super(id, globalStart, globalEnd);
        setStatus(status);
        setPosixStartTime(posixStart);
        setPosixEndTime(posixEnd);
    }

    private void setStatus(BMLABlockStatus status)
    {
        this.status = status;
        if(status!=BMLABlockStatus.NONE)
        {
            addCustomStringParameterValue(STATUS_ID, status.toString());
        }
    }
    
    private void setPosixStartTime(long time)
    {
        posixStartTime = time;
        if (posixStartTime > 0)
        {
            addCustomStringParameterValue(POSIXSTARTTIME_ID, ""+posixStartTime);
        }
    }

    private void setPosixEndTime(long time)
    {
        posixEndTime = time;
        if (posixStartTime > 0)
        {
            addCustomStringParameterValue(POSIXENDTIME_ID, ""+posixEndTime);
        }
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        super.decodeAttributes(attrMap, tokenizer);
        if (specifiesCustomStringParameter(POSIXSTARTTIME_ID))
        {
            setPosixStartTime(Long.parseLong(getCustomStringParameterValue(POSIXSTARTTIME_ID)));
        }
        if (specifiesCustomStringParameter(POSIXENDTIME_ID))
        {
            setPosixEndTime(Long.parseLong(getCustomStringParameterValue(POSIXENDTIME_ID)));
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
