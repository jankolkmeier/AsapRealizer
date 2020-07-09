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
package asap.bmlflowvisualizer.loader;

import javax.swing.JFrame;

import saiba.bmlflowvisualizer.BMLFlowVisualizerPort;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * Demo, playing around with the BMLFlowVisualization
 * @author Herwin
 * 
 */
public final class BMLFlowVisualizationTryout
{
    private BMLFlowVisualizationTryout()
    {
    }

    public static void main(String args[])
    {
        JFrame jf = new JFrame();

        RealizerPort rp = new RealizerPort()
        {
            @Override
            public void addListeners(BMLFeedbackListener... listeners)
            {

            }

            @Override
            public void removeAllListeners()
            {

            }

            @Override
            public void performBML(String bmlString)
            {

            }

            @Override
            public void removeListener(BMLFeedbackListener l)
            {
                                
            }
        };

        BMLFlowVisualizerPort port = new BMLFlowVisualizerPort(rp);
        jf.add(port.getVisualization());
        jf.setSize(1024, 768);
        jf.setVisible(true);
        port.performBML("<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml1\"/>");
        port.performBML("<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml2\"/>");
        port.performBML("<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml3\"/>");
        port.performBML("<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml4\"/>");
        port.performBML("<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml5\"/>");
        port.performBML("<bml xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\" id=\"bml6\"/>");

        port.feedback("<predictionFeedback xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\">"
                + "<bml id=\"bml1\" globalStart=\"1\" globalEnd=\"7\"/></predictionFeedback>");
        port.feedback("<predictionFeedback xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\">"
                + "<bml id=\"bml2\" globalStart=\"1\" globalEnd=\"7\"/></predictionFeedback>");
        port.feedback("<predictionFeedback xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\">"
                + "<bml id=\"bml3\" globalStart=\"1\" globalEnd=\"7\"/></predictionFeedback>");
        port.feedback("<predictionFeedback xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\">"
                + "<bml id=\"bml4\" globalStart=\"1\" globalEnd=\"7\"/></predictionFeedback>");

        port.feedback("<blockProgress id=\"bml1:start\" globalTime=\"1\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"/>");
        port.feedback("<blockProgress id=\"bml2:start\" globalTime=\"1\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"/>");
        port.feedback("<blockProgress id=\"bml3:start\" globalTime=\"3\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"/>");

        port.feedback("<blockProgress id=\"bml1:end\" globalTime=\"10\" xmlns=\"http://www.bml-initiative.org/bml/bml-1.0\"/>");
    }
}
