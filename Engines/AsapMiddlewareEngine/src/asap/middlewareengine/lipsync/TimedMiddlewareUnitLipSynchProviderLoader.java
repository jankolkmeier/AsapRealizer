/*******************************************************************************
 *******************************************************************************/
package asap.middlewareengine.lipsync;

import java.io.IOException;
import java.util.HashMap;

import asap.middlewareengine.engine.MiddlewareEngineLoader;
import asap.middlewareengine.viseme.MiddlewareVisemeBinding;
import asap.middlewareengine.viseme.VisemeToJsonMapping;
import asap.realizer.lipsync.LipSynchProvider;
import asap.realizerembodiments.AsapRealizerEmbodiment;
import asap.realizerembodiments.LipSynchProviderLoader;
import hmi.environmentbase.Environment;
import hmi.environmentbase.Loader;
import hmi.util.ArrayUtils;
import hmi.util.Resources;
import hmi.xml.XMLStructureAdapter;
import hmi.xml.XMLTokenizer;

//TODO: document format of this loader's XML
/**
 * Loads a TimedMiddlewareUnitLipSynchProvider
 * @author Dennis Reidsma
 */
public class TimedMiddlewareUnitLipSynchProviderLoader implements LipSynchProviderLoader
{
    private String id;
    private LipSynchProvider lipSyncProvider;

    public void setId(String newId)
    {
        id = newId;
    }

    @Override
    public String getId()
    {
        return id;
    }

    @Override
    public void readXML(XMLTokenizer tokenizer, String loaderId, String vhId, String vhName, Environment[] environments,
            Loader... requiredLoaders) throws IOException
    {
        setId(loaderId);

        MiddlewareEngineLoader mwel = ArrayUtils.getFirstClassOfType(requiredLoaders, MiddlewareEngineLoader.class);
        if (mwel == null)
        {
            throw tokenizer.getXMLScanException("TimedMiddlewareUnitLipSynchProviderLoader requires MiddlewareEngineLoader.");
        }

        AsapRealizerEmbodiment are = ArrayUtils.getFirstClassOfType(requiredLoaders, AsapRealizerEmbodiment.class);
        if (are == null)
        {
            throw new RuntimeException(
                    "TimedMiddlewareUnitLipSynchProviderLoader requires an EmbodimentLoader containing a AsapRealizerEmbodiment");
        }
        if (!tokenizer.atSTag("MiddlewareVisemeBinding"))
        {
            throw new RuntimeException(
                    "TimedMiddlewareUnitLipSynchProviderLoader requires a child element of type MiddlewareVisemeBinding with attribute filename (and optionally attribute resources)");
        }
        MiddlewareVisemeBinding visBinding;
        VisemeToJsonMapping mapping = new VisemeToJsonMapping();
        XMLStructureAdapter adapter = new XMLStructureAdapter();
        HashMap<String, String> attrMap = tokenizer.getAttributes();
        mapping.readXML(new Resources(adapter.getOptionalAttribute("resources", attrMap, "")).getReader(adapter.getRequiredAttribute("filename", attrMap, tokenizer)));
        visBinding = new MiddlewareVisemeBinding(mapping);
        tokenizer.takeEmptyElement("MiddlewareVisemeBinding");


        lipSyncProvider = new TimedMiddlewareUnitLipSynchProvider(visBinding, mwel.getMiddlewareEmbodiment(), mwel.getPlanManager(), are.getPegBoard());
    }       

    @Override
    public void unload()
    {

    }

    @Override
    public LipSynchProvider getLipSyncProvider()
    {
        return lipSyncProvider;
    }
}
