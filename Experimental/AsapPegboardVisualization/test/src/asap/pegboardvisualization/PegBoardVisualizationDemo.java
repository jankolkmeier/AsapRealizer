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
package asap.pegboardvisualization;

import javax.swing.JFrame;

import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizertestutil.util.TimePegUtil;

/**
 * Test program to play around and try out the visualization
 * @author hvanwelbergen
 *
 */
public class PegBoardVisualizationDemo
{
    public static void main(String args[])
    {
        PegBoard pb = new PegBoard();
        
        pb.addBMLBlockPeg(new BMLBlockPeg("bml1",10));
        pb.addTimePeg("bml1","beh1", "start", TimePegUtil.createTimePeg(11));        
        pb.addTimePeg("bml1","beh1", "end", TimePegUtil.createTimePeg(15));
        pb.addTimePeg("bml1","beh2", "start", TimePegUtil.createTimePeg(12));
        pb.addTimePeg("bml1","beh2", "strokeStart", TimePegUtil.createTimePeg(13));
        pb.addTimePeg("bml1","beh2", "strokeEnd", TimePegUtil.createTimePeg(14));
        pb.addTimePeg("bml1","beh2", "end", TimePegUtil.createTimePeg(20));
        
        
        pb.addBMLBlockPeg(new BMLBlockPeg("bml2",30));
        pb.addTimePeg("bml2","beh1", "start", TimePegUtil.createTimePeg(31));        
        pb.addTimePeg("bml2","beh1", "end", TimePegUtil.createTimePeg(35));
        pb.addTimePeg("bml2","beh2", "start", TimePegUtil.createTimePeg(32));
        pb.addTimePeg("bml2","beh2", "strokeStart", TimePegUtil.createTimePeg(33));
        pb.addTimePeg("bml2","beh2", "strokeEnd", TimePegUtil.createTimePeg(34));
        pb.addTimePeg("bml2","beh2", "end", TimePegUtil.createTimePeg(40));
        
        JFrame jf = new JFrame();
        jf.add(new PegBoardVisualizer(pb));
        jf.setSize(1024,768);
        jf.setVisible(true);
        
        pb.addBMLBlockPeg(new BMLBlockPeg("bml3",30));
        pb.addTimePeg("bml3","beh1", "start", TimePegUtil.createTimePeg(31));        
        pb.addTimePeg("bml3","beh1", "end", TimePegUtil.createTimePeg(100));        
    }
}
