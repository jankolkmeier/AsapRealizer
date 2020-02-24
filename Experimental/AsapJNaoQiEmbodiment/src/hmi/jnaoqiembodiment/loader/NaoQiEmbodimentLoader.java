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
package hmi.jnaoqiembodiment.loader;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jnaoqiembodiment.NaoQiEmbodiment;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import lombok.Getter;

/**
 * Loads a naoqiemboidment
 * @author welberge
 * 
 */
public class NaoQiEmbodimentLoader implements EmbodimentLoader
{
    private String id = "";
    private NaoQiEmbodiment embodiment;

    @Override
    public String getId()
    {
        return id;
    }

    private static class NaoQiElement extends XMLStructureAdapter
    {
        @Getter
        private String ip;
        @Getter
        private int port;

        private static final String XMLTAG = "naoqi";

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            ip = getRequiredAttribute("ip", attrMap, tokenizer);
            port = getRequiredIntAttribute("port", attrMap, tokenizer);
            super.decodeAttributes(attrMap, tokenizer);
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        NaoQiElement nq = null;
        if (tokenizer.atSTag(NaoQiElement.xmlTag()))
        {
            nq = new NaoQiElement();
            nq.readXML(tokenizer);
        }
        if (nq == null)
        {
            throw new XMLScanException("No inner noaqi element in NaoQiEmbodimentLoader");
        }
        embodiment = new NaoQiEmbodiment(id, nq.getIp(), nq.getPort());
    }

    @Override
    public void unload()
    {

    }

    @Override
    public NaoQiEmbodiment getEmbodiment()
    {
        return embodiment;
    }

}
