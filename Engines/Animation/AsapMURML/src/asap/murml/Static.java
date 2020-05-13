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

import hmi.xml.XMLTokenizer;

import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

/**
 * Parser for the static murml element
 * @author hvanwelbergen
 *
 */
public class Static extends MURMLElement implements MovementConstraint
{
    @Getter @Setter
    private String scope;
    
    @Getter
    private Slot slot;
    
    @Getter
    private String value;
    
    @Getter
    @Setter
    private Symmetry symmetryTransform = Symmetry.Sym;
    
    public static Parallel constructMirror(Static s, Dominant dominantHand, Symmetry sym)
    {
        Parallel p = new Parallel();
        s.setScope(dominantHand.toString().toLowerCase());
        p.add(s);
        p.add(Static.mirror(s, sym));
        return p;
    }
    
    public static Static mirror(Static s, Symmetry sym)
    {
        Static sMirror = new Static();
        if (s.scope.equals("left_arm"))
        {
            sMirror.scope = "right_arm";
        }
        else
        {
            sMirror.scope = "left_arm";
        }
        sMirror.setSymmetryTransform(sym);
        sMirror.slot = s.slot;
        sMirror.value = s.value;
        return sMirror;
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        scope = getOptionalAttribute("scope", attrMap);
        String sl = getOptionalAttribute("slot", attrMap);
        if(sl!=null)
        {
            slot = Slot.valueOf(sl);
        }
        value = getRequiredAttribute("value", attrMap, tokenizer);        
    }
    
    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if(scope!=null)
        {
            appendAttribute(buf, "scope", scope);
        }
        if(slot!=null)
        {
            appendAttribute(buf, "slot", slot.toString());
        }
        appendAttribute(buf, "value", value);
        return buf;
    }
    
    static final String XMLTAG = "static";

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
