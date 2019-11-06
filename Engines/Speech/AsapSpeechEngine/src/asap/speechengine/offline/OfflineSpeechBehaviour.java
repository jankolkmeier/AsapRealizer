package asap.speechengine.offline;

import java.io.IOException;
import java.util.HashMap;

import hmi.xml.XMLFormatting;
import hmi.xml.XMLTokenizer;
import saiba.bml.core.SpeechBehaviour;

public class OfflineSpeechBehaviour extends SpeechBehaviour {

	public OfflineSpeechBehaviour(String bmlId, String id, XMLTokenizer tokenizer) throws IOException {
		super(bmlId, id, tokenizer);
	}

    @Override
    public void decodeAttributes(HashMap<String, String> attrMap,
            XMLTokenizer tokenizer)
    {
        // empty, 'cause id is not required
    }

    @Override
    public void decodeContent(XMLTokenizer tokenizer) throws IOException
    {
        content = tokenizer.getXMLSectionContent();        
    }

    /*
     * The XML Stag for XML encoding
     */
    private static final String XMLTAG = "offline";

    /**
     * The XML Stag for XML encoding -- use this static method when you want to
     * see if a given String equals the xml tag for this class
     */
    public static String xmlTag()
    {
        return XMLTAG;
    }

    @Override
    public StringBuilder appendContent(StringBuilder buf, XMLFormatting fmt)
    {
        if (content != null) buf.append(content);
        return buf;
    }
    /**
     * The XML Stag for XML encoding -- use this method to find out the run-time
     * xml tag of an object
     */
    @Override
    public String getXMLTag()
    {
        return XMLTAG;
    }
    
    // TODO: ...
    static final String NAMESPACE = "http://www.yamaha.co.jp/vocaloid/schema/vsq3/";
    
    @Override
    public  String getNamespace() { return NAMESPACE; }
    

}
