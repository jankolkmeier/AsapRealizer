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
package asap.animationengine.gaze;

import static org.junit.Assert.assertThat;
import hmi.animation.Hanim;
import hmi.testutil.animation.HanimBody;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

/**
 * Unit tests for GazeUtils
 * @author hvanwelbergen
 * 
 */
public class GazeUtilsTest
{
    @Test
    public void testGetJointsEyes()
    {
        assertThat(GazeUtils.getJoints(HanimBody.getLOA2HanimBodyWithEyes(), GazeInfluence.EYES),
                IsIterableContainingInAnyOrder.containsInAnyOrder(Hanim.l_eyeball_joint, Hanim.r_eyeball_joint));
    }

    @Test
    public void testGetJointsShoulder()
    {
        assertThat(GazeUtils.getJoints(HanimBody.getLOA2HanimBodyWithEyes(), GazeInfluence.SHOULDER),
                IsIterableContainingInAnyOrder.containsInAnyOrder(Hanim.vc2, Hanim.vt6, Hanim.vt1, Hanim.vt10, Hanim.vc4, Hanim.skullbase,
                        Hanim.l_eyeball_joint, Hanim.r_eyeball_joint));
    }
}
