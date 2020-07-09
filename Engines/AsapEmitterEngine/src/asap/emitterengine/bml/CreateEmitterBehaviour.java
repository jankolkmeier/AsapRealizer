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
package asap.emitterengine.bml;
import hmi.xml.XMLTokenizer;

import java.util.HashMap;

import asap.bml.ext.bmlt.BMLTParameter;
import asap.emitterengine.EmitterInfo;

/**
 * Create Emitter behavior
 * @author Dennis Reidsma
 */
public class CreateEmitterBehaviour extends EmitterBehaviour
{

    public CreateEmitterBehaviour(String bmlId)
    {
        super(bmlId);        
    }
    
    static EmitterInfo emitterInfo = null;
     
    protected static void setEmitterInfo(EmitterInfo ei)
    {
      emitterInfo = ei;
    }    

    @Override
    public boolean specifiesParameter(String name)
    {
        return emitterInfo.specifiesFloatParameter(name)||emitterInfo.specifiesStringParameter(name);
    }
    
    @Override
    public StringBuilder appendAttributeString(StringBuilder buf)
    {
        for (String name: emitterInfo.getRequiredParameters())
        {
          appendAttribute(buf, name, getStringParameterValue(name));
        }
        for (String name: emitterInfo.getOptionalParameters())
        {
          if (getStringParameterValue(name) != null)
          {
            appendAttribute(buf, name, getStringParameterValue(name));
          }
        }
        return super.appendAttributeString(buf);
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        for (String name: emitterInfo.getRequiredParameters())
        {
            BMLTParameter p = new BMLTParameter();
            p.name=name;
            p.value=getRequiredAttribute(name, attrMap, tokenizer);
            parameters.put(name, p);
        }
        for (String name: emitterInfo.getOptionalParameters())
        {
          String value = getOptionalAttribute(name, attrMap);
          if (value != null)
          {
            BMLTParameter p = new BMLTParameter();
            p.name=name;
            p.value=value;
            parameters.put(name, p);
          }
        }
        super.decodeAttributes(attrMap, tokenizer);
    }
    

    @Override
    public String getNamespace()
    {
        return emitterInfo.getNamespace();
    }

    /**
     * The XML Stag for XML encoding -- use this static method when you want to see if a given
     * String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return emitterInfo.getXMLTag();
    }

    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time xml tag of an
     * object
     */
    @Override
    public String getXMLTag()
    {
        return emitterInfo.getXMLTag();
    }    
}
