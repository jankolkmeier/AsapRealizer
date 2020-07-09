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

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * This class implements a single layer which can contain multiple images.
 * The class will ensure that only one image (the most recently added) will be active at a time
 * @author Daniel
 * 
 */
public class ImageLayer
{

    private Map<String, Image> imageStore;
    private Vector<String> imageIndex;

    public ImageLayer()
    {
        imageIndex = new Vector<String>();
        imageStore = new HashMap<String, Image>();
    }

    /**
     * Adds an image to this layer, making it active
     * @param id a unique id by which this image is represented
     * @param img the image file
     */
    public int addImage(String id, Image img)
    {
        imageIndex.add(id);
        imageStore.put(id, img);
        return imageIndex.size();
    }

    /**
     * Removes an image from this layer, even if it is not active at the moment
     * @param id the id of the image to be removed
     */
    public int removeImage(String id)
    {
        imageIndex.removeElement(id);
        imageStore.remove(id);
        return imageIndex.size();
    }

    /**
     * Replaces an image in this layer with a new image.
     * If the id is not found in this layer, nothing is changed
     * @param id The id of the image
     * @param img The actual image file
     */
    public void replaceImage(String id, Image img)
    {
        if (imageIndex.contains(id))
        {
            imageStore.put(id, img);
        }
    }

    /**
     * Returns the active image of this layer, or null if there is no active image at this moment
     * @return the active image, or null if this layer is empty
     */
    public Image getActiveImage()
    {
        if(imageIndex.size()==0) return null;
        return imageStore.get(imageIndex.lastElement());
    }
}
