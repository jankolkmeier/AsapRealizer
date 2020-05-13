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

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import lombok.Delegate;
import asap.picture.display.PictureDisplay;

public class PictureJFrame implements PictureDisplay {

    @Delegate
    private PictureJComponent pictureComponent;
    private int width = 400; 
    private int height = 400;
    
    public PictureJFrame() {
    	this (400,400);
    }
    
    public PictureJFrame(int w, int h) {
    	width = w;
    	height = h;
        init();
    }

    private void init() {
        final JFrame frame = new JFrame();
        JPanel jPanel = new JPanel();
        jPanel.setSize(width,height);
        frame.getContentPane().add(jPanel);
        pictureComponent = new PictureJComponent(jPanel, width, height);
        frame.pack();
        frame.setVisible(true);
        //Dirty exit code
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                System.exit(0);
            }
        });
    }
    
    
    
}
