package asap.faceengine.faceunit;

import java.util.HashMap;
import java.util.Map;

import com.google.common.util.concurrent.AtomicDouble;

import asap.faceengine.faceunit.AUFU.AUFUSide;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.pegboard.PegBoard;
import asap.realizer.planunit.InvalidParameterException;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;
import hmi.faceanimation.FaceController;
import hmi.faceanimation.converters.EmotionConverter;
import hmi.faceanimation.converters.FACS2MorphConverter;
import hmi.faceanimation.converters.FACSConverter;
import hmi.faceanimation.model.ActionUnit;
import hmi.faceanimation.model.FACS;
import hmi.faceanimation.model.FACSConfiguration;
import hmi.faceanimation.model.ActionUnit.Symmetry;
import hmi.faceanimation.model.FACS.Side;
import hmi.util.StringUtil;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 * A basic facial animation unit consisting of one FACS configuration The key
 * positions are: start, attackPeak, relax, end. This describes an apex-like
 * intensity development: Between start and attackPeak, the face configuration is
 * blended in; between relax and end the face configuration is blended out.
 * 
 * Parameter constraint: facsConfig should not be null
 * This version translates the FACS to a set of morph target values erather than the MPEG4 values that FACSFU uses.
 * 
 * @author Dennis Reidsma
 * @author Jan Kolkmeier
 */

@Slf4j
public class FACS2MorphFU implements FaceUnit {
    @Delegate
    private final KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();

    protected AtomicDouble intensity = new AtomicDouble(1f);
    protected AtomicDouble prevIntensity = new AtomicDouble(1f);

    protected FACSConfiguration facsConfig = new FACSConfiguration();
    
    // ==== 
    enum AUFUSide
    {
        LEFT, RIGHT, BOTH
    }
    private AUFUSide side = null;
    private int aunr = -1;
    // ====
    

    private FaceController faceController;
    private FACS2MorphConverter facs2morphConverter;
    
    /** morphs contains the targets and their weights for this face unit with intensity =1 */
    private Map<String, Double> morphs = new HashMap<String, Double>();
    
    
    public FACS2MorphFU() {
        KeyPosition start = new KeyPosition("start", 0d, 1d);
        KeyPosition attackPeak = new KeyPosition("attackPeak", 0.1d, 1d);
        KeyPosition relax = new KeyPosition("relax", 0.9d, 1d);
        KeyPosition end = new KeyPosition("end", 1d, 1d);
        addKeyPosition(start);
        addKeyPosition(attackPeak);
        addKeyPosition(relax);
        addKeyPosition(end);
    }

    public void setFaceController(FaceController fc) {
        faceController = fc;
    }

    public void setFACS2MorphConverter(FACS2MorphConverter f2mc) {
        facs2morphConverter = f2mc;
        if (facsConfig != null)
        	morphs = facs2morphConverter.convert(facsConfig);
    }

    @Override
    public void startUnit(double t) {

    }
    
    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterException {
        if (name.equals("intensity")) {
            intensity.set(value);
            prevIntensity.set(value);
        }
        else throw new ParameterNotFoundException(name);
    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException {
    	/*
        if (StringUtil.isNumeric(value)) {
            setFloatParameterValue(name, Float.parseFloat(value));
        } else {
            throw new InvalidParameterException(name, value);
        }
        */
        
        if (name.equals("au"))
        {
            aunr = Integer.parseInt(value);
        }
        else if (name.equals("side"))
        {
            side = AUFUSide.BOTH;
            if (value.equals("LEFT")) side = AUFUSide.LEFT;
            else if (value.equals("RIGHT")) side = AUFUSide.RIGHT;
            // System.out.println("side: " + side);
        }
        else if (StringUtil.isNumeric(value)) // intensity, ...
        {
            setFloatParameterValue(name, Float.parseFloat(value));
        }
        else
        {
            throw new InvalidParameterException(name, value);
        }
        
        setAU(side, aunr, intensity.floatValue());
    }
    

    public void setAU(AUFUSide s, int i, float intens)
    {
        side = s;
        aunr = i;
        intensity.set(intens);
        prevIntensity.set(intens);
        
        if (!auHasValidParameters())
        {
            //log.warn("Parameters not valid for setAU");
            return;
        }
        facsConfig = new FACSConfiguration();
        ActionUnit au = FACS.getActionUnit(aunr);
        float newval = intensity.floatValue();
        if (au.getSymmetry() != Symmetry.ASYMMETRIC)
        {
            facsConfig.setValue(Side.NONE, au.getIndex(), newval);
        }
        else
        {
            if (side == AUFUSide.LEFT) facsConfig.setValue(Side.LEFT, au.getIndex(), newval);
            if (side == AUFUSide.RIGHT) facsConfig.setValue(Side.RIGHT, au.getIndex(), newval);
            if (side == AUFUSide.BOTH)
            {
                
                facsConfig.setValue(Side.LEFT, au.getIndex(), newval);
                facsConfig.setValue(Side.RIGHT, au.getIndex(), newval);
            }
        }        
    }


    @Override
    public String getParameterValue(String name) throws ParameterException {
        return "" + getFloatParameterValue(name);
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException {
        if (name.equals("intensity")) return intensity.floatValue();
        throw new ParameterNotFoundException(name);
    }

    @Override
    public boolean hasValidParameters()
    {
    	if (facs2morphConverter != null) return true; // TODO: the below conditions are for AU2Morph...
    	return auHasValidParameters();
    }

    private boolean auHasValidParameters() {
        ActionUnit au = FACS.getActionUnit(aunr);
        if (au == null) return false;
        if (intensity.floatValue() > 1) return false;
        if (au.getSymmetry() != Symmetry.ASYMMETRIC) return (side == AUFUSide.BOTH) || (side == null);

        return (side != null);
    }
    
    public void setConfig(FACSConfiguration fc) {
        facsConfig = fc;
        if (facs2morphConverter != null)
        	morphs = facs2morphConverter.convert(facsConfig);
    }

    /**
     * Executes the face unit, by applying the face configuration. Linear
     * interpolate from intensity 0..max between start and attackPeak; keep at max
     * till relax; then back to zero from relax till end.
     * 
     * @param t
     *            execution time, 0 &lt; t &lt; 1
     * @throws FUPlayException
     *             if the play fails for some reason
     */
    public void play(double t) throws FUPlayException {
        // between where and where? Linear interpolate from intensity 0..max between start&attackPeak;
        // then down from relax till end
        double attackPeak = getKeyPosition("attackPeak").time;
        double relax = getKeyPosition("relax").time;
        float newAppliedWeight = 0;

        if (t < attackPeak && t > 0)
        {
            newAppliedWeight = intensity.floatValue() * (float) (t / attackPeak);
        }
        else if (t >= attackPeak && t <= relax)
        {
            newAppliedWeight = intensity.floatValue();
        }
        else if (t > relax && t < 1)
        {
            newAppliedWeight = intensity.floatValue() * (float) (1 - ((t - relax) / (1 - relax)));
        }
        
        morphs = facs2morphConverter.convert(facsConfig);
        float[] values = new float[morphs.size()];
        String[] morphNames = new String[morphs.size()];

        int v = 0;
        for (Map.Entry<String, Double> entry : morphs.entrySet()) {
        	morphNames[v] = entry.getKey();
        	values[v] = entry.getValue().floatValue() * newAppliedWeight;
        	v++;
        }

        //TODO: DENNIS eigenlijk moet dit remove previous weight & add new weight gebruiken. Dat zou in deze engine misschien wel op meer plekken moeten...
        
    	faceController.setMorphTargets(morphNames, values);
        prevIntensity.set(newAppliedWeight);
    }

    @Override
    public void interruptFromHere() {
        intensity.set(prevIntensity.get());
    }

    /**
     * Creates the TimedFaceUnit corresponding to this face unit
     * @param bmlId
     *            BML block id
     * @param id
     *            behaviour id
     * 
     * @return the TFU
     */
    @Override
    public TimedFaceUnit createTFU(FeedbackManager bfm, BMLBlockPeg bbPeg, String bmlId, String id, PegBoard pb) {
        return new TimedFaceUnit(bfm, bbPeg, bmlId, id, this, pb);
    }

    /**
     * @return Prefered duration (in seconds) of this face unit, 0 means not
     *         determined/infinite
     */
    public double getPreferedDuration() {
        return 1d;
    }

    /**
     * Create a copy of this face unit and link it to the facecopntroller
     */
    public FaceUnit copy(FaceController fc, FACSConverter fconv, EmotionConverter econv, FACS2MorphConverter f2mconv) {
    	FACS2MorphFU result = new FACS2MorphFU();
        result.setFaceController(fc);
        result.setFACS2MorphConverter(f2mconv);
        result.intensity = intensity;
        result.setConfig(facsConfig);
        for (KeyPosition keypos : getKeyPositions()) {
            result.addKeyPosition(keypos.deepCopy());
        }
        
        return result;
    }
}
