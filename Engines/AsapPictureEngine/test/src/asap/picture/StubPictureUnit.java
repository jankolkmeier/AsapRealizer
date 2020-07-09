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
package asap.picture;

import lombok.Delegate;
import asap.picture.display.PictureDisplay;
import asap.picture.planunit.PUPlayException;
import asap.picture.planunit.PUPrepareException;
import asap.picture.planunit.PictureUnit;
import asap.picture.planunit.TimedPictureUnit;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;

/**
 * Testing stub for the StubPictureUnit
 * @author hvanwelbergen
 * 
 */
public class StubPictureUnit implements PictureUnit
{
    @Delegate
    private final KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();

    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterException
    {

    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {

    }

    @Override
    public String getParameterValue(String name) throws ParameterException
    {
        return null;
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException
    {
        return 0;
    }

    @Override
    public boolean hasValidParameters()
    {
        return true;
    }

    @Override
    public void prepareImages() throws PUPrepareException
    {

    }

    @Override
    public void startUnit(double time) throws PUPlayException
    {

    }

    @Override
    public void play(double t) throws PUPlayException
    {

    }

    @Override
    public void cleanup()
    {

    }

    @Override
    public TimedPictureUnit createTPU(FeedbackManager bfm, BMLBlockPeg bbPeg, String bmlId, String id)
    {
        return new TimedPictureUnit(bfm, bbPeg, bmlId, id, this);
    }

    @Override
    public double getPreferedDuration()
    {
        return 3;
    }

    @Override
    public PictureUnit copy(PictureDisplay display)
    {
        return this;
    }
}
