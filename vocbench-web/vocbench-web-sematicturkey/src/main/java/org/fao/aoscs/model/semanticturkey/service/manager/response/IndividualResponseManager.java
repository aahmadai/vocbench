package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.Individual;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class IndividualResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(IndividualResponseManager.class);
	
	
	/**
	 * @param ontoInfo
	 * @param indName
	 * @param typeName
	 * @return
	 */
	public static XMLResponseREPLY addTypeRequest(OntologyInfo ontoInfo, String indName, String typeName)
	{
		Response resp = getSTModel(ontoInfo).individualService.makeRequest("addType", 
				STModel.par(Individual.indqnameField, indName), 
				STModel.par(Individual.typeqnameField, typeName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
	/**
	 * @param ontoInfo
	 * @param indName
	 * @param typeName
	 * @return
	 */
	public static XMLResponseREPLY removeTypeRequest(OntologyInfo ontoInfo, String indName, String typeName)
	{
		Response resp = getSTModel(ontoInfo).individualService.makeRequest("removeType", 
				STModel.par(Individual.indqnameField, indName), 
				STModel.par(Individual.typeqnameField, typeName), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
}