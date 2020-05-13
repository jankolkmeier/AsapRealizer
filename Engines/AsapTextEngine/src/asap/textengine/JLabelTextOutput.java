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
package asap.textengine;

import hmi.util.StringUtil;

import java.awt.Font;

import javax.swing.JLabel;

import asap.realizer.planunit.InvalidParameterException;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;

/**
 * TextOutput that prints the text to a JLabel. The volume parameter is linked to font size.
 * @author welberge
 *
 */
public class JLabelTextOutput implements TextOutput
{
    private JLabel label;
    private static final float FONT_PER_VOLUME_PERCENT = 0.25f;

    public JLabelTextOutput(JLabel l)
    {
        label = l;
    }

    @Override
    public void setText(String text)
    {
        label.setText(text);
        Font curFont = label.getFont();
        label.setFont(new Font(curFont.getFontName(), curFont.getStyle(), (int) (50 * FONT_PER_VOLUME_PERCENT)));
        // System.out.println("text: "+text);
    }

    @Override
    public void setFloatParameterValue(String parameter, float value) throws ParameterException
    {
        if (parameter.equals("volume"))
        {
            Font curFont = label.getFont();
            label.setFont(new Font(curFont.getFontName(), curFont.getStyle(), (int) (FONT_PER_VOLUME_PERCENT * value)));
        }
        else
        {
            throw new ParameterNotFoundException(parameter);
        }
    }

    @Override
    public void setParameterValue(String parameter, String value) throws ParameterException
    {
        if (StringUtil.isNumeric(value))
        {
            setFloatParameterValue(parameter, Float.parseFloat(value));
        }
        else
        {
            throw new InvalidParameterException(parameter,value);
        }
    }

    @Override
    public float getFloatParameterValue(String parameter) throws ParameterException
    {
        if (parameter.equals("volume"))
        {
            Font curFont = label.getFont();
            return curFont.getSize() / FONT_PER_VOLUME_PERCENT;
        }
        else
        {
            throw new ParameterNotFoundException(parameter);
        }
    }

    @Override
    public String getParameterValue(String parameter) throws ParameterException
    {
        return "" + getFloatParameterValue(parameter);
    }

}
