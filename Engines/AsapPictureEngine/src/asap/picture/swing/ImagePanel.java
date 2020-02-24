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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Collection;

import javax.swing.JComponent;

public class ImagePanel extends JComponent
{
    private static final long serialVersionUID = 1L;

    private Collection<Image> images;

    public ImagePanel()
    {
        super();        
    }

    public void drawPicture(Collection<Image> images)
    {
        this.images = images;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g)
    {
    	if (images != null && images.size() > 0)
        {
            // System.out.println("nrimg: " +images.size());
            // iterate over all images in the collection, drawing each one on top of the other
            for (Image img : images)
            {
                g.drawImage(img, 0, 0, null);
            }
        }
        else
        {
            // images set is currently empty, so clear the display
            g.clearRect(0, 0, WIDTH, HEIGHT);
        }
    }
}
