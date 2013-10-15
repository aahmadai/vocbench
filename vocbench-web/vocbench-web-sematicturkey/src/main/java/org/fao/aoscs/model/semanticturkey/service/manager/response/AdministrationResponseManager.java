package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import org.fao.aoscs.domain.OntologyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class AdministrationResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(AdministrationResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @return
	 */
	public static XMLResponseREPLY getOntologyMirror(OntologyInfo ontoInfo)
	{
		// TODO replace hardcoded request "getOntologyMirror" with proper constants from ST
		Response resp = getSTModel(ontoInfo).administrationService.makeRequest("getOntologyMirror");
		logger.info(resp.getResponseContent());
		return getXMLResponseREPLY(resp);
	}
}
