/*******************************************************************************
 * Copyright (C) 2009 Human Media Interaction, University of Twente, the Netherlands
 * 
 * This file is part of the Elckerlyc BML realizer.
 * 
 * Elckerlyc is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Elckerlyc is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Elckerlyc.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/
package asap.middlewareengine.planunit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import asap.middlewareengine.embodiment.MiddlewareEmbodiment;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 * parameters:
 * content: string is json data as string
 * middlewareloaderclass: string (optional) an indication of the middlewareloaderclass to be used for this message
 * middlewareloaderproperties: string (optional) an indication of the middleware properties to be used for this message
 * 
 * @author Dennis
 *
 */
@Slf4j
public class SendJsonDataMessageMU implements MiddlewareUnit
{
	@Delegate
    private final KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();


    private String ruId;
    
    private String msgContent = "";
    private JsonNode msgContentAsJson = null;
    private String loaderclass=null;
    private Properties props = null;
    private String propsAsString = null;
    
	private MiddlewareEmbodiment mwe;
	private ObjectMapper om = new ObjectMapper();
	
	private boolean running;

    public SendJsonDataMessageMU()
    {
        KeyPosition start = new KeyPosition("start", 0d, 1d);
        KeyPosition end = new KeyPosition("end", 1d, 1d);
        addKeyPosition(start);
        addKeyPosition(end);
    }

    public void setEmbodiment(MiddlewareEmbodiment re)
    {
    	this.mwe = re;
    }
    
    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterException
    {
    	//
    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {
    	if (name.equals("content")) 
    	{
    		msgContent = value;
    		if (msgContent != null && !msgContent.equals(""))
    		{
    			//check that it is proper JSON
    			try
    			{
    				msgContentAsJson = om.readTree(msgContent);
    				if (msgContentAsJson == null)
    				{
    					msgContent = "";
    	    			throw new ParameterException("invalid content: cannot parse JSON \n "+ value);
    				}
    			} catch (Exception ex)
    			{
    				msgContent = "";
    				msgContentAsJson = null;
	    			throw new ParameterException("invalid content: cannot parse JSON \n "+ value);
    			}
    			
    		}
    	} else if (name.equals("middlewareloaderclass"))
    	{
    		loaderclass= value;
		} else if (name.equals("middlewareloaderproperties"))
		{
			propsAsString=value;
			props = new Properties();
			
			//parse the properties for this middleware
			List<String> ps = new ArrayList<String>(Arrays.asList(value.split(",")));
			for(String p : ps){
				props.put(p.substring(0,p.indexOf(':')),p.substring(p.indexOf(':')+1));
			}

		}
    }

    @Override
    public String getParameterValue(String name) throws ParameterException
    {
    	if (name.equals("content")) return msgContent;
    	if (name.equals("middlewareloaderclass")) return loaderclass;
    	if (name.equals("middlewareloaderproperties")) return propsAsString;
    	return "";
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException
    {
        return 0;
    }

    @Override
    public boolean hasValidParameters()
    {
    	//TODO check if content is well formed
    	if (msgContentAsJson == null) return false;
    	if (loaderclass != null && !loaderclass.equals("") && props==null) return false;
    	return (msgContentAsJson!=null);
    }

    /** start the unit. */
    public void startUnit(double startTime, double endTime) throws MUPlayException
    {
    	if (loaderclass == null || loaderclass.equals(""))
    	{
    		if (props!=null)
    		{
    			mwe.getMiddleware(props).sendData(msgContentAsJson);
    		}
    		else
    		{
    			mwe.getMiddleware().sendData(msgContentAsJson);
    		}
    	}
    	else //loaderclass set: then assume props also set; 
    	{
    		mwe.getMiddleware(loaderclass, props).sendData(msgContentAsJson);
    	}
    }

    /**
     * Constantly monitor whether the current behavior has finished playing on the robot.
     * As soon as the behavior finishes a new keyposition is created which initiates appropriate feedback
     * @param t
     *            execution time, 0 &lt t &lt 1
     * @throws RUPlayException
     *             if the play fails for some reason
     */
    public void play(double t) throws MUPlayException
    {
    //TODO
    }

    public void cleanup()
    {
    }

    @Override
    public TimedMiddlewareUnit createTMU(FeedbackManager bfm, BMLBlockPeg bbPeg, String bmlId, String id)
    {
        this.ruId = id;
        return new TimedMiddlewareUnit(bfm, bbPeg, bmlId, id, this);
    }



    /**
     * Create a copy of this  unit and set its parameters
     */
    @Override
    public MiddlewareUnit copy(MiddlewareEmbodiment middlewareEmbodiment)
    {
        SendJsonDataMessageMU result = new SendJsonDataMessageMU();
        result.setEmbodiment(middlewareEmbodiment);
        try 
        {
        	if (msgContent != null) result.setParameterValue("content", msgContent);
        	if (loaderclass != null && !loaderclass.equals("")) result.setParameterValue("middlewareloaderclass",  loaderclass);
        	if (props!=null)result.setParameterValue("middlewareloaderproperties",  propsAsString);
        } catch (ParameterException ex)
        {
        	log.error("Unexplainable error: cannot set content of a SendJSONDataMessageMU");
        }
        for (KeyPosition keypos : getKeyPositions())
        {
            result.addKeyPosition(keypos.deepCopy());
        }
        return result;
    }



    @Override
    public double getPreferredDuration()
    {
        return 0d;
    }

    @Override
	public void prepareUnit() {
		// nothing needs be done here
	}

}
