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
package asap.naoengine.loader;

import java.io.IOException;

import asap.naoengine.NaoPlanner;
import asap.naoengine.naounits.AbstractNaoUnit;
import asap.realizer.DefaultEngine;
import asap.realizer.DefaultPlayer;
import asap.realizer.Engine;
import asap.realizer.planunit.PlanManager;
import asap.realizer.planunit.SingleThreadedPlanPlayer;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.EngineLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.xml.XMLTokenizer;

public class NaoEngineLoader implements EngineLoader {

	private String id;
	private Engine engine = null;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
			Loader... requiredLoaders) throws IOException {
		
		this.id = loaderId;
		AsapRealizerEmbodiment are = ArrayUtils.getFirstClassOfType(requiredLoaders, AsapRealizerEmbodiment.class);
		
		if (are == null) {
			throw new RuntimeException("NaoEngineLoader requires an EmbodimentLoader containing a AsapRealizerEmbodiment.");
		}
		
		PlanManager<AbstractNaoUnit> planManager = new PlanManager<>();
		SingleThreadedPlanPlayer<AbstractNaoUnit> player = new SingleThreadedPlanPlayer<>(are.getFeedbackManager(), planManager);
		NaoPlanner planner = new NaoPlanner(are.getFeedbackManager(), planManager);
		engine = new DefaultEngine<>(planner, new DefaultPlayer(player), planManager);
		engine.setId(id);
		are.addEngine(engine);
		
	}

	@Override
	public void unload() {
		// TODO Auto-generated method stub
	}

	@Override
	public Engine getEngine() {
		// TODO Auto-generated method stub
		return engine;
	}

}
