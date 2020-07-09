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
package asap.bml.bridge.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import saiba.bml.feedback.BMLBlockProgressFeedback;
import saiba.bml.feedback.BMLFeedback;
import saiba.bml.feedback.BMLFeedbackParser;
import saiba.bml.feedback.BMLPredictionFeedback;
import saiba.bml.feedback.BMLSyncPointProgressFeedback;
import saiba.bml.feedback.BMLWarningFeedback;
import asap.bml.ext.bmla.feedback.BMLABlockProgressFeedback;
import asap.bml.ext.bmla.feedback.BMLAPredictionFeedback;
import asap.bml.ext.bmla.feedback.BMLASyncPointProgressFeedback;
import asap.realizerport.BMLFeedbackListener;
import asap.realizerport.RealizerPort;

/**
 * User interface element to hook up to BML feedback information
 * @author reidsma, welberge
 */
public class FeedbackPanel extends JPanel implements BMLFeedbackListener
{
    // XXX class is not serializable (see findbugs). Better to make this class HAVE a panel rather than BE a panel
    private static final long serialVersionUID = 1L;
    /** Text area to give output feedback */
    private JTextArea feedbackOutput = null;
    private JTextArea warningOutput = null;
    private JTextArea predictionOutput = null;
    private JTabbedPane tabPane;

    private static final int PREF_WIDTH = 500;
    private static final int PREF_HEIGHT = 80;

    public FeedbackPanel(RealizerPort bridge)
    {
        super();
        // text plane for result output
        feedbackOutput = new JTextArea();
        feedbackOutput.setEditable(false);
        JScrollPane resultScroll = new JScrollPane(feedbackOutput);
        resultScroll.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));

        predictionOutput = new JTextArea();
        predictionOutput.setEditable(false);
        JScrollPane planningScroll = new JScrollPane(predictionOutput);
        planningScroll.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));

        warningOutput = new JTextArea();
        warningOutput.setEditable(false);
        JScrollPane warningScroll = new JScrollPane(warningOutput);
        warningScroll.setPreferredSize(new Dimension(PREF_WIDTH, PREF_HEIGHT));

        tabPane = new JTabbedPane();
        tabPane.addTab("Warnings", warningScroll);
        tabPane.addTab("Feedback", resultScroll);
        tabPane.addTab("Prediction", planningScroll);
        add(tabPane);
        JButton clearButton = new JButton("clear");
        clearButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
                clear();
            }
        });
        add(clearButton);
        bridge.addListeners(this);
    }

    public void clear()
    {
        feedbackOutput.setText("");
        warningOutput.setText("");
        predictionOutput.setText("");
    }

    public void appendWarning(String text)
    {
        warningOutput.append(text);
        warningOutput.setCaretPosition(warningOutput.getText().length());
    }

    public void blockProgress(BMLABlockProgressFeedback psf)
    {
        feedbackOutput.append(psf.toBMLFeedbackString());
    }

    public void syncProgress(BMLASyncPointProgressFeedback spp)
    {
        feedbackOutput.append(spp.toBMLFeedbackString());
    }

    public void prediction(BMLAPredictionFeedback bpf)
    {
        predictionOutput.append(bpf.toBMLFeedbackString());
    }

    public void warn(BMLWarningFeedback bw)
    {
        // show the feedback from the realizer
        warningOutput.append(bw.toBMLFeedbackString());
        warningOutput.setCaretPosition(warningOutput.getText().length());
    }

    @Override
    public void feedback(String feedback)
    {
        try
        {
            BMLFeedback fb = BMLFeedbackParser.parseFeedback(feedback);
            if(fb instanceof BMLPredictionFeedback)
            {
                prediction(BMLAPredictionFeedback.build((BMLPredictionFeedback)fb));
            }
            else if (fb instanceof BMLBlockProgressFeedback)
            {
                blockProgress(BMLABlockProgressFeedback.build((BMLBlockProgressFeedback)fb));
            }
            else if (fb instanceof BMLSyncPointProgressFeedback)
            {
                syncProgress(BMLASyncPointProgressFeedback.build((BMLSyncPointProgressFeedback)fb));
            }
            else if (fb instanceof BMLWarningFeedback)
            {
                warn((BMLWarningFeedback)fb);
            }
        }
        catch (IOException e)
        {
            appendWarning("Could not parse feedback " + feedback + "\n");
        }
    }
}
