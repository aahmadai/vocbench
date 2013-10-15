package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SKOSResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

/**
 * @author rajbhandari
 *
 */
public class SKOSManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SKOSManager.class);
	
	/**
	 * @param ontoInfo
	 * @param resourceURI
	 * @param language
	 * @return
	 */
	public static String getShow(OntologyInfo ontoInfo, String resourceURI, String language)
	{
		String showValue = null;
		XMLResponseREPLY resp = SKOSResponseManager.getShowRequest(ontoInfo, resourceURI, language);
		if(resp!=null)
		{
			Element dataElement = ((XMLResponseREPLY) resp).getDataElement();
			for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "show"))
			{
				showValue = skosElem.getAttribute("value");
			}
		}
		return showValue;
	}
	
	
	/**
	 * @param schemeURI
	 * @return
	 */
	public static ArrayList<String> showSKOSConceptsTree(OntologyInfo ontoInfo, String schemeURI)
	{
		XMLResponseREPLY resp = SKOSResponseManager.showSKOSConceptsTree(ontoInfo, schemeURI);
		return STXMLUtility.getConceptTree(resp);
	}
	
	/**
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static boolean isTopConcept(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		boolean chk = false;
		Response resp = SKOSResponseManager.isTopConceptRequest(ontoInfo, conceptURI, schemeURI);
		Element dataElement = ((XMLResponseREPLY) resp).getDataElement();
		for(Element skosElem : STXMLUtility.getChildElementByTagName(dataElement, "value"))
		{
			chk = skosElem.getTextContent().equals("true");
		}
		return chk;
	}
	
	/**
	 * @param schemeURI
	 * @param defaultLanguage
	 * @return
	 */
	public static ArrayList<String> getTopConceptsURI(OntologyInfo ontoInfo, String schemeURI, String defaultLanguage)
	{
		XMLResponseREPLY resp = SKOSResponseManager.getTopConceptsRequest(ontoInfo, schemeURI, defaultLanguage);
		return STXMLUtility.getResourcesURI(resp);
	}
	
	/**
	 * 
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static ArrayList<String> getNarrowerConceptsURI(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		XMLResponseREPLY resp = SKOSResponseManager.getNarrowerConceptRequest(ontoInfo, conceptURI, schemeURI, true);
		return STXMLUtility.getResourcesURI(resp);
	}
	
	/**
	 * 
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static ArrayList<String> getBroaderConceptsURI(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		XMLResponseREPLY resp = SKOSResponseManager.getBroaderConceptRequest(ontoInfo, conceptURI, schemeURI, true);
		if(resp!=null)
			return STXMLUtility.getResourcesURI(resp);
		else
			return null;
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 */
	public static void addTopConcept(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		SKOSResponseManager.addTopConceptRequest(ontoInfo, conceptURI, schemeURI);
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 */
	public static void removeTopConcept(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		SKOSResponseManager.removeTopConceptRequest(ontoInfo, conceptURI, schemeURI);
	}
	
	
	/**
	 * @param conceptURI
	 * @param broaderConceptURI
	 */
	public static void addBroaderConcept(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI)
	{
		SKOSResponseManager.addBroaderConceptRequest(ontoInfo, conceptURI, broaderConceptURI);
	}
	
	
	/**
	 * @param conceptURI
	 * @param broaderConceptURI
	 */
	public static void removeBroaderConcept(OntologyInfo ontoInfo, String conceptURI, String broaderConceptURI)
	{
		SKOSResponseManager.removeBroaderConceptRequest(ontoInfo, conceptURI, broaderConceptURI);
	}
	
	
	/**
	 * @param conceptURI
	 */
	public static void deleteConcept(OntologyInfo ontoInfo, String conceptURI)
	{
		SKOSResponseManager.deleteConceptRequest(ontoInfo, conceptURI);
	}
	
	/**
	 * @param ontoInfo
	 * @param defaultLanguage
	 * @return
	 */
	public static ArrayList<String[]> getAllSchemesList(OntologyInfo ontoInfo)
	{
		ArrayList<String[]> schemesList = new ArrayList<String[]>();

		XMLResponseREPLY resp = SKOSResponseManager.getAllSchemesListRequest(ontoInfo, STXMLUtility.SKOS_LANG_SCHEME);
		Element dataElement = resp.getDataElement();
		for(Element colElem : STXMLUtility.getChildElementByTagName(dataElement, "collection"))
		{
			for(Element uriElem : STXMLUtility.getChildElementByTagName(colElem, "uri"))
			{
				String[] tmp = new String[2];
				tmp[0] = uriElem.getAttribute("show");
				tmp[1] = uriElem.getTextContent();
				schemesList.add(tmp);
				
			}
		}
		return schemesList;
	}
	
	/**
	 * @param ontoInfo
	 * @param scheme
	 * @param preferredLabel
	 * @param preferredLabelLanguage
	 * @param language
	 * @return
	 */
	public static boolean createScheme(OntologyInfo ontoInfo, String scheme, String preferredLabel, String preferredLabelLanguage, String language)
	{
		Response resp = SKOSResponseManager.createSchemeRequest(ontoInfo, scheme, preferredLabel, preferredLabelLanguage, language);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param scheme
	 * @param setForceDeleteDanglingConcepts
	 * @param forceDeleteDanglingConcepts
	 * @return
	 */
	public static boolean deleteScheme(OntologyInfo ontoInfo, String scheme, Boolean setForceDeleteDanglingConcepts, Boolean forceDeleteDanglingConcepts)
	{
		Response resp = SKOSResponseManager.deleteSchemeRequest(ontoInfo, scheme, setForceDeleteDanglingConcepts, forceDeleteDanglingConcepts);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static boolean addConceptToScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = SKOSResponseManager.addConceptToSchemeRequest(ontoInfo, conceptURI, schemeURI);
		return resp.isAffirmative();
	}
	
	/**
	 * @param ontoInfo
	 * @param conceptURI
	 * @param schemeURI
	 * @return
	 */
	public static boolean removeConceptFromScheme(OntologyInfo ontoInfo, String conceptURI, String schemeURI)
	{
		Response resp = SKOSResponseManager.removeConceptFromSchemeRequest(ontoInfo, conceptURI, schemeURI);
		return resp.isAffirmative();
	}
	
}
