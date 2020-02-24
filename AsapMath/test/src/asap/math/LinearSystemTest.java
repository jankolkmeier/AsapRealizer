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
package asap.math;
import static org.junit.Assert.assertEquals;
import hmi.math.Vec3f;
import hmi.testutil.math.Vec3fTestUtil;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
/**
 * Unit test cases for LinearSystem
 * @author hvanwelbergen
 *
 */
public class LinearSystemTest
{
    private static final int ROWS = 5;
    private static final float PRECISION = 0.001f;
    
    private List<List<Float>> getA()
    {
        List<List<Float>> A = new ArrayList<>();
        
        int x = 1;
        for(int i=0;i<ROWS;i++)
        {
            List<Float> l = new ArrayList<>();
            A.add(l);
            for(int j=0;j<ROWS;j++)
            {
                if(i==j)
                {
                    l.add((float)x);
                }
                else
                {
                    l.add(0f);
                }
                x++;
            }
        }
        return A;
    }
    
    private List<float[]> getB()
    {
        List<float[]>b = new ArrayList<>();
        int x=0;
        for(int i=0;i<ROWS;i++)
        {
            b.add(Vec3f.getVec3f(x++,x++,x++));
        }
        return b;
    }
    
    private List<List<Float>> mul(List<List<Float>> M1, List<List<Float>> M2)
    {
        List<List<Float>> res = new ArrayList<>();
        for(int i=0;i<M1.size();i++)
        {
            List<Float> l = new ArrayList<>();
            res.add(l);
            for(int j=0;j<M2.get(i).size();j++)
            {
                l.add(0f);
            }
        }
        
        for(int i=0;i<M1.size();i++)
        {
            for(int j=0;j<M2.size();j++)
            {
                for(int k=0;k<M1.get(i).size();k++)
                {
                    res.get(i).set(j, res.get(i).get(j) + M1.get(i).get(k)*M2.get(k).get(j));                    
                }
            }
        }
        return res;
    }
    
    private List<float[]> mulVec(List<List<Float>> M1, List<float[]> M2)
    {
        List<float[]> res = new ArrayList<>();
        for(int i=0;i<M1.size();i++)
        {
            res.add(Vec3f.getVec3f(0,0,0));
        }
        
        for(int i=0;i<M1.size();i++)
        {
            for(int j=0;j<3;j++)
            {
                for(int k=0;k<M1.get(i).size();k++)
                {
                    res.get(i)[j] = res.get(i)[j] + M1.get(i).get(k) * M2.get(k)[j];                    
                }
            }
        }
        return res;
    }
    
    @Test
    public void test()
    {
        List<List<Float>> A = getA();
        List<float[]> b = getB();
        LinearSystem.solve(A,b);
        List<List<Float>> AOrig = getA();
        List<float[]> bOrig = getB();
        
        //AOrig * A = I => A=AOrig^-1
        List<List<Float>> N = mul(AOrig,A);
        for(int i=0;i<N.size();i++)
        {
            for(int j=0;j<N.get(0).size();j++)
            {
                if(i==j)assertEquals(1f,N.get(i).get(j),PRECISION);
                else assertEquals(0f,N.get(i).get(j),PRECISION);
            }
        }
        
        //AOrig*b = bOrig 
        List<float[]> Axb = mulVec(AOrig,b);
        int i=0;
        for(float[] v:Axb)
        {
            Vec3fTestUtil.assertVec3fEquals(bOrig.get(i),v, PRECISION);
            i++;            
        }           
    }
}
