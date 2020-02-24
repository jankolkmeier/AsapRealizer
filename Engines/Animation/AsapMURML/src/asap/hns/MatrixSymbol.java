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
package asap.hns;

import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;

import com.google.common.primitives.Floats;

/**
 * Parses a symbol inside symbolMatrices
 * @author hvanwelbergen
 *
 */
public class MatrixSymbol extends XMLStructureAdapter
{
    @Getter
    private String className;
    
    @Getter
    private String name;
    
    private float values[];
    public float[] getValues()
    {
        return Arrays.copyOf(values, values.length);
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        className = getRequiredAttribute("class", attrMap, tokenizer);
        name = getRequiredAttribute("name", attrMap, tokenizer);
        String str = getRequiredAttribute("value", attrMap, tokenizer);
        String strValues[] = str.split("\\s+");         
        List<Float>valList = new ArrayList<>();
        for(String val:strValues)
        {
            valList.add(Float.parseFloat(val));
        }
        values = Floats.toArray(valList);
    }
    
    static final String XMLTAG = "symbol";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
