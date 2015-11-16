package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.IndividualResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class IndividualManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(IndividualManager.class);
	
	/**
	 * @param ontoInfo
	 * @param indName
	 * @param typeName
	 * @return
	 */
	public static boolean addType(OntologyInfo ontoInfo, String indName, String typeName)
	{
		XMLResponseREPLY reply = IndividualResponseManager.addTypeRequest(ontoInfo, indName, typeName);
		return getReplyStatus(reply);
	}
	
	/**
	 * @param ontoInfo
	 * @param indName
	 * @param typeName
	 * @return
	 */
	public static boolean removeType(OntologyInfo ontoInfo, String indName, String typeName)
	{
		XMLResponseREPLY reply = IndividualResponseManager.removeTypeRequest(ontoInfo, indName, typeName);
		return getReplyStatus(reply);
	}
	
}
