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

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.junit.Test;

import asap.visualprosody.GaussianMixtureModel.GaussianMixture;

import com.google.common.collect.ImmutableList;



/**
 * Unit tests for the GaussianMixture
 * @author herwinvw
 *
 */
public class GaussianMixtureModelTest
{
    private static final double PRECISION = 0.00001d;

    @Test
    public void testOneGaussianOneDimension()
    {
        GaussianMixture gm = new GaussianMixture(1, new MultivariateNormalDistribution(new double[] { 0 }, new double[][] { { 1 } }));
        GaussianMixtureModel gmm = new GaussianMixtureModel(ImmutableList.of(gm));
        assertEquals(1 / Math.sqrt(2 * Math.PI), gmm.density(new double[] { 0 }), PRECISION);
    }

    @Test
    public void testOneGaussianTwoDimensions()
    {
        GaussianMixture gm = new GaussianMixture(1, new MultivariateNormalDistribution(new double[] { 0, 1 }, new double[][] { { 1, 0 },
                { 0, 1 } }));
        GaussianMixtureModel gmm = new GaussianMixtureModel(ImmutableList.of(gm));
        assertEquals(1 / (2 * Math.PI), gmm.density(new double[] { 0, 1 }), PRECISION);
    }
    
    @Test
    public void testTwoGaussiansOneDimension()
    {
        GaussianMixture gm1 = new GaussianMixture(0.7,new MultivariateNormalDistribution(new double[] { 10 }, new double[][] { { 1 }}));
        GaussianMixture gm2 = new GaussianMixture(0.3,new MultivariateNormalDistribution(new double[] { -10 }, new double[][] { { 1 }}));
        GaussianMixtureModel gmm = new GaussianMixtureModel(ImmutableList.of(gm1,gm2));
        assertEquals(0.7 / Math.sqrt(2 * Math.PI),gmm.density(new double[]{10}),PRECISION);
        assertEquals(0.3 / Math.sqrt(2 * Math.PI),gmm.density(new double[]{-10}),PRECISION);
    }
}
