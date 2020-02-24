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

import hmi.headandgazeembodiments.EulerHeadEmbodiment;

import com.aldebaran.proxy.DCMProxy;


/**
 * Steers the Nao Head using dcm 
 * @author welberge
 */
public class NaoHeadEmbodiment implements EulerHeadEmbodiment
{
    private final String id;
    private DCMProxy dcmProxy;
    
    public NaoHeadEmbodiment(String id, DCMProxy dcmProxy)
    {
        this.dcmProxy = dcmProxy;        
        this.id = id;
        NaoDCMUtils.smoothlySetStiffness("HeadYaw", dcmProxy);
        NaoDCMUtils.smoothlySetStiffness("HeadPitch", dcmProxy);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            Thread.interrupted();
        }
    }
    
    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void setHeadRollPitchYawDegrees(float roll, float pitch, float yaw)
    {
        NaoDCMUtils.setJointRotation("HeadYaw", (float)Math.toRadians(yaw), dcmProxy);
        NaoDCMUtils.setJointRotation("HeadPitch", (float)Math.toRadians(pitch), dcmProxy);
    }
    
    public void shutdown()
    {
        NaoDCMUtils.smoothlyResetStiffness("HeadYaw", dcmProxy);
        NaoDCMUtils.smoothlyResetStiffness("HeadPitch", dcmProxy);
    }

    @Override
    public void claimHeadResource()
    {
                
    }

    @Override
    public void releaseHeadResource()
    {
                
    }
}
