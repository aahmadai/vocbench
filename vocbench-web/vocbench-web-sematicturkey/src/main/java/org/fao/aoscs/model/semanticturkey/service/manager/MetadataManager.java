package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.HashMap;
import java.util.Map.Entry;

import org.fao.aoscs.domain.ImportPathObject;
import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.MetadataResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class MetadataManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(MetadataManager.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static String getBaseuri(OntologyInfo ontoInfo)
	{
		String baseURI = "";
		XMLResponseREPLY reply = MetadataResponseManager.getBaseuriRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "BaseURI"))
			{
				baseURI = propElement.getAttribute("uri");
			}
		}
		return baseURI;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static String getDefaultNamespace(OntologyInfo ontoInfo)
	{
		String defaultNS = "";
		XMLResponseREPLY reply = MetadataResponseManager.getDefaultNamespaceRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "DefaultNamespace"))
			{
				defaultNS = propElement.getAttribute("ns");
			}
		}
		return defaultNS;
	}
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static HashMap<String, String> getNSPrefixMappings(OntologyInfo ontoInfo)
	{
		HashMap<String, String>nsMap = new HashMap<String, String>();
		XMLResponseREPLY reply = MetadataResponseManager.getNSPrefixMappingsRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			for(Element propElement : STXMLUtility.getChildElementByTagName(dataElement, "Mapping"))
			{
				nsMap.put(propElement.getAttribute("prefix"), propElement.getAttribute("ns"));
			}
		}
		return nsMap;
	}
	
	/**
	 * @param ontoInfo
	 * @param prefix
	 * @param namespace
	 * @return
	 */
	public static boolean setNSPrefixMappingRequest(OntologyInfo ontoInfo, String prefix, String namespace)
	{
		XMLResponseREPLY resp = MetadataResponseManager.setNSPrefixMappingRequest(ontoInfo, prefix, namespace);
		boolean result = resp.isAffirmative();
		return result;
		
	}
	
	/**
	 * @param ontoInfo
	 * @param prefix
	 * @param namespace
	 * @return
	 */
	public static boolean changeNSPrefixMappingRequest(OntologyInfo ontoInfo, String prefix, String namespace)
	{
		XMLResponseREPLY resp = MetadataResponseManager.changeNSPrefixMappingRequest(ontoInfo, prefix, namespace);
		boolean result = resp.isAffirmative();
		return result;
	}
	
	/**
	 * @param ontoInfo
	 * @param namespace
	 * @return
	 */
	public static boolean removeNSPrefixMappingRequest(OntologyInfo ontoInfo, String namespace)
	{
		XMLResponseREPLY resp = MetadataResponseManager.removeNSPrefixMappingRequest(ontoInfo, namespace);
		boolean result = resp.isAffirmative();
		return result;
	}
	
	/**
	 * @return
	 */
	public static ImportPathObject getImports(OntologyInfo ontoInfo)
	{
		ImportPathObject importPathObject = new ImportPathObject();
		XMLResponseREPLY reply = MetadataResponseManager.getImportsRequest(ontoInfo);
		if(reply!=null)
		{
			Element dataElement = reply.getDataElement();
			importPathObject = STXMLUtility.addChildImport(dataElement, importPathObject, null);
		}
		
		return importPathObject;
	}
	
	/**
	 * @param namespace
	 * @return
	 */
	public static String getPrefixForNamespace(OntologyInfo ontoInfo, String namespace)
	{
		HashMap<String, String> nsMap = getNSPrefixMappings(ontoInfo);
		for (Entry<String, String> entry : nsMap.entrySet()) {
            if (entry.getValue().equals(namespace)) {
                return entry.getKey();
            }
        }
		return "";
	}
	
	/**
	 * @param prefix
	 * @return
	 */
	public static String getNamespaceForPrefix(OntologyInfo ontoInfo, String prefix)
	{
		HashMap<String, String> nsMap = getNSPrefixMappings(ontoInfo);
		if(nsMap.containsKey(prefix))
		{
			return nsMap.get(prefix);
		}
		return "";
	}
	
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @return
	 */
	public static boolean addFromWeb(OntologyInfo ontoInfo, String baseuri, String altURL)
	{
		XMLResponseREPLY resp = MetadataResponseManager.addFromWebRequest(ontoInfo, baseuri, altURL);
		return resp.isAffirmative();
	}

	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param ontFileName
	 * @return
	 */
	public static boolean addFromWebToMirror(OntologyInfo ontoInfo, String baseuri, String altURL, String mirrorFile)
	{
		XMLResponseREPLY resp = MetadataResponseManager.addFromWebToMirrorRequest(ontoInfo, baseuri, altURL, mirrorFile);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param localPath
	 * @param ontFileName
	 * @return
	 */
	public static boolean addFromLocalFile(OntologyInfo ontoInfo, String baseuri, String localFilePath, String mirrorFile)
	{
		XMLResponseREPLY resp = MetadataResponseManager.addFromLocalFileRequest(ontoInfo, baseuri, localFilePath, mirrorFile);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param ontFileName
	 * @return
	 */
	public static boolean addFromOntologyMirror(OntologyInfo ontoInfo, String baseuri, String mirrorFile)
	{
		XMLResponseREPLY resp = MetadataResponseManager.addFromOntologyMirrorRequest(ontoInfo, baseuri, mirrorFile);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @return
	 */
	public static boolean removeImport(OntologyInfo ontoInfo, String baseuri)
	{
		XMLResponseREPLY resp = MetadataResponseManager.removeImportRequest(ontoInfo, baseuri);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param baseuri
	 * @param ontFileName
	 * @return
	 */
	public static boolean mirrorOntology(OntologyInfo ontoInfo, String baseuri, String mirrorFile)
	{
		XMLResponseREPLY resp = MetadataResponseManager.mirrorOntologyRequest(ontoInfo, baseuri, mirrorFile);
		return resp.isAffirmative();
	}
	
}
