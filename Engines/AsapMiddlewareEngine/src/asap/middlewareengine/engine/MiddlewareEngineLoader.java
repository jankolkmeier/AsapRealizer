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
package asap.middlewareengine.engine;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.util.Resources;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

import java.io.IOException;
import java.util.HashMap;

import asap.middlewareengine.MiddlewarePlanner;
import asap.middlewareengine.embodiment.MiddlewareEmbodiment;
import asap.middlewareengine.middlewarebinding.MiddlewareBinding;
import asap.middlewareengine.planunit.TimedMiddlewareUnit;
import asap.realizer.DefaultEngine;
import asap.realizer.DefaultPlayer;
import asap.realizer.Engine;
import asap.realizer.Player;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.PlanPlayer;
import asap.realizer.planunit.SingleThreadedPlanPlayer;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.EngineLoader;

/**

*/
public class MiddlewareEngineLoader implements EngineLoader
{

    private XMLStructureAdapter adapter = new XMLStructureAdapter();
    private MiddlewareEmbodiment mwe = null;

    private Engine engine = null;
    private PlanManager<TimedMiddlewareUnit> planManager = null;
    private Player player = null;
    private String id = "";
    private String characterId = "";
    // some variables cached during loading
    private MiddlewareBinding middlewareBinding = null;
    private AsapRealizerEmbodiment are = null;

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        id = loaderId;
        characterId = vhId;
        for (EmbodimentLoader e : ArrayUtils.getClassesOfType(requiredLoaders, EmbodimentLoader.class))
        {
            if (e.getEmbodiment() instanceof MiddlewareEmbodiment)
            {
                mwe = (MiddlewareEmbodiment) e.getEmbodiment();
            }
            if (e.getEmbodiment() instanceof AsapRealizerEmbodiment)
            {
                are = (AsapRealizerEmbodiment) e.getEmbodiment();
            }
        }
        if (mwe == null)
        {
            throw new RuntimeException("MiddlewareEngineLoader requires an EmbodimentLoader containing a MiddlewareEmbodiment");
        }
        if (are == null)
        {
            throw new RuntimeException("MiddlewareEngineLoader requires an EmbodimentLoader containing a AsapRealizerEmbodiment");
        }
        while (!tokenizer.atETag("Loader"))
        {
            readSection(tokenizer);
        }
        constructEngine(tokenizer);
    }

    @Override
    public void unload()
    {
        // engine.shutdown(); already done by realizer
    }

    protected void readSection(XMLTokenizer tokenizer) throws IOException
    {
        HashMap<String, String> attrMap = null;
        if (tokenizer.atSTag("MiddlewareBinding"))
        {
            attrMap = tokenizer.getAttributes();
            middlewareBinding = new MiddlewareBinding(mwe.getEmbodiment());
            try
            {
                middlewareBinding.readXML(new Resources(adapter.getOptionalAttribute("resources", attrMap, "")).getReader(adapter
                        .getRequiredAttribute("filename", attrMap, tokenizer)));
            }
            catch (Exception e)
            {
                e.printStackTrace();
                throw new RuntimeException("Cannnot load MiddlewareBinding: " + e);
            }
            tokenizer.takeEmptyElement("MiddlewareBinding");
        }
        else
        {
            throw tokenizer.getXMLScanException("Unknown tag in Loader content");
        }
    }

    private void constructEngine(XMLTokenizer tokenizer)
    {
        if (middlewareBinding == null) throw tokenizer.getXMLScanException("middlewareBinding is null, cannot build middlewareplanner ");
        planManager = new PlanManager<TimedMiddlewareUnit>();
        PlanPlayer planPlayer = new SingleThreadedPlanPlayer<TimedMiddlewareUnit>(are.getFeedbackManager(), planManager);
        player = new DefaultPlayer(planPlayer);
        MiddlewarePlanner planner = new MiddlewarePlanner(are.getFeedbackManager(), middlewareBinding, planManager);
        engine = new DefaultEngine<TimedMiddlewareUnit>(planner, player, planManager);
        engine.setId(id);
        engine.setCharacterId(characterId);

        // add engine to realizer;
        are.addEngine(engine);

    }

    /** Return the Engine that was constructed from the XML specification */
    public Engine getEngine()
    {
        return engine;
    }
    public MiddlewareEmbodiment getMiddlewareEmbodiment() 
    {
    	return mwe;
    }
    public PlanManager<TimedMiddlewareUnit> getPlanManager()
    {
        return planManager;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String newId)
    {
        id = newId;
    }
}
