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
	public static XMLResponseREPLY removePropertyRequest(OntologyInfo ontoInfo, String uri)
	{
		//TODO on ST UPDATE : Replacing property uri with local name. After ST update replace back with uri.
		String name = uri;
		if (uri.contains("#"))			
			name = uri.split("#")[1];
		else {
			String[] contents = uri.split("/");
			name = contents[contents.length-1];
		}
		Response resp = getSTModel(ontoInfo).deleteService.makeRequest(Delete.removePropertyRequest, STModel.par("name", name));
		return getXMLResponseREPLY(resp);
	}
}