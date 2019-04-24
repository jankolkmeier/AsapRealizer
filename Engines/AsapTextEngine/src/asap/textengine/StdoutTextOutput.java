/*******************************************************************************
 *******************************************************************************/
package asap.textengine;

import asap.realizer.planunit.ParameterException;
import asap.realizer.planunit.ParameterNotFoundException;

/**
 * TextOutput that prints the text to the stdout
 * @author welberge
 *
 */
public class StdoutTextOutput implements TextOutput
{
    public StdoutTextOutput()
    {
        
    }

    @Override
    public void setText(String textProgress)
    {
        System.out.println(textProgress);        
    }
    
    @Override
    public void setText(String textProgress, String textComplete)
    {
    	setText(textProgress);        
    }

    @Override
    public void setFloatParameterValue(String parameter, float value)throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
    }
        
    @Override
    public void setParameterValue(String parameter, String value) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
        
    }   
    
    @Override
    public float getFloatParameterValue(String parameter) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
}

    @Override
    public String getParameterValue(String parameter) throws ParameterException
    {
        throw new ParameterNotFoundException(parameter);
    }   
    
}
