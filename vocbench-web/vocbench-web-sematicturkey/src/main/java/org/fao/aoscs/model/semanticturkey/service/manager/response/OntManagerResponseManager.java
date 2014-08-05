package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.OntManager;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class OntManagerResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(OntManagerResponseManager.class);
	
	/**
	 * @return
	 */
	public static XMLResponseREPLY getOntManagerParametersRequest(OntologyInfo ontoInfo, String ontMgrID)
	{
		Response resp = getSTModel(ontoInfo).ontManagerService.makeRequest(OntManager.getOntManagerParametersRequest, 
				STModel.par(OntManager.ontMgrIDField, ontMgrID), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
