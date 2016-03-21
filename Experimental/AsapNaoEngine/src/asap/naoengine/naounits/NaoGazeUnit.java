package asap.naoengine.naounits;

import java.util.ArrayList;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

public class NaoGazeUnit extends AbstractNaoUnit{

	private String target;
	private String influence = "Head";
	private float offsetAngle;
	private String direction;
	
	private ALMotion motion;
	private ALRobotPosture posture;
	
	public NaoGazeUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId) {
		super(fbm, bmlPeg, bmlId, behId);
		try {
			motion = new ALMotion(application.session());
			posture = new ALRobotPosture(application.session());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public void setInfluence(String influence) {
		this.influence = influence;
	}
	
	public void setOffsetAngle(float offsetAngle) {
		this.offsetAngle = offsetAngle;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	@Override
	protected void startUnit(double time) throws TimedPlanUnitPlayException {
		ArrayList<Float> angles = new ArrayList<Float>();
		try {
	        motion.wakeUp();
	        posture.goToPosture("StandInit", 0.5f);
	        String effectorName = "Head";
	        
	        switch (influence) {
	            case "Head":
					//motion.stiffnessInterpolation(influence, 1.0f,1.0f);
			        ArrayList<Float> timeList = new ArrayList<Float>();
			        timeList.add(3.0f);
			        timeList.add(6.0f);
			        timeList.add(9.0f);
			        
			        angles.add(-1.5f);
			        angles.add(1.5f);
			        angles.add(0.0f);
			        
			        motion.angleInterpolation(effectorName, angles, timeList, true);
			        break;
	            case "Body":
	            	angles.add(0.0f);
	            	angles.add(0.0f);
	            	angles.add(-1.5f);
	            	motion.wbEnableEffectorControl(effectorName, true);
	            	motion.wbSetEffectorControl(effectorName, angles);
	            	break;
	            default:
	            	break;
	        }
			setEnd(startPeg);
		} catch (CallError | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
