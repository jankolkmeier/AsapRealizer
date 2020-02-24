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
package asap.faceengine.viseme;

import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
/**
 * given a viseme number, return the appropriate morph target name. 
 * 
 * The mapping is read from a resource file. Note: meaning of viseme number dependent on chose viseme set, e.g., Disney 13, or IKP
 *
 * @author Dennis Reidsma
 */
public class VisemeToMorphMapping extends XMLStructureAdapter
{
    
    private Map<String,MorphVisemeDescription> mappings = new HashMap<String,MorphVisemeDescription>();

    /**
     * Get the set of morph ids used in the mapping
     */
    public Set<String> getUsedMorphs()
    {
        Set<String> morphs = new HashSet<String>();
        for(Entry<String,MorphVisemeDescription> entry:mappings.entrySet())
        {
            morphs.addAll(entry.getValue().morphNames);
        }
        return ImmutableSet.copyOf(morphs);
    }
    
    /**
     * Get the morph target name for viseme vis. Returns null if not found.
     */
    public MorphVisemeDescription getMorphTargetForViseme(int vis)
    {
      return mappings.get(String.valueOf(vis));
    }

    /**
     * Get the morph target name for viseme vis. Returns null if not found.
     */
    public MorphVisemeDescription getMorphTargetForViseme(String vis)
    {
      return mappings.get(vis);
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (!tag.equals("Mapping")) throw new XMLScanException("Unknown element in VisemeToMorphMapping: "+tag);
            HashMap<String, String> attrMap = tokenizer.getAttributes();
            String viseme = getRequiredAttribute("viseme", attrMap, tokenizer);
            String target = getRequiredAttribute("target", attrMap, tokenizer);
            float intensity = getOptionalFloatAttribute("intensity",attrMap, 1f);
            mappings.put(viseme,new MorphVisemeDescription(target.split(","),intensity));
            tokenizer.takeSTag("Mapping");
            tokenizer.takeETag("Mapping");
        }
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "VisemeToMorphMapping";
 
    /**
     * The XML Stag for XML encoding -- use this static method when you want to see if a given String equals
     * the xml tag for this class
     */
    public static String xmlTag() { return XMLTAG; }
 
    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time xml tag of an object
     */
    @Override
    public String getXMLTag() {
       return XMLTAG;
    }  
}