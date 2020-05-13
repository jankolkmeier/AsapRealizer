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
package asap.bml.ext.maryxml;

import static org.junit.Assert.assertEquals;
import hmi.testutil.LabelledParameterized;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

/**
 * Unit test cases for the MaryXMLBaseBehaviour
 * @author welberge
 * 
 */
@RunWith(LabelledParameterized.class)
public class MaryXMLBaseBehaviourTest
{
    private MaryXMLBaseBehaviour beh;
    private MaryXMLBehaviorFactory factory;

    private static final class MaryXMLBehaviorFactory
    {
        private final Class<? extends MaryXMLBaseBehaviour> behClass;

        public MaryXMLBehaviorFactory(Class<? extends MaryXMLBaseBehaviour> behClass)
        {
            this.behClass = behClass;
        }

        public MaryXMLBaseBehaviour createBehavior(String bmlId, String id, String bml) throws IOException, InstantiationException,
                IllegalAccessException, InvocationTargetException, NoSuchMethodException
        {
            return behClass.getConstructor(String.class, String.class, XMLTokenizer.class).newInstance(bmlId, id, new XMLTokenizer(bml));
        }

        public MaryXMLBaseBehaviour createBehavior(String bmlId, String bml) throws IOException, InstantiationException,
                IllegalAccessException, InvocationTargetException, NoSuchMethodException
        {
            return behClass.getConstructor(String.class, XMLTokenizer.class).newInstance(bmlId, new XMLTokenizer(bml));
        }
    }

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[] { "MaryXMLBaseBehaviour", new MaryXMLBehaviorFactory(MaryXMLBaseBehaviour.class) }, new Object[] {
                "MaryXMLBehaviour", new MaryXMLBehaviorFactory(MaryXMLBehaviour.class) }, new Object[] { "MaryAllophonesBehaviour",
                new MaryXMLBehaviorFactory(MaryAllophonesBehaviour.class) }, new Object[] { "MaryWordsBehaviour",
                new MaryXMLBehaviorFactory(MaryWordsBehaviour.class) });
    }

    public MaryXMLBaseBehaviourTest(String label, MaryXMLBehaviorFactory factory)
    {
        this.factory = factory;
    }

    @Before
    public void setup() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException
    {
        String str = "<maryxml xmlns=\"" + MaryXMLBaseBehaviour.NAMESPACE + "\" >" + "Hello world!" + "</maryxml>";
        beh = factory.createBehavior("bml1", "beh1", str);
    }

    @Test
    public void testReadXML() throws IOException
    {
        assertEquals("Hello world!", beh.getContent());
    }

    @Test
    public void testWriteXML() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException
    {
        StringBuilder buf = new StringBuilder();
        beh.appendXML(buf);
        MaryXMLBaseBehaviour behOut = factory.createBehavior("bml1", buf.toString());
        assertEquals("Hello world!", behOut.getContent().trim());
        assertEquals("bml1", behOut.getBmlId());
        assertEquals("beh1", behOut.id);
    }
}
