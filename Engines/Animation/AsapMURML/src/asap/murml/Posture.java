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
package asap.murml;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;

import com.google.common.base.Joiner;
import com.google.common.primitives.Floats;

/**
 * Parses a MURML posture
 * @author hvanwelbergen
 *
 */
public class Posture extends MURMLElement
{
    @Getter
    private List<JointValue> jointValues = new ArrayList<JointValue>();
    
    @Override
    public boolean hasContent()
    {
        return true;
    }
    
    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        for(JointValue v:jointValues)
        {
            buf.append("(");
            buf.append(v.jointSid);
            buf.append(" ");
            if(v.dofs.length>1)
            {
                buf.append(v.dofs.length+" ");
            }
            buf.append(Joiner.on(" ").join(Floats.asList(v.dofs)));
            buf.append(")");
        }
        return buf;
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        StringBuffer contentBuffer = new StringBuffer();
        while (!tokenizer.atETag())
        {
            if (tokenizer.atCDSect())
            {
                contentBuffer = contentBuffer.append(tokenizer.takeCDSect());
            }
            else if (tokenizer.atCharData())
            {
                contentBuffer = contentBuffer.append(tokenizer.takeCharData());
            }
        }
        Pattern regex = Pattern.compile("(?<=\\()(.+?)(?=\\))"); //match everything inside braces
        Matcher regexMatcher = regex.matcher(contentBuffer.toString());
        List<String> matchList = new ArrayList<String>();
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }

        for (String spec:matchList)
        {
            String specElements[]=spec.split("\\s");
            String jointId = specElements[0];
            int nrOfDof = 1;
            int valueOffset = 1;
            if(specElements.length>2)
            {
                nrOfDof = Integer.parseInt(specElements[1]);
                valueOffset = 2;
            }
            float dofs[] = new float[nrOfDof];
            for(int i=0;i<nrOfDof;i++)
            {
                dofs[i] = Float.parseFloat(specElements[i+valueOffset]);
            }
            jointValues.add(new JointValue(jointId,dofs));
        }
    }
    
    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "posture";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time
     * xml tag of an object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
