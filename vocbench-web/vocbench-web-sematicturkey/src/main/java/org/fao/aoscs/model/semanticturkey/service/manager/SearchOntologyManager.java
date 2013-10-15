package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import java.util.ArrayList;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.domain.SearchParameterObject;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.SearchOntologyResponseManager;
import org.fao.aoscs.model.semanticturkey.util.STXMLUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SearchOntologyManager extends ResponseManager{
	
	protected static Logger logger = LoggerFactory.getLogger(SearchOntologyManager.class);
	
	public static String SEARCHTYPE_SKOSXL = "skosxl";

	/**
	 * @param searchObj
	 * @return
	 */
	public static ArrayList<String> searchOntologyRequest(OntologyInfo ontoInfo, SearchParameterObject searchObj)
	{
		XMLResponseREPLY resp = SearchOntologyResponseManager.searchOntologyRequest(ontoInfo, searchObj.getKeyword(), SEARCHTYPE_SKOSXL);
		return STXMLUtility.getResourcesURI(resp);
	}
}
