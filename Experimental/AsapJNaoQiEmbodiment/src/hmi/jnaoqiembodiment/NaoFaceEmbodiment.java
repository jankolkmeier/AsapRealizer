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
package hmi.jnaoqiembodiment;

import hmi.faceembodiments.AUConfig;
import hmi.faceembodiments.FACSFaceEmbodiment;
import hmi.faceembodiments.Side;

import com.aldebaran.proxy.ALLedsProxy;


/**
 * 
 * @author welberge
 */
public class NaoFaceEmbodiment implements FACSFaceEmbodiment
{
    private final ALLedsProxy ledsProxy;
    private final String id;
    
    @Override
    public String getId()
    {
        return id;
    }
    
    public NaoFaceEmbodiment(String id, ALLedsProxy ledsProxy)
    {
        this.id = id;
        this.ledsProxy = ledsProxy;
        disableAllEyeLeds();
    }

    private void disableAllEyeLeds(String color)
    {
        for (int angle = 0; angle < 360; angle += 45)
        {
            ledsProxy.setIntensity("Face/Led/" + color + "/Left/" + angle + "Deg/Actuator/Value", 0);
            ledsProxy.setIntensity("Face/Led/" + color + "/Right/" + angle + "Deg/Actuator/Value", 0);
        }
    }

    public void disableAllEyeLeds()
    {
        disableAllEyeLeds("Red");
        disableAllEyeLeds("Green");
        disableAllEyeLeds("Blue");
    }

    private void setFacs1(String side, float intensity)
    {
        ledsProxy.setIntensity("Face/Led/Red/"+side+"/0Deg/Actuator/Value", intensity);
        ledsProxy.setIntensity("Face/Led/Red/"+side+"/315Deg/Actuator/Value", intensity);
        ledsProxy.setIntensity("Face/Led/Red/"+side+"/45Deg/Actuator/Value", intensity);
        
    }
        
    private void setFacs1(Side side, float intensity)
    {
        switch (side)
        {
        case LEFT: 
            setFacs1("Left",intensity);
            break;
        case RIGHT:
            setFacs1("Right",intensity);
            break;
        default:
            setFacs1("Left",intensity);
            setFacs1("Right",intensity);
            break;
        }        
    }
    
    @Override
    public void setAUs(AUConfig... configs)
    {
        for (AUConfig config : configs)
        {
            if (config != null && config.getAu() == 1)
            {
                setFacs1(config.getSide(),config.getValue());
            }
        }
    }

    public void shutdown()
    {
        disableAllEyeLeds();
    }

    
}
