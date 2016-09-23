/*******************************************************************************
 *******************************************************************************/
package asap.fluencyttsbinding;

import asap.speechengine.ttsbinding.TTSBinding;
import asap.speechengine.ttsbinding.TTSBindingFactory;


/**
 * Produces a FluencyTTSBinding 
 * @author Dennis Reidsma
 *
 */
public class FluencyTTSBindingFactory implements TTSBindingFactory
{
    @Override
    public TTSBinding createBinding()
    {
        return new FluencyTTSBinding();
    }
}