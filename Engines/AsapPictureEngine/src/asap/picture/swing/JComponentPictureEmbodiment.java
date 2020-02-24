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
package asap.picture.swing;

import hmi.environmentbase.Embodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.jcomponentenvironment.JComponentEmbodiment;
import hmi.jcomponentenvironment.loader.JComponentEmbodimentLoader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JPanel;

import lombok.Getter;
import lombok.Setter;
import asap.picture.display.PictureDisplay;
import asap.picture.loader.PictureEmbodiment;

/**
 * Loads the PictureEmbodiment into an external JComponentEmbodiment
 * @author Herwin
 * 
 */
public class JComponentPictureEmbodiment implements EmbodimentLoader, PictureEmbodiment
{
    @Getter
    @Setter
    private String id = "";

    private PictureDisplay display;
    private JComponentEmbodiment jce;
    private JPanel jPanel;

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        JComponentEmbodimentLoader jcl = ArrayUtils.getFirstClassOfType(requiredLoaders, JComponentEmbodimentLoader.class);
        if (jcl == null)
        {
            throw new XMLScanException("JComponentPictureEmbodiment requires an JComponentEmbodimentLoader.");
        }
        jce = jcl.getEmbodiment();
        if (jce == null)
        {
            throw new XMLScanException("JComponentPictureEmbodiment: null embodiment in JComponentEmbodimentLoader.");
        }
        
        
        jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1,1));
        display = new PictureJComponent(jPanel);
        jce.addJComponent(jPanel);
    }

    @Override
    public void unload()
    {
        jce.removeJComponent(jPanel);
    }

    @Override
    public PictureDisplay getPictureDisplay()
    {
        return display;
    }

    @Override
    public Embodiment getEmbodiment()
    {
        return this;
    }
}
