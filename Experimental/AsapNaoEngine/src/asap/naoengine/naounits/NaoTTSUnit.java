package asap.naoengine.naounits;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

public class NaoTTSUnit extends AbstractNaoUnit{

	private ALMemory memory;
	private ALTextToSpeech tts;
	private String speech;
	
	public NaoTTSUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId, String speech) {
		super(fbm, bmlPeg, bmlId, behId);
		this.speech = speech;
		try {
			this.feedback(application.session());
			tts = new ALTextToSpeech(application.session());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setSpeech(String speech) {
		this.speech = speech;
	}
	
	public String getSpeech() {
		return speech;
	}

	@Override
	protected void startUnit(double time) throws TimedPlanUnitPlayException {
		System.out.println("Start");
		try {
			tts.say(speech);
			//TODO: Endzeit erst bei Feedback vom Roboter setzen.
			setEnd(startPeg);
		} catch (CallError | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void feedback(Session session) throws Exception {
		System.out.println("FEEDBACK");
		memory = new ALMemory(session);
		
		memory.subscribeToEvent("TextDone", new EventCallback<Float>() {
			@Override
			public void onEvent(Float arg0) {
				System.out.println("TEXT DONE ======================================================================================");
				if (arg0 > 0) {
					System.out.println("ICH HABE FERTIG MIT TEXT!!!!!!!!!!!!!!!");
				}
			}
		});
	}

}
