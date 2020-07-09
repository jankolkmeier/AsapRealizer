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
package asap.bml.ext.murml;

import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.List;

import saiba.bml.core.GestureBehaviour;
import saiba.bml.parser.SyncPoint;

/**
 * Extension for MURML gesture behaviours
 * @author hvanwelbergen
 */
public class MURMLGestureBehaviour extends MURMLBehaviour
{
    public MURMLGestureBehaviour(String bmlId)
    {
        super(bmlId);
    }

    public MURMLGestureBehaviour(String bmlId, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }

    public MURMLGestureBehaviour(String bmlId, String id, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId, id);
        readXML(tokenizer);
    }

    private static final String XMLTAG = "murmlgesture";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }

    private static final List<String> DEFAULT_SYNCS = GestureBehaviour.getDefaultSyncPoints();

    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    @Override
    public void addDefaultSyncPoints()
    {
        for (String s : getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }
    }
}
