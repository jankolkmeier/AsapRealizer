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
package asap.realizerembodiments;

import java.io.IOException;
import java.util.Collection;

import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLTokenizer;

/**
 * 
 * @author Jan Kolkmeier
 */
public class MultiAgentAsapRealizerEmbodiment extends AsapRealizerEmbodiment {

	private SharedPortLoader sharedPortEnvironment;
	
	@Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        this.loaderId = loaderId;
        this.name = vhName;
        this.vhId = vhId;
        
        String sharedPortId = null;
        
        while (!tokenizer.atETag("Loader")) {
        	
        	if (tokenizer.atSTag("SharedPort")) {
                sharedPortId = tokenizer.getAttribute("id");
                tokenizer.takeSTag("SharedPort");
                tokenizer.takeETag("SharedPort");
        	}
        	
        }
        
        SharedPortLoader spl = null;
        for (Loader l : requiredLoaders) {
            if ((l instanceof EmbodimentLoader) && (((EmbodimentLoader) l).getEmbodiment() instanceof SchedulingClockEmbodiment)) theSchedulingClock = ((SchedulingClockEmbodiment) ((EmbodimentLoader) l)
                    .getEmbodiment()).getSchedulingClock();
        }

        for (Environment env : environments) {
            if (env instanceof SharedPortLoader && ((SharedPortLoader) env).getId().equals(sharedPortId)) {
            	spl = (SharedPortLoader) env;
            }
        }
        
        if (theSchedulingClock == null) {
            throw new RuntimeException("MultiAgentAsapRealizerEmbodiment requires an SchedulingClockEmbodiment with id=\""+sharedPortId+"\" when loading");
        }
        
        if (spl == null) {
        	throw new RuntimeException("MultiAgentAsapRealizerEmbodiment requires an SharedPortLoader when loading");
        }

        sharedPortEnvironment = spl;
        elckerlycRealizer = sharedPortEnvironment.getAsapRealizer();
        realizerPort = sharedPortEnvironment.getRealizerPort();

        bmlScheduler = spl.getBmlScheduler();
        bmlBlockManager = spl.getBmlBlockManager();
        feedbackManager = spl.getFeedbackManager();
        pegBoard = spl.getPegBoard();
    }

    @Override
    public PipeLoader getPipeLoader(String id) {
        return sharedPortEnvironment.getPipeLoaders().get(id);
    }

    @Override
    public Collection<PipeLoader> getPipeLoaders() {
        return sharedPortEnvironment.getPipeLoaders().values();
    }
    
    @Override
    public void unload() {
        for (PipeLoader pipeLoader : sharedPortEnvironment.getPipeLoaders().values()) {
            pipeLoader.shutdown();
        }
        sharedPortEnvironment.getAsapRealizer().shutdown();
    }
}
