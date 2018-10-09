/*******************************************************************************
 *******************************************************************************/
package asap.activemqadapters;

/**
 * Topics for BML and BML feedback
 * not used so much anymore, as we now have adapters that take these as parameters in the loader.
 * @deprecated
 * @author Herwin
 */
public final class AMQBMLConstants
{
    public static final String BML = "asap.bml.request"; 
    public static final String BML_FEEDBACK = "asap.bml.feedback";
    private AMQBMLConstants(){}
}
