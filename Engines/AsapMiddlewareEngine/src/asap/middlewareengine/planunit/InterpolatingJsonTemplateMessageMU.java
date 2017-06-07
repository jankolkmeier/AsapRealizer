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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.primitives.Floats;

import asap.middlewareengine.embodiment.MiddlewareEmbodiment;
import asap.realizer.feedback.FeedbackManager;
import asap.realizer.pegboard.BMLBlockPeg;
import asap.realizer.planunit.KeyPosition;
import asap.realizer.planunit.KeyPositionManager;
import asap.realizer.planunit.KeyPositionManagerImpl;
import asap.realizer.planunit.ParameterException;
import hmi.util.Resources;
import lombok.Delegate;
import lombok.extern.slf4j.Slf4j;

/**
 * parameters:
 * filename: filename of template
 * resource (optional): resource dir of template 
 * middlewareloaderclass: string (optional) an indication of the middlewareloaderclass to be used for this message
 * middlewareloaderproperties: string (optional) an indication of the middleware properties to be used for this message
 * Any other property: used to str-replace the parameters in the template file
 * 
 * This planunit will interpolate some parameters to follow an attach/hold/release intensity between 0 and 1, similar to the regular FaceBehaviours.
 * To specify a parameter that needs to be interpolated between a min and max value:
 * Use min_<parname> and max_<parname> float parameters, the planunit will take this as the min and max values for parameter <parname> 
 * 
 * @author Dennis
 *
 */
@Slf4j
public class InterpolatingJsonTemplateMessageMU implements MiddlewareUnit
{
	@Delegate
    private final KeyPositionManager keyPositionManager = new KeyPositionManagerImpl();


    private String ruId;
    /** this one is set in the copy method to be the parameter replaced version of the template for all parameters except the interpolating ones. The latter are replaced at playtime before transforming the message to json */
    private String msgContentAsString = "";
    private JsonNode msgContentAsJson = null; 
    private String loaderclass=null;
    private Properties props = null;
    private String propsAsString = null;
    private String filename=null;
    private String resource = "";
    private Map<String,String> templateParameterMap = new HashMap<String,String>(); 
    private Map<String,Float> interpolatingMaxValues = new HashMap<String,Float>(); 
    private Map<String,Float> interpolatingMinValues = new HashMap<String,Float>(); 
    
	private MiddlewareEmbodiment mwe;
	private ObjectMapper om = new ObjectMapper();
	
	private boolean running;

    public InterpolatingJsonTemplateMessageMU()
    {
        KeyPosition start = new KeyPosition("start", 0d, 1d);
        KeyPosition ready = new KeyPosition("ready", 0.2d, 1d);
        KeyPosition relax = new KeyPosition("relax", 0.8d, 1d);
        KeyPosition end = new KeyPosition("end", 1d, 1d);
        addKeyPosition(start);
        addKeyPosition(ready);
        addKeyPosition(relax);
        addKeyPosition(end);
    }

    public void setEmbodiment(MiddlewareEmbodiment re)
    {
    	this.mwe = re;
    }
    
    @Override
    public void setFloatParameterValue(String name, float value) throws ParameterException
    {
    	if (name.startsWith("max_"))
		{
			interpolatingMaxValues.put(name.substring(4), value);
		}
    	else if (name.startsWith("min_"))
		{
			interpolatingMinValues.put(name.substring(4), value);
		
		}
    	else
    	{
    		throw new ParameterException("unknown parameter "+ name );
    	}
    }

    @Override
    public void setParameterValue(String name, String value) throws ParameterException
    {
    	if (name.equals("filename")) 
    	{
    		filename = value;
    	} 
    	else if (name.equals("resource")) 
    	{
    		resource = value;
    	} 
    	else if (name.equals("middlewareloaderclass"))
    	{
    		loaderclass= value;
		} 
    	else if (name.equals("middlewareloaderproperties"))
		{
			propsAsString=value;
			props = new Properties();
			
			//parse the properties for this middleware
			List<String> ps = new ArrayList<String>(Arrays.asList(value.split(",")));
			for(String p : ps){
				props.put(p.substring(0,p.indexOf(':')),p.substring(p.indexOf(':')+1));
			}

		} 
    	else
		{
            Float f = Floats.tryParse(value);
            if (f!=null)
            {
                setFloatParameterValue(name, f);
            }
            else
            {
            	templateParameterMap.put(name, value);
            }
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
    	return ""+getFloatParameterValue(name);
    }

    @Override
    public float getFloatParameterValue(String name) throws ParameterException
    {
    	if (name.startsWith("max_") && interpolatingMaxValues.containsKey(name.substring(4))) return interpolatingMaxValues.get(name.substring(4)).floatValue(); 
    	if (name.startsWith("min_") && interpolatingMinValues.containsKey(name.substring(4))) return interpolatingMinValues.get(name.substring(4)).floatValue(); 
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
    	prepareMessage();
    	play(0);
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
            msgContentAsString = fileContents;
		}
		catch (Exception ex)
		{
			log.error("Could not fill template. Path: {} File: {}", resource, filename);
			log.error("{}",ex);
		}
    	
    }

    /**
     */
    public void play(double t) throws MUPlayException
    {
    	interpolateMessage((float)t);
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
     * 0<=t<=1
     * given the msgContentAsString, which still contains a number of template parameters, and the parameters for which we have a min and a max value:
     * 	calculate the interpolated value for the parameter, wrt t and min and max; replace the result in the template  
     * @param t
     */
    protected void interpolateMessage(float t)
    {
    	float intensity=0;
    	String tempContent = msgContentAsString;
    	if (t < getKeyPosition("ready").time)
    	{
    		intensity = (float) (t/getKeyPosition("ready").time);
    	} 
    	else if (t < getKeyPosition("relax").time)
    	{
    		intensity = 1f;
    	}
    	else
    	{
    		intensity = 1f-(float) ((t-getKeyPosition("relax").time)/(1-getKeyPosition("relax").time));
    	}
    	for (String key: interpolatingMaxValues.keySet())
    	{
    		float min = interpolatingMinValues.get(key).floatValue();
    		float max = interpolatingMaxValues.get(key).floatValue();
    		float val = min + intensity*(max-min);
    		tempContent = tempContent.replaceAll("\\$\\{"+key+"\\}", ""+val);
    	}
    	try {
			msgContentAsJson = om.readTree(tempContent);
		} catch (Exception e) {
			log.error("Could not interpolate template: {}", e);
		} 
    }
    public void cleanup()
    {
    	try {
			play(1d);
		} catch (MUPlayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
        InterpolatingJsonTemplateMessageMU result = new InterpolatingJsonTemplateMessageMU();
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
        	for (Map.Entry<String,Float> entry : interpolatingMaxValues.entrySet())
        	{
        		result.setFloatParameterValue("max_"+entry.getKey(), entry.getValue());
        	}
        	for (Map.Entry<String,Float> entry : interpolatingMinValues.entrySet())
        	{
        		result.setFloatParameterValue("min"+entry.getKey(), entry.getValue());
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
        return 0d;
    }

	@Override
	public void prepareUnit() {
		// maybe let this one take over the duties of prepareMessage()? check who calls this again, and why...
	}

}
