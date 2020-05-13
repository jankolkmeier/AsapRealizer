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
package asap.tcpipadapters.loader;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.loader.JComponentEmbodimentLoader;
import hmi.util.ArrayUtils;
import hmi.util.CollectionUtils;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.tcpipadapters.ui.BridgeServerUI;

/**
 * XML Loader for the BridgeServerUI
 * @author Herwin
 * 
 */
public class BridgeServerUILoader implements Loader
{
    private String id;

    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        JComponentEmbodimentLoader jcc = ArrayUtils.getFirstClassOfType(requiredLoaders, JComponentEmbodimentLoader.class);
        if (jcc == null)
        {
            throw new XMLScanException("BridgeServerUILoader requires an JComponentEmbodimentLoader");
        }
        AsapRealizerEmbodiment are = ArrayUtils.getFirstClassOfType(requiredLoaders, AsapRealizerEmbodiment.class);
        TCPIPToBMLRealizerAdapterLoader tcpipAdapterLoader = CollectionUtils.getFirstClassOfType(are.getPipeLoaders(),
                TCPIPToBMLRealizerAdapterLoader.class);
        jcc.getEmbodiment().addJComponent(
                new BridgeServerUI(tcpipAdapterLoader.getAdaptedRealizerPort(), tcpipAdapterLoader.getTcpIpAdapter()).getUI());
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void unload()
    {

    }
}
