package asap.speechengine.offline;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hmi.tts.AbstractTTSGenerator;
import hmi.tts.Bookmark;
import hmi.tts.Phoneme;
import hmi.tts.TTSException;
import hmi.tts.TTSTiming;
import hmi.tts.TimingInfo;
import hmi.tts.Visime;
import hmi.tts.WordDescription;
import hmi.tts.util.PhonemeToVisemeMapping;


/* test bml:
 * 

<bml id="bml1" characterId="COUCH_M_1" xmlns="http://www.bml-initiative.org/bml/bml-1.0" 
  xmlns:bmlt="http://hmi.ewi.utwente.nl/bmlt">
  <speech id="s1" start="0">
    <text>Hello there. I am very important<sync id="important"/> and I want to talk.</text>
<description priority="2" type="offline">

<offline xmlns="http://www.yamaha.co.jp/vocaloid/schema/vsq3/">
1690,180,high,h aI
2050,300,ken-,kh e
2350,120,/,- n
</offline>

</description>
</speech>
</bml>

 * test wav created with:
say -o "hiken.wav" --file-format WAVE --data-format I16 "Hi Ken"

TODOs:

Implement mapping from the yamaha speech phonemes to whatever we use internally...

Instead of this csv format, can't we just support the original yamaha xml?

Looks like this behaviour needs to be bound from the TTS engines, i.e. see constructor of hmi.tts.BMLTTSBridge.MaryTTSBinding.
It would be preferable to activate/bind it in asap.speechengine.loader.SpeechEngineLoader (?)

Can we pass a path to an audio-file as an attribute in the <offline> tag?

What is the proper way to handle/detect silences/pauses. The "start" times are discarded, start currently is at 0, and visemes are just chained by their duration.
If the first row does not has a start time of 0, should we inject a silence of length "start time"?

Can we find a way that other engines (such as the TextEngine used for subtitles) does not see this markup, but gets a clear-text version of this?
Is that what we would use TTSBridge classes for? 

 */

public class OfflineSpeechGenerator extends AbstractTTSGenerator {

	// TODO: read the audio for this from the hard disk, or make optional (assume some other audio source is used?)
	private String testAudioPath = "/Users/jan/Dev/TechnicalDemonstrator/UT_HMI/HmiCouch/hiken.wav";
	
	private PhonemeToVisemeMapping visemeMapping;
	
	private Logger logger = LoggerFactory.getLogger(OfflineSpeechGenerator.class.getName());
	
	public OfflineSpeechGenerator(PhonemeToVisemeMapping ptv) {
		visemeMapping = ptv;
	}
	 
	@Override
	public TTSTiming speak(String text) throws TTSException {
		logger.debug("speak {}", text);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TTSTiming speakBML(String text) throws TTSException {
		// TODO Auto-generated method stub
		logger.debug("speakBML {}", text);
		return null;
	}

	@Override
	public TTSTiming speakToFile(String text, String filename) throws IOException, TTSException {
		logger.debug("speakToFile {} \n {}", text, filename);
		
        return null;
	}

	@Override
	public TTSTiming speakBMLToFile(String text, String filename) throws IOException, TTSException {
		logger.debug("speakBMLToFile {} \n {}", text, filename);
		
		Files.copy(Paths.get(testAudioPath), Paths.get(filename), StandardCopyOption.REPLACE_EXISTING);
		
        List<WordDescription> des = new ArrayList<WordDescription>();
        List<Visime> visAll = new ArrayList<Visime>();
        List<Bookmark> bms = new ArrayList<Bookmark>();
        
        String[] lines = text.split("\\r?\\n");
        System.out.println(visemeMapping.getVisemeForPhoneme(1));
        for (String line : lines) {
        	String[] tokens = line.split(",");
        	if (tokens.length != 4) continue;
        	// TODO: if tStart is not equal to the sum of preceding phonemes, there was a gap/silence that was not specified, I think then we want to inject a silence phoneme
        	int tStart = Integer.parseInt(tokens[0]);
        	int tDur = Integer.parseInt(tokens[1]);
        	String word = tokens[2];
        	String[] phonemes = tokens[3].split(" ");
        	List<Phoneme> phonemeList = new ArrayList<Phoneme>();
        	List<Visime> visemeList = new ArrayList<Visime>();
        	int avgdur = tDur/phonemes.length;
        	for (String phoneme : phonemes) {
        		// TODO: do this properly!
        		if (phoneme.length() == 0) continue;
        		phonemeList.add(new Phoneme(PhonemeNameToNumber.getPhonemeNumber(phoneme), avgdur,  true));
        		int visemeNumber = 5;//visemeMapping.getVisemeForPhoneme(PhonemeNameToNumber.getPhonemeNumber(phoneme));
        		visemeList.add(new Visime(visemeNumber,  avgdur,  true));// ,
        	}
        	
        	System.out.print("WORD: "+word+"\n    Phoneme: ");
        	for (Phoneme p : phonemeList) {
        		System.out.print(p.getNumber() +" " +p.getDuration()+ " / " );
        	}

        	System.out.print("\n    Visime: ");
        	for (Visime v : visemeList) {
        		System.out.print(v.getNumber() +" " +v.getDuration()+ " / " );
        	}
        	
        	System.out.println();
        	System.out.println();
        	des.add(new WordDescription(word, phonemeList, visemeList));
        	visAll.addAll(visemeList);
        }

        return new TimingInfo(des, bms, visAll);
	}

	@Override
	public TTSTiming getTiming(String text) throws TTSException {
		logger.debug("getTiming {}", text);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setVoice(String speaker) {
		logger.debug("setVoice {}", speaker);
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getVoice() {
		return "offline";
	}

	@Override
	public TTSTiming getBMLTiming(String s) throws TTSException {
		logger.debug("getBMLTiming {}", s);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getVoices() {
		return new String[] { "offline" };
	}

}
