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

import hmi.headandgazeembodiments.GazeEmbodiment;
import hmi.math.Quat4f;
import hmi.math.Vec3f;

import com.aldebaran.proxy.DCMProxy;


public class NaoGazeEmbodiment implements GazeEmbodiment
{
    private final String id;
    private DCMProxy dcmProxy;
    private static final float[] FORWARD = { 0, 0, 0, 1 }; // default forward gaze direction

    @Override
    public String getId()
    {
        return id;
    }
    
    public NaoGazeEmbodiment(String id, DCMProxy dcmProxy)
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
    public void setGazePosition(float[] target)
    {
        float dir[] = new float[3];
        Vec3f.set(dir, target);
        Vec3f.normalize(dir);
        float q[] = Quat4f.getQuat4f();

        Quat4f.set(q, 0, -dir[0], -dir[1], -dir[2]);
        Quat4f.mul(q, FORWARD);
        float rpy[] = new float[3];
        Quat4f.getRollPitchYaw(q, rpy);
        System.out.println("Setting rpy: "+Vec3f.toString(rpy));
        NaoDCMUtils.setJointRotation("HeadYaw", rpy[2], 100, dcmProxy);
        NaoDCMUtils.setJointRotation("HeadPitch", rpy[1], 100, dcmProxy);
    }
    
    public void shutdown()
    {
        NaoDCMUtils.smoothlyResetStiffness("HeadYaw", dcmProxy);
        NaoDCMUtils.smoothlyResetStiffness("HeadPitch", dcmProxy);
    }

    @Override
    public void setGazeRollPitchYawDegrees(float roll, float pitch, float yaw)
    {
        NaoDCMUtils.setJointRotation("HeadYaw", yaw, 100, dcmProxy);
        NaoDCMUtils.setJointRotation("HeadPitch", pitch, 100, dcmProxy);
    }
}
