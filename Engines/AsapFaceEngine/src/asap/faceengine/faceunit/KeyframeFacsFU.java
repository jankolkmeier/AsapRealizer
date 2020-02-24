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
package asap.faceengine.faceunit;

import hmi.faceanimation.FaceController;
import hmi.faceanimation.FaceInterpolator;
import hmi.faceanimation.converters.EmotionConverter;
import hmi.faceanimation.converters.FACS2MorphConverter;
import hmi.faceanimation.converters.FACSConverter;
import hmi.faceanimation.model.FACS.Side;
import hmi.faceanimation.model.FACSConfiguration;
import hmi.faceanimation.model.MPEG4Configuration;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import lombok.Data;
import asap.motionunit.MUPlayException;

/**
 * MotionUnit for AU keyframe animation
 * @author herwinvw
 *
 */
public class KeyframeFacsFU extends KeyframeFaceUnit
{
    private FACSConverter facsConverter;
    private final List<AU> aus;
    
    enum AUSide
    {
        LEFT, RIGHT, BOTH;
    }
    
    @Data
    private class AU
    {
        final int number;
        final AUSide side;
    }

    public KeyframeFacsFU(FaceInterpolator mi)
    {
        super(mi);
        List<String> parts = mi.getParts();
        List<AU> auList = new ArrayList<AU>();

        for (String part : parts)
        {
            String split[] = part.split("\\.");
            auList.add(new AU(Integer.parseInt(split[0]), AUSide.valueOf(split[1])));
        }
        aus = ImmutableList.copyOf(auList);
    }

    @Override
    public KeyframeFacsFU copy(FaceController fc, FACSConverter fconv, EmotionConverter econv, FACS2MorphConverter f2mconv)
    {
        KeyframeFacsFU copy = new KeyframeFacsFU(mi);
        setupCopy(copy, fc);
        copy.facsConverter = fconv;
        return copy;
    }

    @Override
    public void play(double t) throws MUPlayException
    {
        MPEG4Configuration mpeg4Config = new MPEG4Configuration();
        FACSConfiguration facsConfig = new FACSConfiguration();
        float current[] = getInterpolatedValue(t);
        int i=0;
        for (AU au:aus)
        {
            if(au.side.equals(AUSide.BOTH))
            {
                System.out.println("Setting facs "+au.getNumber()+" to "+current[i]);
                facsConfig.setValue(Side.LEFT, au.getNumber(), current[i]);
                facsConfig.setValue(Side.RIGHT, au.getNumber(), current[i]);
            }
            else
            {
                facsConfig.setValue(Side.valueOf(au.getSide().toString()), au.getNumber(), current[i]);
            }
            i++;
        }
        facsConverter.convert(facsConfig, mpeg4Config);        
        faceController.addMPEG4Configuration(mpeg4Config);
    }

}
