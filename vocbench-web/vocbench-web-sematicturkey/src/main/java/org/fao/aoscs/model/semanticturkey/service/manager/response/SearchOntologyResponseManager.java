package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.OntoSearch;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SearchOntologyResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SearchOntologyResponseManager.class);
	
	/**
	 * @param inputString
	 * @param types
	 * @return
	 */
	public static XMLResponseREPLY searchOntologyRequest(OntologyInfo ontoInfo, String inputString, String types)
	{
		Response resp = getSTModel(ontoInfo).searchOntologyService.makeRequest(OntoSearch.searchOntologyRequest, STModel.par("inputString", inputString), STModel.par("types", types));
		return getXMLResponseREPLY(resp);
	}
	
}
