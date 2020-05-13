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
package asap.picture.display;

public interface PictureDisplay
{

    /**
     * Pre-load an image from the given resource and cache it for future access
     */
    void preloadImage(String imageId, String resourcePath, String fileName);

    /**
     * Removes all current images and displays only the image img, at layer 'z'
     * @param id the id of the planunit to which this image belongs
     * @param img the image to be displayed
     */
    void setImage(String planUnitId, String imageId, float z);

    /**
     * Removes the image at a given layer 'z'
     * @param id the id of the planunit to which this image belongs
     * @param z the layer of which to remove the image
     */
    void removeImage(String planUnitId, float z);

    /**
     * Adds an image img onto layer z. THe previous image on that layer is 'remembered'; i.e.
     * when this image is removed, the previous one will appear again! This allows for 'base pose images'.
     * @param id the id of the planunit to which this image belongs
     * @param img the image to be added
     * @param layer the layer on which to add the image
     */
    void addImage(String planUnitId, String imageId, float layer);

    /**
     * Replaces the image of planunit id on layer z with a new image
     * @param id the id of the planunit
     * @param img the image file
     * @param layer the layer
     */
    void replaceImage(String planUnitId, String imageId, float layer);
}
