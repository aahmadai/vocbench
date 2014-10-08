package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SKOSICVResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(ResourceViewResponseManager.class);
	
	public static XMLResponseREPLY listDanglingConceptsRequest(OntologyInfo ontoInfo)
	{
		
		Response resp = getSTModel(ontoInfo).icvService.makeNewRequest("listDanglingConcepts", 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
