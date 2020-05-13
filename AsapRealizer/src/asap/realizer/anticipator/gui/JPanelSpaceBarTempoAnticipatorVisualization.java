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
import java.lang.reflect.InvocationTargetException;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import asap.realizer.anticipator.Anticipator;
import asap.realizer.anticipator.KeyInfo;
import asap.realizer.pegboard.TimePeg;

/**
 * Visualizes the prediction of spacebar presses on a red/green jpanel
 * @author Herwin
 *
 */
public class JPanelSpaceBarTempoAnticipatorVisualization extends SpaceBarTempoAnticipatorVisualization
{
    private final JPanel predictPanel;
    private final JPanelSpaceBarVisualization spaceBarPressViz;
    private final static double DISPLAY_DURATION = 0.1;

    public JPanelSpaceBarTempoAnticipatorVisualization(JPanel jpPress, JPanel jpPredict, KeyInfo keyInfo, Anticipator ant)
    {
        super(ant);
        spaceBarPressViz = new JPanelSpaceBarVisualization(jpPress, keyInfo);
        predictPanel = jpPredict;
    }

    @Override
    public void update(Observable arg0, Object arg1)
    {
        spaceBarPressViz.update(arg0, arg1);
    }

    public void update(double time)
    {
        for (TimePeg tp : anticipator.getTimePegs())
        {
            if (time - tp.getGlobalValue() < DISPLAY_DURATION && time - tp.getGlobalValue() > 0)
            {
                try
                {
                    SwingUtilities.invokeAndWait(() -> {
                        predictPanel.setBackground(Color.GREEN);
                        predictPanel.setForeground(Color.GREEN);
                    });
                }
                catch (InvocationTargetException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
        try
        {
            SwingUtilities.invokeAndWait(() -> {
                predictPanel.setBackground(Color.RED);
                predictPanel.setForeground(Color.RED);
            });
        }
        catch (InvocationTargetException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
