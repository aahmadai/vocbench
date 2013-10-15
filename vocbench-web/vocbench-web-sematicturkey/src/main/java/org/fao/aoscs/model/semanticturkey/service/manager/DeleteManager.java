package org.fao.aoscs.model.semanticturkey.service.manager;

import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.service.manager.response.DeleteResponseManager;
import org.fao.aoscs.model.semanticturkey.service.manager.response.ResponseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class DeleteManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(DeleteManager.class);
	
	/**
	 * @param clsName
	 * @return
	 */
	public static boolean deleteProperty(OntologyInfo ontoInfo, String propertyUri)
	{
		XMLResponseREPLY reply = DeleteResponseManager.removePropertyRequest(ontoInfo, propertyUri);
		return getReplyStatus(reply);
	}
	
	
	
}
