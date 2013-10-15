package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Delete;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class DeleteResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(DeleteResponseManager.class);
	
	/**
	 * @param propertyUri
	 * @return
	 */
	public static XMLResponseREPLY removePropertyRequest(OntologyInfo ontoInfo, String propertyUri)
	{
		Response resp = getSTModel(ontoInfo).deleteService.makeRequest(Delete.removePropertyRequest, STModel.par("name", propertyUri));
		return getXMLResponseREPLY(resp);
	}
}