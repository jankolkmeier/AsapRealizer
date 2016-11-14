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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import hmi.util.Resources;
import hmi.util.StringUtil;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 * parameters:
 * filename: filename of template
 * resource (optional): resource dir of template 
 * middlewareloaderclass: string (optional) an indication of the middlewareloaderclass to be used for this message
 * middlewareloaderproperties: string (optional) an indication of the middleware properties to be used for this message
 * any other property: used to str-replace the parameters in the template file
 * 
 * @author Dennis
 *
 */
@Slf4j
public class SendJsonTemplateMessageMU implements MiddlewareUnit
{
	@Delegate
    private final KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();


    private String ruId;
    /** this one is set in the copy method */
    private JsonNode msgContentAsJson = null; 
    private String loaderclass=null;
    private Properties props = null;
    private String propsAsString = null;
    private String filename=null;
    private String resource = "";
    private Map<String,String> templateParameterMap = new HashMap<String,String>(); 
    
	private MiddlewareEmbodiment mwe;
	private ObjectMapper om = new ObjectMapper();
	
	private double duration;
	
	private boolean running;

    public SendJsonTemplateMessageMU()
    {
    	duration = 0d;
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
    	if (name.equals("filename")) 
    	{
    		filename = value;
    	} else if (name.equals("resource")) 
    	{
    		resource = value;
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
		} else if (StringUtil.isNumeric(value) && name.equals("duration")) {
			duration = Double.parseDouble(value);
			templateParameterMap.put(name, value);
        } else 
		{
			templateParameterMap.put(name, value);
		}
			
    }

    @Override
    public String getParameterValue(String name) throws ParameterException
    {
    	if (name.equals("filename")) return filename;
    	if (name.equals("resource")) return resource;
    	if (name.equals("middlewareloaderclass")) return loaderclass;
    	if (name.equals("middlewareloaderproperties")) return propsAsString;
    	if (templateParameterMap.containsKey(name)) return templateParameterMap.get(name);
    	return null;
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
    	if (filename == null) return false;
    	if (loaderclass != null && !loaderclass.equals("") && props==null) return false;
    	return true;
    }

    /** start the unit. */
    public void startUnit(double startTime, double endTime) throws MUPlayException
    {
		templateParameterMap.put("duration", (endTime-startTime)+"");
    	prepareMessage();
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
     * @throws MUPlayException
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


    protected void prepareMessage()
    {
		try 
		{
            Resources r = new Resources(resource);
            if (filename==null||filename.equals(""))return;
            String fileContents = r.read(filename);
            for (Map.Entry<String, String> entry : templateParameterMap.entrySet())
            {
            	fileContents = fileContents.replaceAll("\\$\\{"+entry.getKey()+"\\}", entry.getValue());            	
            }
            msgContentAsJson = om.readTree(fileContents);
		}
		catch (Exception ex)
		{
			log.error("Could not fill template. Path: {} File: {}", resource, filename);
			log.error("{}",ex);
		}
    	
    }
    /**
     * Create a copy of this  unit and set its parameters
     */
    @Override
    public MiddlewareUnit copy(MiddlewareEmbodiment middlewareEmbodiment)
    {
        SendJsonTemplateMessageMU result = new SendJsonTemplateMessageMU();
        result.setEmbodiment(middlewareEmbodiment);
        try 
        {
        	if (loaderclass != null && !loaderclass.equals("")) result.setParameterValue("middlewareloaderclass",  loaderclass);
        	if (props!=null)result.setParameterValue("middlewareloaderproperties",  propsAsString);
        	result.setParameterValue("filename",  filename);
        	result.setParameterValue("resource",  resource);
        	for (Map.Entry<String,String> entry : templateParameterMap.entrySet())
        	{
        		result.setParameterValue(entry.getKey(), entry.getValue());
        	}
        	
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
        return duration;
    }

	@Override
	public void prepareUnit() {
		// nothing needs be done here
	}

}
