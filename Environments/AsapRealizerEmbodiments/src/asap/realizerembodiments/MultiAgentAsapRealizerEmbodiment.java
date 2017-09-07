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
