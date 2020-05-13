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
package asap.animationengine.ace;

import hmi.math.Vec3f;
import lombok.Getter;

/**
 * Orientation constraint for wrist rotation
 * @author hvanwelbergen
 *
 */
public class OrientConstraint
{
    private float [] d = Vec3f.getVec3f();  //extended finger direction:
    private float [] p = Vec3f.getVec3f();  //palm normal
    
    public float[] getD()
    {
        return Vec3f.getVec3f(d);
    }
    
    public float[] getP()
    {
        return Vec3f.getVec3f(p);
    }
    
    @Getter private final GStrokePhaseID phase;
    @Getter private final String id;
    
    public OrientConstraint(String id, GStrokePhaseID ph,
            float[] d_dir, float[] p_dir)
    {
        this.id = id;
        phase = ph;
        Vec3f.set(d,d_dir);
        Vec3f.set(p,p_dir);        
    }
    
    public OrientConstraint(String id, GStrokePhaseID ph)
    {        
        this(id, ph,Vec3f.getVec3f(0,0,0), Vec3f.getVec3f(0,0,0));
    }
    
    public void setP(float[]pNew)
    {
        Vec3f.set(p,pNew);
    }
    
    public void setD(float[]dNew)
    {
        Vec3f.set(d,dNew);
    }
    
    
}
