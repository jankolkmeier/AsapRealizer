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
package asap.faceengine.loader;

import hmi.faceanimation.converters.FACSConverter;
import hmi.util.Resources;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import asap.faceengine.viseme.FACSVisemeBinding;
import asap.faceengine.viseme.MorphVisemeBinding;
import asap.faceengine.viseme.VisemeBinding;
import asap.faceengine.viseme.VisemeToFACSMapping;
import asap.faceengine.viseme.VisemeToMorphMapping;

/**
 * Utility class to load the VisemeBinding
 * @author hvanwelbergen
 */
public final class VisemeBindingLoader
{
    private VisemeBindingLoader()
    {
    }

    public static boolean isAtVisemeBindingTag(XMLTokenizer tokenizer) throws IOException
    {
        return tokenizer.atSTag("MorphVisemeBinding")||tokenizer.atSTag("FACSVisemeBinding");
    }
    
    public static VisemeBinding load(XMLTokenizer tokenizer, FACSConverter fc) throws IOException
    {
        VisemeBinding visBinding = null;

        if (tokenizer.atSTag("MorphVisemeBinding"))
        {
            visBinding = loadMorphVisemeBinding(tokenizer);
        }
        else if (tokenizer.atSTag("FACSVisemeBinding"))
        {
            visBinding = loadFACSVisemeBinding(tokenizer, fc);
        }
        return visBinding;
    }

    private static FACSVisemeBinding loadFACSVisemeBinding(XMLTokenizer tokenizer, FACSConverter fc) throws IOException
    {
        FACSVisemeBinding visBinding;
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        HashMap<String, String> attrMap = tokenizer.getAttributes();
        VisemeToFACSMapping mapping = new VisemeToFACSMapping();
        mapping.readXML(new Resources(adapter.getOptionalAttribute("resources", attrMap, "")).getReader(adapter.getRequiredAttribute(
                "filename", attrMap, tokenizer)));
        visBinding = new FACSVisemeBinding(mapping, fc);
        tokenizer.takeEmptyElement("FACSVisemeBinding");
        return visBinding;
    }

    public static MorphVisemeBinding loadMorphVisemeBinding(XMLTokenizer tokenizer) throws IOException
    {
        MorphVisemeBinding visBinding;
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        HashMap<String, String> attrMap = tokenizer.getAttributes();
        VisemeToMorphMapping mapping = new VisemeToMorphMapping();
        mapping.readXML(new Resources(adapter.getOptionalAttribute("resources", attrMap, "")).getReader(adapter.getRequiredAttribute(
                "filename", attrMap, tokenizer)));
        visBinding = new MorphVisemeBinding(mapping);
        tokenizer.takeEmptyElement("MorphVisemeBinding");
        return visBinding;
    }
}
