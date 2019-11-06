package asap.speechengine.offline;

import java.io.IOException;

import hmi.tts.AbstractTTSGenerator;
import hmi.tts.TTSBridge;
import hmi.tts.TTSException;
import hmi.tts.TTSTiming;

/*
 * TODO: not sure what we would use this for...
 */
public class OfflineSpeechMLBridge implements TTSBridge {

	private final AbstractTTSGenerator ttsGenerator;
	
	public OfflineSpeechMLBridge(AbstractTTSGenerator ttsGen) {
        ttsGenerator = ttsGen;
	}
	
	@Override
	public TTSTiming speak(String text) throws TTSException {
        return ttsGenerator.speakBML(text);
	}

	@Override
	public TTSTiming speakToFile(String text, String filename) throws IOException, TTSException {
        return ttsGenerator.speakBMLToFile(text, filename);
	}

	@Override
	public TTSTiming getTiming(String text) throws TTSException {
        return ttsGenerator.getBMLTiming(text);
	}

}
