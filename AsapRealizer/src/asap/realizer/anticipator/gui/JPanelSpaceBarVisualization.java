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
package asap.realizer.anticipator.gui;


import java.awt.Color;
import java.util.Observable;

import javax.swing.JPanel;

import asap.realizer.anticipator.KeyInfo;

/**
 * Visualizes the spacebar presses on a green/red jpanel
 * @author Herwin
 *
 */
public class JPanelSpaceBarVisualization extends SpacebarAnticipatorVisualization
{
    private final JPanel panel;
    
    public JPanelSpaceBarVisualization(JPanel jp, KeyInfo ki)
    {
        super(ki);
        panel = jp;
        panel.setBackground(Color.GREEN);
        panel.setForeground(Color.GREEN);
    }

    @Override
    public void update(Observable o, Object arg)
    {
        if(keyInfo.isPressed())
        {
            panel.setBackground(Color.GREEN);          
            panel.setForeground(Color.GREEN);
        }
        else
        {
            panel.setBackground(Color.RED);
            panel.setForeground(Color.RED);
        }
        panel.repaint();
    }
}
