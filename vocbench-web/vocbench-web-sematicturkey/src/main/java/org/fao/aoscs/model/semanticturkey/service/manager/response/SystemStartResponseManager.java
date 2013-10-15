package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SystemStart;

import org.fao.aoscs.domain.OntologyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SystemStartResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SystemStartResponseManager.class);
	
	/**
	 * @return
	 */
	public static XMLResponseREPLY listTripleStoresRequest(OntologyInfo ontoInfo)
	{
		Response resp = getSTModel(ontoInfo).systemStartService.makeRequest(SystemStart.listTripleStoresRequest);
		return getXMLResponseREPLY(resp);
	}
	
}
