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
import java.lang.NumberFormatException;

import lombok.Getter;
import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import asap.bml.ext.bmla.BMLAInfo;
import asap.bml.ext.bmla.BMLAPrefix;

/**
 * Extension of BMLSyncPointProgressFeedback to allow posix times in feedback
 * @author hvanwelbergen
 * 
 */
public class BMLASyncPointProgressFeedback extends BMLSyncPointProgressFeedback
{
    @Getter
    private long posixTime = 0;
    private static final String POSIXTIME_ID = BMLAInfo.BMLA_NAMESPACE + ":" + "posixTime";

    public BMLASyncPointProgressFeedback()
    {
        super();
    }

    public BMLASyncPointProgressFeedback(String bmlId, String behaviorId, String syncId, double time, double globalTime, long posixTime)
    {
        super(bmlId, behaviorId, syncId, time, globalTime);
        setPosixTime(posixTime);
    }

    public static BMLASyncPointProgressFeedback build(BMLSyncPointProgressFeedback fb)
    {
        BMLASyncPointProgressFeedback fbNew = new BMLASyncPointProgressFeedback();
        fbNew.readXML(fb.toXMLString());
        return fbNew;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
		super.decodeAttributes(attrMap, tokenizer);
		try
		{
			if (caHandler.getCustomStringParameterValue(POSIXTIME_ID)!=null && !caHandler.getCustomStringParameterValue(POSIXTIME_ID).equals(""))
			{
				setPosixTime(Long.parseLong(caHandler.getCustomStringParameterValue(POSIXTIME_ID)));
			}
		} catch (NumberFormatException ex) {
			throw new RuntimeException("error converting posixTime to long");			
		}
	}
	
    public void setPosixTime(long time)
    {
        posixTime = time;
        if (posixTime > 0)
        {
            addCustomStringParameterValue(POSIXTIME_ID, ""+posixTime);
        }
    }

    @Override
    public String toBMLFeedbackString(List<XMLNameSpace> xmlNamespaceList)
    {
        return super.toBMLFeedbackString(BMLAPrefix.insertBMLANamespacePrefix(xmlNamespaceList));
    }
}
