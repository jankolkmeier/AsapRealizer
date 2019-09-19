package asap.animationengine.restpose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import asap.animationengine.AnimationPlayer;
import asap.animationengine.MovementTimingUtils;
import asap.animationengine.gesturebinding.GestureBinding;
import asap.animationengine.motionunit.AnimationUnit;
import asap.animationengine.motionunit.MUSetupException;
import asap.animationengine.motionunit.TimedAnimationMotionUnit;
import asap.animationengine.procanimation.IKBody;
import asap.animationengine.transitions.SlerpTransitionToPoseMU;
import asap.animationengine.transitions.T1RTransitionToPoseMU;
import asap.animationengine.transitions.TransitionMU;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.OffsetPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.pegboard.TimePeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.TimedPlanUnitState;
import hmi.animation.Hanim;
import hmi.animation.SkeletonPose;
import hmi.animation.VJoint;
import hmi.animation.VObjectTransformCopier;
import hmi.math.Quat4f;
import hmi.neurophysics.BiologicalSwivelCostsEvaluator;
import hmi.xml.XMLTokenizer;

public class IKSkeletonPoseRestPose extends SkeletonPoseRestPose {
	

	private final String root_ik_target_name = "__root_target";
	private final String l_foot_ik_target_name = "__l_foot_target";
	private final String r_foot_ik_target_name = "__r_foot_target";
	private final String l_hand_ik_target_name = "__l_hand_target";
	private final String r_hand_ik_target_name = "__r_hand_target";
	private VJoint currLFootTargetBone = null;
	private VJoint currRFootTargetBone = null;
	private VJoint currLHandTargetBone = null;
	private VJoint currRHandTargetBone = null;
	private VJoint currRootTargetBone = null;

    private BiologicalSwivelCostsEvaluator r_autoSwivel;
    private BiologicalSwivelCostsEvaluator l_autoSwivel;
	
    protected VJoint ikTree; 
	
	private VObjectTransformCopier votcCurrToIk;
	private VObjectTransformCopier poseTreeToIk;
	private IKBody ikbody;
	
	public IKSkeletonPoseRestPose(XMLTokenizer tokenizer) throws IOException {
		super(new SkeletonPose(tokenizer));
	}

    public IKSkeletonPoseRestPose() {
	}
    
    @Override
    public TimedAnimationMotionUnit createTransitionToRest(FeedbackManager fbm, Set<String> joints, double startTime, String bmlId,
            String id, BMLBlockPeg bmlBlockPeg, PegBoard pb)
    {
        return createTransitionToRest(fbm, joints, startTime, 1, bmlId, id, bmlBlockPeg, pb);
    }

    @Override
    public TimedAnimationMotionUnit createTransitionToRest(FeedbackManager fbm, Set<String> joints, TimePeg startPeg, TimePeg endPeg,
            String bmlId, String id, BMLBlockPeg bmlBlockPeg, PegBoard pb)
    {
        TransitionMU mu = createTransitionToRest(joints);
        mu.addKeyPosition(new KeyPosition("start", 0));
        mu.addKeyPosition(new KeyPosition("end", 1));
        TimedAnimationMotionUnit tmu = new TimedAnimationMotionUnit(fbm, bmlBlockPeg, bmlId, id, mu, pb, player);
        tmu.setTimePeg("start", startPeg);
        tmu.setTimePeg("end", endPeg);
        tmu.setState(TimedPlanUnitState.LURKING);
        return tmu;
    }

    @Override
    public TimedAnimationMotionUnit createTransitionToRest(FeedbackManager fbm, Set<String> joints, double startTime, double duration,
            String bmlId, String id, BMLBlockPeg bmlBlockPeg, PegBoard pb)
    {
        TimePeg startPeg = new TimePeg(bmlBlockPeg);
        startPeg.setGlobalValue(startTime);
        TimePeg endPeg = new OffsetPeg(startPeg, duration);
        return createTransitionToRest(fbm, joints, startPeg, endPeg, bmlId, id, bmlBlockPeg, pb);
    }
    
    
    
    
    @Override
    public TransitionMU createTransitionToRest(Set<String> joints)
    {
		poseTreeToIk.copyConfig();
        updateIK();
        float rotations[] = new float[joints.size() * 4];
        int i = 0;
        List<VJoint> targetJoints = new ArrayList<VJoint>();
        List<VJoint> startJoints = new ArrayList<VJoint>();
        for (String joint : joints)
        {
            VJoint vj = ikTree.getPartBySid(joint);
            vj.getRotation(rotations, i);
            targetJoints.add(player.getVNextPartBySid(joint));
            startJoints.add(player.getVCurrPartBySid(joint));
            i += 4;
        }
        TransitionMU mu = new SlerpTransitionToPoseMU(targetJoints, startJoints, rotations);
        mu.setStartPose();
        return mu;
    }
   
    @Override
    public TransitionMU createTransitionToRestFromVJoints(Collection<VJoint> joints)
    {
		poseTreeToIk.copyConfig();
        updateIK();
        float rotations[] = new float[joints.size() * 4];
        int i = 0;
        List<VJoint> targetJoints = new ArrayList<VJoint>();
        List<VJoint> startJoints = new ArrayList<VJoint>();
        for (VJoint joint : joints)
        {
            VJoint vj = ikTree.getPartBySid(joint.getSid());
            vj.getRotation(rotations, i);
            targetJoints.add(joint);
            startJoints.add(player.getVCurrPartBySid(joint.getSid()));
            i += 4;
        }
        TransitionMU mu = new SlerpTransitionToPoseMU(targetJoints, startJoints, rotations);
        mu.setStartPose();
        return mu;
    }
    
    
    @Override
    public double getTransitionToRestDuration(VJoint vCurrent, Set<String> joints) {
		poseTreeToIk.copyConfig();
        updateIK();
        double duration = MovementTimingUtils.getFittsMaximumLimbMovementDuration(vCurrent, ikTree, joints);
        if (duration > 0) return duration;
        return 1;
    }

    @Override
    protected void setRotConfig(VJoint tree, int startIndex, float[] config)
    {
        int i = 0;
        for(VJoint vj:tree.getParts())
        {
            if(vj.getSid()!=null)
            {
                vj.getRotation(config, startIndex+i);
                i+=4;
            }
        }
    }
    
    @Override
    public PostureShiftTMU createPostureShiftTMU(FeedbackManager bbf, BMLBlockPeg bmlBlockPeg, String bmlId, String id, PegBoard pb)
            throws MUSetupException
    {
		poseTreeToIk.copyConfig();
        updateIK();
        List<VJoint> targetJoints = new ArrayList<VJoint>();
        List<VJoint> startJoints = new ArrayList<VJoint>();
        
        for(VJoint vj:ikTree.getParts())
        {
            if(vj.getSid()!=null)
            {
                targetJoints.add(player.getVNextPartBySid(vj.getSid()));
                startJoints.add(player.getVCurrPartBySid(vj.getSid()));
            }
        }
        
        AnimationUnit mu;
        float config[]= new float[targetJoints.size()*4+3];
        ikTree.getPartBySid(Hanim.HumanoidRoot).getTranslation(config);
        setRotConfig(ikTree, 3, config);
        mu = new T1RTransitionToPoseMU(targetJoints, startJoints, config);
        return new PostureShiftTMU(bbf, bmlBlockPeg, bmlId, id, mu.copy(player), pb, this, player);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void setupIK() {
    	for (VJoint j : player.getVCurr().getParts()) {
    		switch (j.getName()) {
    			case l_foot_ik_target_name:
    				currLFootTargetBone = j;
    				break;
    			case r_foot_ik_target_name:
    				currRFootTargetBone = j;
    				break;
    			case l_hand_ik_target_name:
    				currLHandTargetBone = j;
    				break;
    			case r_hand_ik_target_name:
    				currRHandTargetBone = j;
    				break;
    			case root_ik_target_name:
    				currRootTargetBone = j;
				default:
					break;
    		}
    	}
    	
    	if (currLFootTargetBone == null || currRFootTargetBone == null ||
    		currLHandTargetBone == null || currRHandTargetBone == null || currRootTargetBone == null) {
    		throw new RuntimeException("Failed to setup IK");
    	}
        ikTree = poseTree.copyTree("ik-");
        ikbody = new IKBody(ikTree);
        int humanoidRootIdx = player.getVCurr().getParts().indexOf(player.getVCurrPartBySid(Hanim.HumanoidRoot));
        votcCurrToIk = VObjectTransformCopier.newInstanceFromVJointTree(player.getVCurr(), ikTree, "T1R");
        votcCurrToIk.setRootTranslationIdx(humanoidRootIdx);
        poseTreeToIk = VObjectTransformCopier.newInstanceFromVJointTree(poseTree, ikTree, "T1R");
        poseTreeToIk.setRootTranslationIdx(humanoidRootIdx);
        r_autoSwivel = GestureBinding.constructAutoSwivel("right_arm");
        l_autoSwivel = GestureBinding.constructAutoSwivel("left_arm");
    	//lHandRig = new ArmRig(player.getVCurr(), Hanim.l_shoulder, currLHandTargetBone);
    	//rHandRig = new ArmRig(player.getVCurr(), Hanim.r_shoulder, currRHandTargetBone);
    }

	@Override
    public void setAnimationPlayer(AnimationPlayer player) {
        super.setAnimationPlayer(player);
        setupIK();
    }

	@Override
    public RestPose copy(AnimationPlayer player)
    {
    	IKSkeletonPoseRestPose copy = new IKSkeletonPoseRestPose();
        if (pose != null)
        {
            copy.pose = pose.untargettedCopy();
        }
        copy.setAnimationPlayer(player);
        return copy;
    }
	
	public Set<String> updateIK() {
        float t[] = new float[3];
        currRootTargetBone.getTranslation(t);
	    ikbody.setRoot(t);
	    ikbody.setLeftHand(currLHandTargetBone.getPosition());
	    ikbody.setRightHand(currRHandTargetBone.getPosition());
	    ikbody.setLeftFoot(currLFootTargetBone.getPosition(), true);
	    ikbody.setRightFoot(currRFootTargetBone.getPosition(), true);
	    ikbody.setSwivelLeftHand(-1.2);
	    ikbody.setSwivelRightHand(1.2);
	    ikbody.setSwivelRightHand(r_autoSwivel.getSwivelAngleWithMinCost(ikbody.getSwivelRightArm()));
	    ikbody.setSwivelLeftHand(l_autoSwivel.getSwivelAngleWithMinCost(ikbody.getSwivelLeftArm()));
	    return ikbody.getKinematicJoints();
	}
	
    @Override
    public void play(double time, Set<String> kinematicJoints, Set<String> physicalJoints)
    {
        if (poseTree == null) return;

        float qTmp1[] = new float[4];
        float qTmp2[] = new float[4];
        float q[] = new float[4];
        float t[] = new float[3];

        votcCurrToIk.copyConfig();
        Set<String> ikJoints = updateIK();

        //double tPrep = 0.25;
        //double preparationDuration = 0.01;
        
        //double remDuration = ( (tPrep-time)/tPrep)*preparationDuration;
        float deltaT = (float)(player.getStepTime()*5);
        
        for (VJoint vj : poseTree.getParts()) {
            if (!kinematicJoints.contains(vj.getSid()) && !physicalJoints.contains(vj.getSid())) {
            	if (ikJoints.contains(vj.getSid())) {
                   //if(time < tPrep) {
                    	ikTree.getPartBySid(vj.getSid()).getRotation(qTmp1);
                      //player.getVPrevPartBySid(vj.getSid()).getRotation(qTmp2);
                    	player.getVPrevPartBySid(vj.getSid()).getRotation(qTmp2);
                        Quat4f.interpolate(q, qTmp2, qTmp1, deltaT);
                   // } else {
                    	ikTree.getPartBySid(vj.getSid()).getRotation(q);
                   // }
                    // TODO: force exact wrist rotation here?
            	} else {
            		vj.getRotation(q);
            	}
                VJoint vjSet = player.getVNextPartBySid(vj.getSid());
                if (vjSet != null) {
                    vjSet.setRotation(q);  
                    
                    if (vjSet.getSid() == Hanim.HumanoidRoot) { //  && (pose == null || pose.getConfigType().equals("T1R"))
                       //vj.getTranslation(t);
                        ikTree.getPartBySid(Hanim.HumanoidRoot).getTranslation(t);
                        vjSet.setTranslation(t);
                    }
                }                
            }
        }

    }
}