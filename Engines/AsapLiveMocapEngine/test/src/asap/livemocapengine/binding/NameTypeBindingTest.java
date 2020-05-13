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
package asap.livemocapengine.binding;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

interface StubInterface{}

/**
 * Unit tests for the NameTypeBinding
 * @author welberge
 *
 */
public class NameTypeBindingTest
{
    NameTypeBinding ntb = new NameTypeBinding();
    
    @Test
    public void testGetAndPutOne()
    {
        class Stub{}
        Stub stub = new Stub();
        ntb.put("stub", Stub.class, stub);
        assertEquals(stub, ntb.get("stub", Stub.class));
    }
    
    @Test
    public void testGetAndPutTwoWithSameName()
    {
        class Stub1{}
        class Stub2{}
        Stub1 stub1 = new Stub1();
        Stub2 stub2 = new Stub2();
        ntb.put("stub", Stub1.class, stub1);
        ntb.put("stub", Stub2.class, stub2);        
        assertEquals(stub1, ntb.get("stub", Stub1.class));
        assertEquals(stub2, ntb.get("stub", Stub2.class));
    }
    
    @Test
    public void testGetAndPutWithInterface()
    {
        class Stub implements StubInterface{}
        Stub stub = new Stub();
        ntb.put("stub", StubInterface.class, stub);
        assertEquals(stub, ntb.get("stub", StubInterface.class));
    }
    
    @Test
    public void testGetAndPutWithName()
    {
        class Stub implements StubInterface{}
        Stub stub = new Stub();
        System.out.println(StubInterface.class.getName());
        ntb.put("stub", "asap.livemocapengine.binding.StubInterface", stub);
        assertEquals(stub, ntb.get("stub", StubInterface.class));
    }
}
