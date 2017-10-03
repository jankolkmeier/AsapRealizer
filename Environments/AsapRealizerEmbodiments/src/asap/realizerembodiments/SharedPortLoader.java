package asap.realizerembodiments;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableSet;

import asap.bml.ext.bmla.BMLABMLBehaviorAttributes;
import asap.realizer.AsapRealizer;
import asap.realizer.bridge.MultiThreadedElckerlycRealizerBridge;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.feedback.FeedbackManagerImpl;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.scheduler.BMLASchedulingHandler;
import asap.realizer.scheduler.BMLBlockManager;
import asap.realizer.scheduler.BMLScheduler;
import asap.realizer.scheduler.MultiAgentBMLScheduler;
import asap.realizer.scheduler.SortedSmartBodySchedulingStrategy;
import asap.realizerembodiments.impl.BMLParserAssembler;
import asap.realizerembodiments.impl.MultiAgentBMLSchedulerAssembler;
import asap.realizerport.RealizerPort;
import hmi.environmentbase.Environment;
import hmi.util.Clock;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;
import lombok.Getter;
import saiba.bml.core.BMLBehaviorAttributeExtension;
import saiba.bml.parser.BMLParser;

public class SharedPortLoader implements Environment {

	private String id;

    private XMLStructureAdapter adapter = new XMLStructureAdapter();
    private HashMap<String, String> attrMap = null;
	private XMLTokenizer tokenizer;    
	
	@Getter
    private final PegBoard pegBoard = new PegBoard();
	@Getter
	private Clock schedulingClock;
	@Getter
	private BMLParser parser;
	@Getter
	private BMLBlockManager bmlBlockManager;
	@Getter
	private FeedbackManager feedbackManager;
	@Getter
	private BMLScheduler bmlScheduler;
	@Getter
	private AsapRealizer asapRealizer;
	@Getter
	private RealizerPort realizerPort;
	@Getter
	private final String multiAgentId = "&lt;MULTI_AGENT&gt;";
	@Getter
    private Map<String, PipeLoader> pipeLoaders = new HashMap<>();
	
	public void load(String resources, String file, Clock sc) throws IOException {
		schedulingClock = sc;
		
		tokenizer = XMLTokenizer.forResource(resources, file);
        attrMap = tokenizer.getAttributes();
        for (Entry<String, String> kvp : attrMap.entrySet()) {
        	if (kvp.getKey().equals("id")) {
        		this.id = kvp.getValue();
        	}
        }
        
        tokenizer.takeSTag("SharedPortLoader");
        parser = readParserSection();
        bmlBlockManager = new BMLBlockManager();
        feedbackManager = new FeedbackManagerImpl(bmlBlockManager, multiAgentId);
        bmlScheduler = readSchedulerSection(bmlBlockManager, parser, feedbackManager);

        asapRealizer = new AsapRealizer(parser, feedbackManager, schedulingClock, bmlScheduler);
        realizerPort = new MultiThreadedElckerlycRealizerBridge(asapRealizer);

        while (!tokenizer.atETag("SharedPortLoader")) {
        	readBMLRealizerSubsection();
        }
        tokenizer.takeETag("SharedPortLoader");
        
	}
	
    protected BMLParser readParserSection() throws IOException {
        BMLParser bp = new BMLParser(new ImmutableSet.Builder<Class<? extends BMLBehaviorAttributeExtension>>().add(
                BMLABMLBehaviorAttributes.class).build());
        if (tokenizer.atSTag("BMLParser")) {
            BMLParserAssembler assembler = new BMLParserAssembler();
            assembler.readXML(tokenizer);
            bp = assembler.getBMLParser();
        }
        return bp;
    }
    
    protected MultiAgentBMLScheduler readSchedulerSection(BMLBlockManager bm, BMLParser p, FeedbackManager fm)
            throws IOException {
    	MultiAgentBMLScheduler scheduler;
        
        if (tokenizer.atSTag(MultiAgentBMLSchedulerAssembler.xmlTag())) {
            MultiAgentBMLSchedulerAssembler assembler = new MultiAgentBMLSchedulerAssembler(p, fm,
            		bm, schedulingClock, pegBoard);
            assembler.readXML(tokenizer);
            scheduler = assembler.getBMLScheduler();
        } else {
        	scheduler = new MultiAgentBMLScheduler(p, fm, schedulingClock, new BMLASchedulingHandler(
                    new SortedSmartBodySchedulingStrategy(pegBoard), pegBoard), bm, pegBoard);
        }
        
        return scheduler;
    }

    protected void readBMLRealizerSubsection() throws IOException {
        if (tokenizer.atSTag("PipeLoader")) {
            attrMap = tokenizer.getAttributes();
            String id = adapter.getRequiredAttribute("id", attrMap, tokenizer);
            String loaderClass = adapter.getRequiredAttribute("loader", attrMap, tokenizer);
            PipeLoader pipeloader = null;
            try {
                pipeloader = (PipeLoader) Class.forName(loaderClass).newInstance();
            } catch (InstantiationException e) {
                throw tokenizer.getXMLScanException("InstantiationException while starting PipeLoader " + loaderClass);
            } catch (IllegalAccessException e) {
                throw tokenizer.getXMLScanException("IllegalAccessException while starting PipeLoader " + loaderClass);
            } catch (ClassNotFoundException e) {
                throw tokenizer.getXMLScanException("ClassNotFoundException while starting PipeLoader " + loaderClass);
            } catch (ClassCastException e) {
                throw tokenizer.getXMLScanException("ClassCastException while starting PipeLoader " + loaderClass);
            }

            tokenizer.takeSTag("PipeLoader");
            pipeloader.readXML(tokenizer, id, multiAgentId, "", realizerPort, schedulingClock);
            tokenizer.takeETag("PipeLoader");
            realizerPort = pipeloader.getAdaptedRealizerPort();
            pipeLoaders.put(id, pipeloader);
        } else {
            throw tokenizer.getXMLScanException("Unknown tag in SharedPort content: "+tokenizer.getTagName());
        }
    }
    
	/* === Environment =========== */
	
	@Override
	public String getId() {
		return id;
	}
	
	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void requestShutdown() {
		// TODO: MA - SharedPortLoader
		
	}

	@Override
	public boolean isShutdown() {
		// TODO: MA - SharedPortLoader
		return false;
	}

}
