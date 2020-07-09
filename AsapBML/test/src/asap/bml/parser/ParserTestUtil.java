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
package asap.bml.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import saiba.bml.parser.Constraint;
import saiba.bml.parser.SyncPoint;

/**
 * Test utilities for BMLParser testing
 * @author hvanwelbergen
 *
 */
public final class ParserTestUtil
{
    private ParserTestUtil(){}
    
    public static boolean syncMatch(ExpectedSync es, SyncPoint as)
    {
        if (es.bmlId != null)
        {
            if (!es.bmlId.equals(as.getBmlId()))
                return false;
        }

        if (es.name == null && as.getName() != null)
            return false;
        if (as.getName() == null && es.name != null)
            return false;
        if (as.getName() != null && es.name != null)
        {
            if (!es.name.equals(as.getName()))
                return false;
        }
        
        if (es.offset != as.offset)
            return false;
        else if (es.behaviorName == null && as.getBehaviourId() == null)
            return true;
        if (es.behaviorName == null)
            return false;
        if (as.getBehaviourId() == null)
            return false;
        if (!es.behaviorName.equals(as.getBehaviourId()))
            return false; // findbugs complains, but
                          // this is not a bug
        return true;
    }

    public static  boolean constraintMatch(ExpectedConstraint ec, Constraint ac)
    {
        if (ec.expectedSyncs.size() != ac.getTargets().size())
            return false;

        for (ExpectedSync es : ec.expectedSyncs)
        {
            boolean matches = false;
            for (SyncPoint as : ac.getTargets())
            {
                if (syncMatch(es, as))
                {
                    matches = true;
                    break;
                }
            }
            if (!matches)
                return false;
        }
        return true;
    }

    public static  void assertEqualConstraints(List<ExpectedConstraint> expected, List<Constraint> actual)
    {
        assertEquals("Size of actual constraint list: " + actual + " does not match size of expected constraint list " + expected, expected.size(),
                actual.size());
        for (Constraint ac : actual)
        {
            boolean matches = false;
            for (ExpectedConstraint ec : expected)
            {
                if (constraintMatch(ec, ac))
                {
                    matches = true;
                    break;
                }
            }
            assertTrue("Constraint " + ac + " is not matched by any of the expected constraints " + expected, matches);
        }
    }
}
