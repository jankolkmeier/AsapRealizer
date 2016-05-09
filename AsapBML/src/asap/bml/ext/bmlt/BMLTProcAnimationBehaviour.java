/*******************************************************************************
 *******************************************************************************/
package asap.bml.ext.bmlt;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ImmutableList;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLScanException;
import hmi.xml.XMLTokenizer;
import lombok.Getter;
import saiba.bml.parser.SyncPoint;
/**
 * Procedural animation behavior 
 * @author welberge
 */
public class BMLTProcAnimationBehaviour extends BMLTBehaviour
{
    @Getter
    private String name;

    @Getter
    private String content = null;

    @Getter
    private String fileName = null;
    
    @Override
    public void addDefaultSyncPoints()
    {
        for(String s:getDefaultSyncPoints())
        {
            addSyncPoint(new SyncPoint(bmlId, id, s));
        }        
    }
    
    @Override
    public boolean hasContent()
    {
        return true;
    }
    
    @Override
    public void decodeAttributes(HashMap<String, String> attrMap, XMLTokenizer tokenizer)
    {
        fileName = getOptionalAttribute("fileName", attrMap, null);
        name = getOptionalAttribute("name", attrMap, null);
        super.decodeAttributes(attrMap, tokenizer);
    }
    
    @Override
    public StringBuilder appendAttributeString(StringBuilder buf, XMLFormatting fmt)
    {
        if(fileName!=null)
        {
            appendAttribute(buf,"fileName",fileName);
        }
        if(name!=null)
        {
            appendAttribute(buf, "name", name);
        }
        return super.appendAttributeString(buf, fmt);
    }
    
    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        while (tokenizer.atSTag())
        {
            String tag = tokenizer.getTagName();
            if (tag.equals(BMLTParameter.xmlTag()))
            {
                decodeBMLTParameter(tokenizer);
            }
            else if (tag.equals("ProcAnimation"))
            {
                content = tokenizer.getXMLSection();
            }
            else
            {
                throw new XMLScanException("Invalid content " + tag + " in BMLTBehavior " + id);
            }
        }
        super.decodeContent(tokenizer);
    }
    
    @Override
    public boolean satisfiesConstraint(String n, String value)
    {
        if (n.equals("name") && value.equals(name)) return true;
        return super.satisfiesConstraint(n, value);
    }
    
    private static final List<String> DEFAULT_SYNCS = ImmutableList.of("start","end");    
    public static List<String> getDefaultSyncPoints()
    {
        return DEFAULT_SYNCS;
    }

    public BMLTProcAnimationBehaviour(String bmlId,XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId);
        readXML(tokenizer);
    }
    
    public BMLTProcAnimationBehaviour(String bmlId,String id, XMLTokenizer tokenizer) throws IOException
    {
        super(bmlId,id);
        readXML(tokenizer);
    }

    private static final String XMLTAG = "procanimation";

    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
}
