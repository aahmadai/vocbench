package org.fao.aoscs.model.semanticturkey.service.manager.response;

import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SPARQL;

import org.fao.aoscs.domain.OntologyInfo;
import org.fao.aoscs.model.semanticturkey.util.STModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rajbhandari
 *
 */
public class SparqlResponseManager extends ResponseManager {
	
	protected static Logger logger = LoggerFactory.getLogger(SparqlResponseManager.class);
	
	/**
	 * @param ontoInfo
	 * @param query
	 * @param language
	 * @param infer
	 * @return
	 */
	public static XMLResponseREPLY resolveQueryRequest(OntologyInfo ontoInfo, String query, String language, Boolean infer, String mode) {
		Response resp = getSTModel(ontoInfo).sparqlService.makeRequest(SPARQL.resolveQueryRequest,
				STModel.par(SPARQL.queryPar, query), 
				STModel.par(SPARQL.languagePar, language), 
				STModel.par(SPARQL.inferPar, infer.toString()), 
				STModel.par(SPARQL.modePar, mode), 
				STModel.par("ctx_project", ontoInfo.getDbTableName()));
		return getXMLResponseREPLY(resp);
	}
	
}
