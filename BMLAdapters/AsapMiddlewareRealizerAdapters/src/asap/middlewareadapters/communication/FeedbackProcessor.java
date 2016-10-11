package asap.middlewareadapters.communication;

import asap.realizerport.RealizerPort;
import asap.middlewareadapters.BMLRealizerToMiddlewareAdapter;
import nl.utwente.hmi.middleware.worker.AbstractWorker;

import java.net.URLEncoder;
import java.net.URLDecoder;

import com.fasterxml.jackson.databind.JsonNode;

public class FeedbackProcessor extends AbstractWorker {

	private BMLRealizerToMiddlewareAdapter rp;

	public FeedbackProcessor(BMLRealizerToMiddlewareAdapter rp){
		this.rp = rp;
	}
	
	//TODO: empty bottle might cause a problem here
	@Override
	public void processData(JsonNode jn) {
        try{
            String feedback = jn.get("feedback").get("content").asText();
            rp.sendFeedback(URLDecoder.decode(feedback,"UTF-8"));
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
	}

}
