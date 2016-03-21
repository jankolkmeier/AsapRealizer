package asap.naoengine.naounits;

import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.TimedPlanUnitPlayException;

public class NaoAsyncUnit extends AbstractNaoUnit{

	public NaoAsyncUnit(FeedbackManager fbm, BMLBlockPeg bmlPeg, String bmlId, String behId) {
		super(fbm, bmlPeg, bmlId, behId);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void startUnit(double time) throws TimedPlanUnitPlayException {
		
	}

}
