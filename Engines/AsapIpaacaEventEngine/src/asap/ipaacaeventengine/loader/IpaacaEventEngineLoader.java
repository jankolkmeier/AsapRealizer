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
package asap.ipaacaeventengine.loader;

import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

import java.io.IOException;

import lombok.Getter;
import asap.ipaacaeventengine.IpaacaEventPlanner;
import asap.ipaacaeventengine.MessageManager;
import asap.ipaacaeventengine.TimedIpaacaMessageUnit;
import asap.realizer.DefaultEngine;
import asap.realizer.DefaultPlayer;
import asap.realizer.Engine;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.SingleThreadedPlanPlayer;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.EngineLoader;

/**
 * Loads the IpaacaEventEngine
 * @author hvanwelbergen
 *
 */
public class IpaacaEventEngineLoader implements EngineLoader
{
    @Getter
    private String id;
    private Engine engine = null;
    private MessageManager messageManager;
    static
    {
        ipaaca.Initializer.initializeIpaacaRsb();
    }
    
    public IpaacaEventEngineLoader()
    {

    }
    
    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.id = loaderId;
        messageManager = new MessageManager(id);
        AsapRealizerEmbodiment are = ArrayUtils.getFirstClassOfType(requiredLoaders, AsapRealizerEmbodiment.class);
        if (are == null)
        {
            throw new RuntimeException("IpaacaEventEngineLoader requires an EmbodimentLoader containing a AsapRealizerEmbodiment");
        }
        
        PlanManager<TimedIpaacaMessageUnit> planManager = new PlanManager<>();
        SingleThreadedPlanPlayer<TimedIpaacaMessageUnit> pp = new SingleThreadedPlanPlayer<>(are.getFeedbackManager(), planManager);
        IpaacaEventPlanner planner = new IpaacaEventPlanner(are.getFeedbackManager(), planManager, messageManager);
        engine = new DefaultEngine<TimedIpaacaMessageUnit>(planner,new DefaultPlayer(pp), planManager);
        engine.setId(id);
        engine.setCharacterId(vhId);
        
        are.addEngine(engine);
    }
    
    @Override
    public Engine getEngine()
    {
        return engine;
    }    

    @Override
    public void unload()
    {
        messageManager.close();
    }
}
