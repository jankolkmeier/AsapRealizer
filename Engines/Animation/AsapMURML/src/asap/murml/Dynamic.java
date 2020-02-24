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
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Parses the MURML dynamic element
 * @author hvanwelbergen
 */
public class Dynamic extends MURMLElement implements MovementConstraint
{
    @Getter
    private Keyframing keyframing;

    @Getter
    private List<DynamicElement> dynamicElements = new ArrayList<>();

    @Getter
    private Slot slot;

    @Getter
    @Setter
    private String scope;

    @Getter
    @Setter
    private Symmetry symmetryTransform = Symmetry.Sym;

    public static Parallel constructMirror(Dynamic d, Dominant dominantHand, Symmetry sym)
    {
        Parallel p = new Parallel();
        d.setScope(dominantHand.toString().toLowerCase());
        p.add(d);
        p.add(Dynamic.mirror(d, sym));
        return p;
    }

    public static Dynamic mirror(Dynamic d, Symmetry s)
    {
        Dynamic dMirror = new Dynamic();
        dMirror.slot = d.slot;
        dMirror.setSymmetryTransform(s);
        if (d.scope.equals("left_arm"))
        {
            dMirror.scope = "right_arm";
        }
        else
        {
            dMirror.scope = "left_arm";
        }
        for (DynamicElement de : d.dynamicElements)
        {
            dMirror.dynamicElements.add(de.copy());
        }

        return dMirror;
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            switch (tag)
            {
            case Keyframing.XMLTAG:
                keyframing = new Keyframing();
                keyframing.readXML(tokenizer);
                break;
            case DynamicElement.XMLTAG:
                DynamicElement dynamicElement = new DynamicElement();
                dynamicElement.readXML(tokenizer);
                dynamicElements.add(dynamicElement);
                break;
            default:
                throw new XMLScanException("Unkown tag " + tag + " in <dynamic>");
            }
        }
    }

    @Override
    public StringBuilder appendAttributes(StringBuilder buf)
    {
        if (scope != null)
        {
            appendAttribute(buf, "scope", scope);
        }
        if (slot != null)
        {
            appendAttribute(buf, "slot", slot.toString());
        }
        return buf;
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (keyframing != null)
        {
            keyframing.appendXML(buf, fmt);
        }
        for (DynamicElement dynamicElement : dynamicElements)
        {
            dynamicElement.appendXML(buf, fmt);
        }
        return buf;
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        scope = getOptionalAttribute("scope", attrMap);
        String sl = getOptionalAttribute("slot", attrMap);
        if (sl != null)
        {
            slot = Slot.valueOf(sl);
        }
    }

    static final String XMLTAG = "dynamic";

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
