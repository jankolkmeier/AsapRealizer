/*******************************************************************************
 * 
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
package asap.middlewareengine.embodiment;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hmi.environmentbase.Embodiment;
import hmi.environmentbase.EmbodimentLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.xml.XMLScanException;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;
import lombok.extern.slf4j.Slf4j;
import nl.utwente.hmi.middleware.Middleware;
import nl.utwente.hmi.middleware.loader.GenericMiddlewareLoader;

/**
*/
@Slf4j
public class MiddlewareEmbodiment implements Embodiment, EmbodimentLoader
{
    private String id = "";
    private static Logger logger = LoggerFactory.getLogger(MiddlewareEmbodiment.class.getName());
    private XMLStructureAdapter adapter = new XMLStructureAdapter();
    
    public void setId(String id)
    {
        this.id = id;
    }

    @Override
    public String getId()
    {
        return id;
    }
    

    @Override
    public void readXML(XMLTokenizer theTokenizer, String loaderId, String vhId, String vhName, Environment[] environments, Loader ... requiredLoaders) 
    	throws IOException
    {
        setId(loaderId);
        if(!theTokenizer.atSTag("MiddlewareOptions"))
        {
            throw new XMLScanException("MiddlewareEmbodiment requires an inner MiddlewareOptions element");            
        }
        String middlewareLoader = null;
        Properties middlewareProperties = null;
        while (theTokenizer.atSTag("MiddlewareOptions"))
        {
	        HashMap<String, String> attrMap = theTokenizer.getAttributes();
	        XMLStructureAdapter adapter = new XMLStructureAdapter();
	        middlewareLoader = adapter.getRequiredAttribute("loaderclass", attrMap, theTokenizer);
	
	        theTokenizer.takeSTag("MiddlewareOptions");
	
	        middlewareProperties = new Properties();
	        boolean isdefault = false;
	        while (theTokenizer.atSTag("MiddlewareProperty"))
	        {
	            HashMap<String, String> attrMap2 = theTokenizer.getAttributes();
	            XMLStructureAdapter adapter2 = new XMLStructureAdapter();
	            String name = adapter2.getRequiredAttribute("name", attrMap, theTokenizer);
	            String value = adapter2.getRequiredAttribute("value", attrMap, theTokenizer);
	            middlewareProperties.put(name, value);
	            if (name.equals("default")&&value.equals("true")){
	            	isdefault = true;
	            }
	            theTokenizer.takeSTag("MiddlewareProperty");
	            theTokenizer.takeETag("MiddlewareProperty"); 
	        }
	        if (isdefault)
	        {
	        	defaultMiddlewareLoader = middlewareLoader;
	        	defaultMiddlewareProperties = middlewareProperties;
	        }
	        
	        
	        theTokenizer.takeETag("MiddlewareOptions");
	        getCachedMiddlewareChannel(middlewareLoader,middlewareProperties);
        }
        
        if (defaultMiddlewareLoader.equals("")) defaultMiddlewareLoader = middlewareLoader;
        if (defaultMiddlewareProperties==null) defaultMiddlewareProperties = middlewareProperties;
        
        if (!theTokenizer.atETag("Loader")) throw new XMLScanException("MiddlewareEmbodiment can only have MiddlewareOptions elements");
        
        return;
    }

    @Override
    public void unload()
    {
    	//TODO: unregister and stuff?
        
    	//con.disconnect();
    }

    @Override
    public Embodiment getEmbodiment()
    {
        return this;
    }
  
    /** ================================================================================
    
    				ACCESS TO THE CACHED MIDDLEWARE CHANNELS
    				
        ================================================================================ */
    
    protected Map<Map.Entry<String,Properties>, Middleware> middlewareChannels = new HashMap<Map.Entry<String,Properties>,Middleware>();
    protected String defaultMiddlewareLoader = "";
    protected Properties defaultMiddlewareProperties = new Properties();
    
    /** 
     * given the loader class and properties, this function will first look in the middleware  
     * channel cache to see whether there already is a channel with the given parameters. If yes, 
     * this is returned; if no, a new channel is prepared and cached before returning. Method
     * uses exact these properties, does not add the default props anymore (that's done by calling method)
     * 
     * @param specificLoader
     * @param ps
     * @return
     */
    protected Middleware getCachedMiddlewareChannel(String specificLoader, Properties ps)
    {
    	Map.Entry<String,Properties> entry = new AbstractMap.SimpleEntry<String,Properties>(specificLoader,ps); 
    	Middleware mw = middlewareChannels.get(entry);
    	if (mw == null)
    	{
            GenericMiddlewareLoader gml = new GenericMiddlewareLoader(specificLoader, ps);
            mw = gml.load();
            middlewareChannels.put(entry, mw);
    	}
    	if (mw == null)
    	{
    		log.error("Cannot create requested middleware channel {} with properties {}", specificLoader, ps);
    		throw new RuntimeException("cannot load middleware");
    	}
    	return mw;
    }
    /** return the default middleware channel */
    public Middleware getMiddleware()
    {
    	return getCachedMiddlewareChannel(defaultMiddlewareLoader, defaultMiddlewareProperties);
    }
    /** return the middleware channel with default loader type; use specified properties to override default properties */
    public Middleware getMiddleware(Properties ps)
    {
    	Properties actualProps = new Properties();
		actualProps.putAll(defaultMiddlewareProperties);
		actualProps.putAll(ps);
		return getCachedMiddlewareChannel(defaultMiddlewareLoader, actualProps);
    }
    /** return the middleware channel with given loader type and properties, ignoring the default settings altogether */
    public Middleware getMiddleware(String specificLoader, Properties ps)
    {
    	return getCachedMiddlewareChannel(specificLoader, ps);
    }
 }
