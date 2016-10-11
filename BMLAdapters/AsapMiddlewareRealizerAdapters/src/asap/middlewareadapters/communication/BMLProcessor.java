package asap.middlewareadapters.communication;

import asap.realizerport.RealizerPort;
import nl.utwente.hmi.middleware.stomp.STOMPMiddleware;
import nl.utwente.hmi.middleware.worker.AbstractWorker;

import java.net.URLEncoder;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;

public class BMLProcessor extends AbstractWorker {
	private static Logger logger = LoggerFactory.getLogger(BMLProcessor.class.getName());

	private RealizerPort rp;

	public BMLProcessor(RealizerPort rp){
		this.rp = rp;
	}
	
	@Override
	public void processData(JsonNode jn) {
        try{
            String bml = jn.path("bml").path("content").asText();
            if("".equals(bml)){
            	logger.error("Got malformed bml request through middleware, dropping behavior: {}",jn.toString());
            } else {
            	rp.performBML(URLDecoder.decode(bml,"UTF-8"));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
	}

}
