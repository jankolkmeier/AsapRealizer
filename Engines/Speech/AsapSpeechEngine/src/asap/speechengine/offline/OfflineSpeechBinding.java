package asap.speechengine.offline;

import asap.speechengine.ttsbinding.TTSBinding;
import hmi.tts.BMLTTSBridge;

/**
 * Offline Speech is pre recorded speech (i.e. in a wav file) and (optionally) has offline generated phoneme/viseme timings.
 * This is part of the speech engine, and is being used when an <offline> type speech description is provided.
 * 
 * Note that this binding is currently not used, but we inject the OfflineSpeechBehaviour from the MaryTTS bindings (TODO find a better solution)
 * @author jan
 */
public class OfflineSpeechBinding extends TTSBinding {
	
	private final OfflineSpeechGenerator offlineSpeechGenerator;
	
	public OfflineSpeechBinding() {
		offlineSpeechGenerator = new OfflineSpeechGenerator(null);
        ttsGenerator = offlineSpeechGenerator;
        ttsBridgeMap.put(OfflineSpeechBehaviour.class,  new BMLTTSBridge(offlineSpeechGenerator));
        supportedBehaviours.add(OfflineSpeechBehaviour.class);
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
	}
	
}
