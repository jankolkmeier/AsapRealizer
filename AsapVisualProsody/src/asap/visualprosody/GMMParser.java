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

import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

import asap.visualprosody.GaussianMixtureModel.GaussianMixture;
import lombok.Getter;

/**
 * Parses a GMM XML specification
 * @author herwinvw
 *
 */
public class GMMParser extends XMLStructureAdapter
{
    @Getter
    private int k = 0;
    @Getter
    private List<Double> lambdas;
    @Getter
    private List<double[]> mus;
    @Getter
    private List<double[][]> sigmas;

    private static final class Lambda extends XMLStructureAdapter
    {
        private static final String XMLTAG = "lambda";
        @Getter
        private double lambda;

        @Override
        public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            lambda = getRequiredDoubleAttribute("val", attrMap, tokenizer);
        }

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    private static final class Lambdas extends XMLStructureAdapter
    {
        @Getter
        private List<Double> lambdas = new ArrayList<Double>();

        @Override
        public void decodeContent(XMLTokenizer tokenizer) throws IOException
        {
            while (tokenizer.atSTag())
            {
                String tag = tokenizer.getTagName();
                switch (tag)
                {
                case Lambda.XMLTAG:
                    Lambda l = new Lambda();
                    l.readXML(tokenizer);
                    lambdas.add(l.getLambda());
                    break;
                default:
                    throw new XMLScanException("Unknown tag " + tag + " in <lambdas>");
                }
            }
        }

        private static final String XMLTAG = "lambdas";

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    private static final class Mu extends XMLStructureAdapter
    {
        private static final String XMLTAG = "mu";

        @Getter
        private double[] mu;

        @Override
        public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            mu = decodeDoubleArray(getRequiredAttribute("val", attrMap, tokenizer));
        }

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    private static final class Mus extends XMLStructureAdapter
    {
        @Getter
        private List<double[]> mus = new ArrayList<double[]>();

        @Override
        public void decodeContent(XMLTokenizer tokenizer) throws IOException
        {
            while (tokenizer.atSTag())
            {
                String tag = tokenizer.getTagName();
                switch (tag)
                {
                case Mu.XMLTAG:
                    Mu mu = new Mu();
                    mu.readXML(tokenizer);
                    mus.add(mu.getMu());
                    break;
                default:
                    throw new XMLScanException("Unknown tag " + tag + " in <mus>");
                }
            }
        }

        private static final String XMLTAG = "mus";

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    private static final class Sigma extends XMLStructureAdapter
    {
        
        @Getter
        private double[][] sigma;

        @Override
        public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
        {
            double sigmaArray[] = decodeDoubleArray(getRequiredAttribute("val", attrMap, tokenizer));
            int n = (int) Math.sqrt(sigmaArray.length);
            sigma = new double[n][];
            for (int i = 0; i < n; i++)
            {
                sigma[i] = new double[n];
                for (int j = 0; j < n; j++)
                {
                    sigma[i][j] = sigmaArray[i * n + j];
                }
            }
        }

        private static final String XMLTAG = "sigma";

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    private static final class Sigmas extends XMLStructureAdapter
    {
        @Getter
        private List<double[][]> sigmas=new ArrayList<double[][]>();

        @Override
        public void decodeContent(XMLTokenizer tokenizer) throws IOException
        {
            while (tokenizer.atSTag())
            {
                String tag = tokenizer.getTagName();
                switch (tag)
                {
                case Sigma.XMLTAG:
                    Sigma sigma = new Sigma();
                    sigma.readXML(tokenizer);
                    sigmas.add(sigma.getSigma());
                    break;
                default:
                    throw new XMLScanException("Unknown tag " + tag + " in <sigmas>");
                }
            }
        }

        private static final String XMLTAG = "sigmas";

        public static String xmlTag()
        {
            return XMLTAG;
        }

        @Override
        public String getXMLTag()
        {
            return XMLTAG;
        }
    }

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        k = getRequiredIntAttribute("k", attrMap, tokenizer);
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            switch (tag)
            {
            case Lambdas.XMLTAG:
                Lambdas l = new Lambdas();
                l.readXML(tokenizer);
                lambdas = l.getLambdas();
                break;
            case Mus.XMLTAG:
                Mus m = new Mus();
                m.readXML(tokenizer);
                mus = m.getMus();
                break;
            case Sigmas.XMLTAG:
                Sigmas s = new Sigmas();
                s.readXML(tokenizer);
                sigmas = s.getSigmas();
                break;
            default:
                throw new XMLScanException("Unknown tag " + tag + " in <gmm>");
            }
        }
    }

    public GaussianMixtureModel constructMixtureModel()
    {
        List<GaussianMixture> mixtures = new ArrayList<GaussianMixture>();
        for(int i=0;i<getK();i++)
        {
            MultivariateNormalDistribution mvnd = new MultivariateNormalDistribution(getMus().get(i),getSigmas().get(i));
            GaussianMixture mixture= new GaussianMixture(getLambdas().get(i),mvnd);
            mixtures.add(mixture);
        }
        return new GaussianMixtureModel(mixtures);
    }

    private static final String XMLTAG = "gmm";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
