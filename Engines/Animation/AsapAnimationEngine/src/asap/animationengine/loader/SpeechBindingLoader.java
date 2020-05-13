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
package asap.animationengine.loader;

import hmi.util.Resources;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import asap.animationengine.gesturebinding.SpeechBinding;

/**
 * Utility class to load a speechbinding
 * @author hvanwelbergen
 *
 */
final class SpeechBindingLoader
{
    private SpeechBindingLoader(){}
    
    static SpeechBinding load(XMLTokenizer tokenizer) throws IOException
    {
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        SpeechBinding sb = null;
        if (tokenizer.atSTag("SpeechBinding"))
        {

            HashMap<String, String> attrMap = tokenizer.getAttributes();
            sb = new SpeechBinding(new Resources(adapter.getOptionalAttribute("basedir", attrMap, "")));
            try
            {
                sb.readXML(new Resources(adapter.getOptionalAttribute("resources", attrMap, "")).getReader(adapter
                        .getRequiredAttribute("filename", attrMap, tokenizer)));
            }
            catch (Exception e)
            {
                throw new RuntimeException("Cannnot load SpeechBinding: " + e);
            }
            tokenizer.takeEmptyElement("SpeechBinding");
        }
        return sb;
    }
}
