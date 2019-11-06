/*******************************************************************************
 *******************************************************************************/
package asap.marytts5binding;

import hmi.tts.BMLTTSBridge;
import hmi.tts.mary5.MaryAllophonesTTSBridge;
import hmi.tts.mary5.MarySSMLTTSBridge;
import hmi.tts.mary5.MaryTTSGenerator;
import hmi.tts.mary5.MaryWordsTTSBridge;
import hmi.tts.mary5.MaryXMLTTSBridge;
import hmi.tts.util.PhonemeToVisemeMapping;
import saiba.bml.core.SpeechBehaviour;
import asap.bml.ext.maryxml.MaryAllophonesBehaviour;
import asap.bml.ext.maryxml.MaryWordsBehaviour;
import asap.bml.ext.maryxml.MaryXMLBehaviour;
import asap.bml.ext.ssml.SSMLBehaviour;
import asap.speechengine.offline.OfflineSpeechBehaviour;
import asap.speechengine.offline.OfflineSpeechGenerator;
import asap.speechengine.offline.OfflineSpeechMLBridge;
import asap.speechengine.ttsbinding.TTSBinding;

/**
 * Binds SpeechBehaviour, SSMLBehaviour, MaryXMLBehaviour, MaryWordsBehaviour and MaryAllophonesBehaviour
 * to the MaryTTS speech generation
 * @author welberge
 *
 */
public class MaryTTSBinding extends TTSBinding
{
    private MaryTTSGenerator maryTTSGenerator;
    private final OfflineSpeechGenerator offlineSpeechGenerator;

    public MaryTTSBinding(PhonemeToVisemeMapping ptv)
    {
        try
        {
            maryTTSGenerator = new MaryTTSGenerator(ptv);
            offlineSpeechGenerator = new OfflineSpeechGenerator(ptv);
            ttsGenerator = maryTTSGenerator;
        } 
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }        

        ttsBridgeMap.put(SpeechBehaviour.class, new BMLTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(SSMLBehaviour.class, new MarySSMLTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(MaryXMLBehaviour.class, new MaryXMLTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(MaryWordsBehaviour.class, new MaryWordsTTSBridge(maryTTSGenerator));
        ttsBridgeMap.put(MaryAllophonesBehaviour.class, new MaryAllophonesTTSBridge(maryTTSGenerator));
        
        ttsBridgeMap.put(OfflineSpeechBehaviour.class,  new OfflineSpeechMLBridge(offlineSpeechGenerator));

        supportedBehaviours.add(SSMLBehaviour.class);
        supportedBehaviours.add(MaryXMLBehaviour.class);
        supportedBehaviours.add(MaryWordsBehaviour.class);
        supportedBehaviours.add(MaryAllophonesBehaviour.class);

        supportedBehaviours.add(OfflineSpeechBehaviour.class);
    }
    @Override
    public void cleanup()
    {
            
    }
}
