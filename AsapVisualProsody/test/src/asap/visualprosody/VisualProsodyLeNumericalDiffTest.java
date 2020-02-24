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
package asap.visualprosody;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Before;
import org.junit.Test;

import asap.visualprosody.GaussianMixtureModel.GaussianMixture;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for VisualProsodyLe
 * @author herwinvw
 *
 */
public class VisualProsodyLeNumericalDiffTest
{
    private GaussianMixtureModel mockGmmVelocity = mock(GaussianMixtureModel.class);
    private GaussianMixtureModel mockGmmAcceleration = mock(GaussianMixtureModel.class);
    private static final double PRECISION = 0.001;

    private GaussianMixture gmRollVoiced = new GaussianMixture(1, new MultivariateNormalDistribution(new double[] { -2, 1, 2 },
            new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } }));
    private GaussianMixture gmPitchVoiced = new GaussianMixture(1, new MultivariateNormalDistribution(new double[] { 0, 1, 2 },
            new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } }));
    private GaussianMixture gmYawVoiced = new GaussianMixture(1, new MultivariateNormalDistribution(new double[] { 2, 1, 2 },
            new double[][] { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } }));

    private VisualProsodyLeNumericalDiff vp;

    @Before
    public void setup()
    {
        when(mockGmmVelocity.density(any(double[].class))).thenReturn(1d);
        when(mockGmmAcceleration.density(any(double[].class))).thenReturn(1d);
        vp = new VisualProsodyLeNumericalDiff(new GaussianMixtureModel(ImmutableList.of(gmRollVoiced)), new GaussianMixtureModel(
                ImmutableList.of(gmPitchVoiced)), new GaussianMixtureModel(ImmutableList.of(gmYawVoiced)), mockGmmVelocity,
                mockGmmAcceleration);
    }

    @Test
    public void testVoiced()
    {
        double res[] = vp.generateHeadPose(new double[] { 0, 3, 0 }, new double[] { 0, 0, 0 }, 1, 1, 1, 1);
        assertEquals(-2, res[0], PRECISION);
        assertEquals(0, res[1], PRECISION);
        assertEquals(2, res[2], PRECISION);
    }

    @Test
    public void testUnVoiced()
    {
        double res[] = vp.generateHeadPose(new double[] { 0, 3, 0 }, new double[] { 0, 0, 0 }, 0, 1, 1, 1);
        assertEquals(0, res[0], PRECISION);
        assertEquals(3, res[1], PRECISION);
        assertEquals(0, res[2], PRECISION);
    }
}
