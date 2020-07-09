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

import hmi.util.SwingResources;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import asap.picture.display.PictureDisplay;

/**
 * Connects a parent JComponent to an imagepanel that handles the picturedisplay
 * @author hvanwelbergen
 * 
 */
public class PictureJComponent implements PictureDisplay
{
    private ImagePanel content;
    private HashMap<String, Image> cachedImages = new HashMap<String, Image>();
    private TreeMap<Float, ImageLayer> layers;
    private Logger logger = LoggerFactory.getLogger(PictureJFrame.class.getName());
    private int width = 400;
    private int height = 400; 

    /**
     * Pre-load an image from the given resource and cache it for future access
     */
    public void preloadImage(String imageId, String resourcePath, String fileName)
    {
        Image image = cachedImages.get(imageId);
        if (image == null)
        {
            ImageIcon imageIcon = (new SwingResources(resourcePath)).getImageIcon(fileName);

            // getImageIcon returns null if image is not found, so we must check to see if the image has loaded correctly
            if (imageIcon == null)
            {
                logger.warn("Error while reading image file from: " + resourcePath + fileName);
            }
            else
            {
                image = imageIcon.getImage();
                cachedImages.put(imageId, image);
            }

        }
    }

    public PictureJComponent(JComponent parent)
    {
    	this (parent, 400,400);
    }
    public PictureJComponent(JComponent parent, int w, int h)
    {
    	width=w;
    	height = h;
        content = new ImagePanel();
        content.setPreferredSize(new Dimension(w,h));
        content.setMinimumSize(new Dimension(w,h));

        parent.add(content, BorderLayout.CENTER);
        layers = new TreeMap<Float, ImageLayer>();
    }

    @Override
    public void setImage(String puId, String imageId, float z)
    {
        layers.clear();
        addImage(puId, imageId, z);
    }

    @Override
    public void removeImage(String id, float z)
    {
        ImageLayer layer = getImageLayer(z);
        if (layer == null)
        {
            logger.warn("No image to remove at layer {}", z);
            return;
        }
        int layersize = layer.removeImage(id);
        if (layersize == 0)
        {
            layers.remove(new Float(z));
        }

        redrawPicture();
    }

    @Override
    public void addImage(String id, String imageId, float z)
    {
        logger.debug("Adding image: {}  on layer: {}", imageId, z);
        ImageLayer layer = getImageLayer(z);
        Image img = cachedImages.get(imageId);
        if (img == null)
        {
            logger.warn("Image with id {} not available in cache", imageId);
        }
        else
        {
            layer.addImage(id, img);
            layers.put(new Float(z), layer);
        }
        redrawPicture();
    }

    @Override
    public void replaceImage(String id, String imageId, float z)
    {
        logger.debug("Replacing image: {} on layer: {}", imageId, z);
        ImageLayer layer = getImageLayer(z);
        Image img = cachedImages.get(imageId);
        if (img == null)
        {
            logger.warn("Image with id {} not available in cache", imageId);
        }
        else
        {
            layer.replaceImage(id, img);
            layers.put(new Float(z), layer);
        }
        redrawPicture();
    }

    /**
     * Get the ImageLayer object at layer z. This will create a new layer if it
     * does not currently exist
     * 
     * @param z the layer
     * @return the ImageLayer object at layer z
     */
    private ImageLayer getImageLayer(float z)
    {
        if (layers.containsKey(z))
        {
            return layers.get(z);
        }
        else
        {
            return new ImageLayer();
        }
    }

    /**
     * Function constructs a list of images and passes it to the display
     */
    private void redrawPicture()
    {
        // create a copy of the treemap containing all the images,
        // because it will throw a concurrent modification error otherwise..
        Vector<Image> images = new Vector<Image>();
        for (ImageLayer layer : (new TreeMap<Float, ImageLayer>(layers)).values())
        {
            if(layer.getActiveImage()!=null)
            {
                images.add(layer.getActiveImage());
            }
        }
        content.drawPicture(images);
    }
}
